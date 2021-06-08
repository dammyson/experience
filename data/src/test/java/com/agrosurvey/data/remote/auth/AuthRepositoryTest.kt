package com.agrosurvey.data.remote.auth

import com.google.common.truth.Truth.assertThat
import com.google.gson.GsonBuilder
import org.agrosurvey.data.remote.ApiErrorResponse
import org.agrosurvey.data.remote.ApiErrorType
import org.agrosurvey.data.remote.ApiSuccessResponse
import org.agrosurvey.data.remote.auth.AuthRepository
import org.agrosurvey.data.remote.auth.AuthService
import org.agrosurvey.domain.entities.post.LoginPost
import org.junit.Test


class AuthRepositoryTest {


    @Test
    fun signInWithCorrectLogin_Returns_ApiSuccessResponse() {

        val authService = AuthService()
        val repository = AuthRepository(authService)

        val loginPost = LoginPost(email = "adriankbarnes@agrosfer.org", password = "testpass")
        val expected = "{\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Adrian Koomson-Barnes\",\n" +
                "      \"username\": \"adriankbarnes@agrosfer.org\",\n" +
                "      \"email\": \"adriankbarnes@agrosfer.org\",\n" +
                "      \"phone_number\": \"+22951997247\",\n" +
                "      \"phone_number_verified_at\": \"2021-01-25 13:25:12\",\n" +
                "      \"created_at\": \"2021-01-25T13:25:12.000000Z\",\n" +
                "      \"updated_at\": \"2021-01-25T13:25:12.000000Z\"\n" +
                "    }"

        repository.signIn(loginPost) { actual ->
            assertThat(actual is ApiSuccessResponse).isTrue()

            val actualBody = (actual as ApiSuccessResponse).body
            val gson = GsonBuilder().setPrettyPrinting().create()

            assertThat(gson.toJson(actualBody)).isEqualTo(expected)
        }

    }

    @Test
    fun signInWithWrongLogin_Returns_ApiErrorResponse() {

        val authService = AuthService()
        val repository = AuthRepository(authService)

        val loginPost = LoginPost(email = "testuser@agrosfer.org", password = "testpass")

        repository.signIn(loginPost) { actual ->
            assertThat(actual is ApiErrorResponse).isTrue()
            val response = (actual as ApiErrorResponse)
            assertThat(response.errorType).isEqualTo(ApiErrorType.INVALID_CREDENTIALS)
        }

    }
}