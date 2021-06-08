package org.agrosurvey.data.remote.auth

import com.agrosurvey.data.BuildConfig.API_BASE_URL
import okhttp3.OkHttpClient
import org.agrosurvey.data.remote.RequestHeaders
import org.agrosurvey.domain.entities.get.Organisation
import org.agrosurvey.domain.entities.get.User
import org.agrosurvey.domain.entities.post.LoginPost
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class AuthService @Inject constructor() {

    fun login(payload: LoginPost): Call<User> =
        authApi.signIn(
            headers = hashMapOf(Pair("Accept", "")),
            email = payload.email,
            password = payload.password
        )

    fun organisations(headers: RequestHeaders): Call<List<Organisation>> {
        return authApi.getUserOrganizations(headers.map())
    }

    private val authApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
            .create(AuthApi::class.java)
    }

}