package com.agrosurvey.survey.model.api

import com.agrosurvey.survey.data.question.type.QuestionTypeError
import com.agrosurvey.survey.data.question.type.QuestionTypeIn
import com.agrosurvey.survey.data.remote.NetworkResponse
import retrofit2.http.GET

interface QuestionService {
    @GET("question-types")
    suspend fun getQuestionTypes() : NetworkResponse<List<QuestionTypeIn>, QuestionTypeError>
}