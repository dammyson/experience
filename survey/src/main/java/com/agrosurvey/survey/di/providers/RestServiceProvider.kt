package com.agrosurvey.survey.di.providers

import android.util.Log
import com.agrosurvey.survey.data.remote.NetworkResponseAdapterFactory
import com.agrosurvey.survey.model.api.adapters.FieldTypeAdapter
import com.agrosurvey.survey.model.api.adapters.OffsetDatetimeAdapter
//import com.agrosurvey.survey.model.api.adapters.QuestionLogicAdapter
import com.agrosurvey.survey.model.interceptors.AuthInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Provider
import java.net.SocketTimeoutException
import kotlin.jvm.Throws


class RestServiceProvider(val baseUrl : String) : Provider<Retrofit> {

    private var httpClient : OkHttpClient? = null

    override fun get(): Retrofit {
        if (httpClient == null) {
            val interceptor = HttpLoggingInterceptor { message -> Log.e("REQUEST", message) }
            interceptor.level = HttpLoggingInterceptor.Level.BASIC


            httpClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .addInterceptor(interceptor)
                .addInterceptor { chain ->
                    onOnIntercept(chain)
                }
                .writeTimeout(26, TimeUnit.SECONDS)
                .readTimeout(26, TimeUnit.SECONDS)
                .build()
        }


        val moshi = Moshi.Builder()
            .add(FieldTypeAdapter())
            //.add(QuestionLogicAdapter())
            .add(OffsetDatetimeAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            //.addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

    }

    @Throws(IOException::class)
    private fun onOnIntercept(chain: Interceptor.Chain): Response {
        try {
            val response = chain.proceed(chain.request())
            val content = response.body()?.string()
            Log.e("CONTENT", "==  $content")
            return response.newBuilder()
                .body(ResponseBody.create(response.body()?.contentType(), content)).build()
        } catch (exception: SocketTimeoutException) {
            exception.printStackTrace()
        }

        return chain.proceed(chain.request())
    }
}