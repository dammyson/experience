package org.agrosurvey.domain.entities.get

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("email_verified")
    val email_verified_at: String,
    @SerializedName("phone_number")
    val phone_number: String,
    @SerializedName("phone_number_verified_at")
    val phone_number_verified_at: String,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("token")
    val token: String
)

fun User.getBearToken(): String {
    return "Bearer " + token.split("|")[1]
}

fun User.getBearerToken(): String {
    return token
}

fun isBearerTokenValid(token: String): Boolean {
    return token.run {
        startsWith("Bearer", ignoreCase = false) && indexOf(" ") == 6 &&
                length > 6
    }
}