package org.agrosurvey.data.remote.auth

import org.agrosurvey.data.remote.ApiResponse
import org.agrosurvey.data.remote.RequestHeaders
import org.agrosurvey.domain.entities.get.Organisation
import org.agrosurvey.domain.entities.get.User
import org.agrosurvey.domain.entities.post.LoginPost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(val service: AuthService) {

    fun signIn(
        payloadForLogin: LoginPost,
        onResult: (ApiResponse<User>) -> Unit
    ) {
        service.login(payloadForLogin).enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                onResult(ApiResponse.create(response))
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                onResult(ApiResponse.create(t))
            }
        })
    }

    fun getOrganisations(
        requestHeaders: RequestHeaders,
        onResult: (ApiResponse<List<Organisation>>) -> Unit
    ) {
        service.organisations(requestHeaders).enqueue(object : Callback<List<Organisation>> {
            override fun onResponse(
                call: Call<List<Organisation>>,
                response: Response<List<Organisation>>
            ) {
                onResult(ApiResponse.create(response))
            }

            override fun onFailure(call: Call<List<Organisation>>, t: Throwable) {
                onResult(ApiResponse.create(t))
            }

        })
    }
}