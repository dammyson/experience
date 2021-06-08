package org.agrosurvey.data.remote

import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.regex.Pattern

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return when (error) {
                is SocketTimeoutException -> {
                    ApiErrorResponse(error.message ?: "Timeout", ApiErrorType.TIMEOUT)
                }

                is UnknownHostException -> {
                    ApiErrorResponse(
                        error.message ?: "Host is unreachable",
                        ApiErrorType.UNKNOWN_HOST
                    )
                }

                else -> ApiErrorResponse(
                    error.message ?: "Unknow errpr",
                    ApiErrorType.UNKNOWN_ERROR
                )
            }

        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(
                        body = body,
                        linkHeader = response.headers().get("link")
                    )
                }
            } else {
                var msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else msg

                val errorType = when (response.code()) {
                    401 -> ApiErrorType.NOT_AUTHENTICATED
                    403 -> ApiErrorType.ACCESS_DENIED
                    404 -> ApiErrorType.PAGE_NOT_FOUND
                    422 -> ApiErrorType.INVALID_CREDENTIALS
                    500, 503 -> ApiErrorType.SERVER_ERROR
                    504 -> ApiErrorType.SERVER_NOT_RESPONDING
                    else -> ApiErrorType.UNKNOWN_ERROR
                }

                ApiErrorResponse(errorMsg, errorType)
            }
        }
    }
}

/**
 * separate class for HTTP 204 resposes so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(
    val body: T,
    val links: Map<String, String>
) : ApiResponse<T>() {
    constructor(body: T, linkHeader: String?) : this(
        body = body,
        links = linkHeader?.extractLinks() ?: emptyMap()
    )


    companion object {
        private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
        private const val NEXT_LINK = "next"

        private fun String.extractLinks(): Map<String, String> {
            val links = mutableMapOf<String, String>()
            val matcher = LINK_PATTERN.matcher(this)

            while (matcher.find()) {
                val count = matcher.groupCount()
                if (count == 2) {
                    links[matcher.group(2)] = matcher.group(1)
                }
            }
            return links
        }
    }
}

data class ApiErrorResponse<T>(val errorMessage: String, val errorType: ApiErrorType) :
    ApiResponse<T>()

enum class ApiErrorType {
    NOT_AUTHENTICATED,
    ACCESS_DENIED,
    PAGE_NOT_FOUND,
    SERVER_ERROR,
    INVALID_CREDENTIALS,
    SERVER_NOT_RESPONDING,
    TIMEOUT, UNKNOWN_HOST,
    UNKNOWN_ERROR
}