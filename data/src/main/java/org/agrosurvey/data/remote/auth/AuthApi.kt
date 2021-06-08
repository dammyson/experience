package org.agrosurvey.data.remote.auth

import org.agrosurvey.data.remote.ApiEndpoints.loginEndpoint
import org.agrosurvey.data.remote.ApiEndpoints.organisationsEndpoint
import org.agrosurvey.domain.entities.get.Organisation
import org.agrosurvey.domain.entities.get.User
import retrofit2.http.*


interface AuthApi {
    @FormUrlEncoded
    @POST
    fun signIn(
        @Field("email") email: String,
        @Field("password") password: String,
        @HeaderMap headers: Map<String, String>,
        @Url endpoint: String = loginEndpoint()
    ): retrofit2.Call<User>


    @GET
    fun getUserOrganizations(
        @HeaderMap headers: Map<String, String>,
        @Url endpoint: String = organisationsEndpoint()
    ): retrofit2.Call<List<Organisation>>

}