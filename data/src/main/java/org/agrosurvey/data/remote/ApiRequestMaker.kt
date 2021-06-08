package org.agrosurvey.data.remote

import com.agrosurvey.data.BuildConfig.API_BASE_URL
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject


class ApiRequestMaker @Inject constructor() {

    fun makeGetRequest(
        endpoint: String,
        requestHeaders: RequestHeaders
    ): Call {

        val client = OkHttpClient().newBuilder().build()

        val request: Request = Request.Builder()
            .url("${API_BASE_URL}agrosurvey$endpoint")
            .get()
            .addHeaders(requestHeaders.map())
            .build()
        return client.newCall(request)
    }

    private fun Request.Builder.addHeaders(map: HashMap<String, String>): Request.Builder {
        return apply {
            map.keys.forEach { name ->
                addHeader(name, map[name].orEmpty())
            }
        }
    }
}