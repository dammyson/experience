package org.agrosurvey.data.remote

import org.agrosurvey.data.remote.ApiEndpoints.Endpoint.*

object ApiEndpoints {

    fun loginEndpoint(): String {
        return "/$login"
    }

    fun organisationsEndpoint(): String {
        return "/$organisations"
    }

    fun surveyListEndpoint(): String {
        return "/$agrosurvey/$surveys"
    }

    fun surveyQuestionsEndpoint(surveyId: String): String {
        return "/$agrosurvey/$surveys/$surveyId/$questions"
    }

    fun questionTypesEndpoint(): String {
        return "/$agrosurvey/${question_types.toString().replace("_", "-")}"
    }


    enum class Endpoint {
        // Endpoints are case sensitive.
        agrosurvey, login, organisations, surveys, questions, question_types
    }
}