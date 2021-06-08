package org.agrosurvey.data.remote.surveys

import org.agrosurvey.domain.entities.get.Question
import org.agrosurvey.domain.entities.get.QuestionType
import org.agrosurvey.domain.entities.get.Survey
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Url
import java.util.*


interface SurveyApi {
    @GET
    fun getSurveys(
        @HeaderMap headers: Map<String, String>, @Url endpoint: String
    ): Call<List<Survey>>

    @GET
    fun getSurveyQuestions(
        @HeaderMap headers: Map<String, String>, @Url endpoint: String
    ): Call<List<Question>>

    @GET
    fun supportedQuestions(
        @HeaderMap headers: HashMap<String, String>,
        @Url questionTypesEndpoint: String
    ): Call<List<QuestionType>>

}