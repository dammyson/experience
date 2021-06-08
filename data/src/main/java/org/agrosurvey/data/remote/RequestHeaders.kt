package org.agrosurvey.data.remote

class RequestHeaders(
    private val language: String = "fr_FR",
    private val authorization: String
) {

    fun map(): HashMap<String, String> {
        return hashMapOf(
            "Accept" to "",
            "Accept-Language" to language,
            "Authorization" to authorization,
            "Content-Type" to "application/json"
        )
    }

}


