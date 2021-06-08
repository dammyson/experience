package org.agrosurvey.data.remote.surveys

import com.agrosurvey.data.BuildConfig.API_BASE_URL
import okhttp3.OkHttpClient
import org.agrosurvey.data.remote.ApiEndpoints.questionTypesEndpoint
import org.agrosurvey.data.remote.ApiEndpoints.surveyListEndpoint
import org.agrosurvey.data.remote.ApiEndpoints.surveyQuestionsEndpoint
import org.agrosurvey.data.remote.ApiRequestMaker
import org.agrosurvey.data.remote.RequestHeaders
import org.agrosurvey.domain.entities.get.Question
import org.agrosurvey.domain.entities.get.QuestionType
import org.agrosurvey.domain.entities.get.Survey
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class SurveyService @Inject constructor() {
    private lateinit var requestMaker: ApiRequestMaker


    fun surveys(headers: RequestHeaders): Call<List<Survey>> {
        return api.getSurveys(
            headers.map(),
            endpoint = surveyListEndpoint()
        )
    }


    fun surveyQuestions(headers: RequestHeaders, surveyId: String): Call<List<Question>> {
        return api.getSurveyQuestions(headers.map(), surveyQuestionsEndpoint(surveyId))
    }

    fun supportedQuestionTypes(headers: RequestHeaders): Call<List<QuestionType>> {
        return api.supportedQuestions(headers.map(), questionTypesEndpoint())
    }

    private val api: SurveyApi by lazy {
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
            .create(SurveyApi::class.java)
    }
}