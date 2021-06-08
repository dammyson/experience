package org.agrosurvey.data.remote.surveys

import org.agrosurvey.data.remote.ApiResponse
import org.agrosurvey.data.remote.RequestHeaders
import org.agrosurvey.domain.entities.get.Question
import org.agrosurvey.domain.entities.get.QuestionType
import org.agrosurvey.domain.entities.get.Survey
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SurveyRepository @Inject constructor(val service: SurveyService) {

    fun getSurveys(requestHeaders: RequestHeaders, onResult: (ApiResponse<List<Survey>>) -> Unit) {
        service.surveys(requestHeaders).enqueue(object : Callback<List<Survey>> {
            override fun onResponse(
                call: Call<List<Survey>>,
                response: Response<List<Survey>>
            ) {
                onResult(ApiResponse.create(response))
            }

            override fun onFailure(call: Call<List<Survey>>, t: Throwable) {
                onResult(ApiResponse.create(t))
            }

        })
    }

    fun getSurveyQuestions(
        requestHeaders: RequestHeaders,
        surveyId: String,
        onResult: (ApiResponse<List<Question>>) -> Unit
    ) {
        service.surveyQuestions(requestHeaders, surveyId)
            .enqueue(object : Callback<List<Question>> {
                override fun onResponse(
                    call: Call<List<Question>>,
                    response: Response<List<Question>>
                ) {
                    onResult(ApiResponse.create(response))
                }

                override fun onFailure(call: Call<List<Question>>, t: Throwable) {
                    onResult(ApiResponse.create(t))
                }

            })
    }

    fun getSupportedQuestionTypes(
        headers: RequestHeaders,
        onResult: (ApiResponse<List<QuestionType>>) -> Unit
    ) {
        service.supportedQuestionTypes(headers)
            .enqueue(object : Callback<List<QuestionType>> {
                override fun onResponse(
                    call: Call<List<QuestionType>>,
                    response: Response<List<QuestionType>>
                ) {
                    onResult(ApiResponse.create(response))
                }

                override fun onFailure(call: Call<List<QuestionType>>, t: Throwable) {
                    onResult(ApiResponse.create(t))
                }

            })
    }

}