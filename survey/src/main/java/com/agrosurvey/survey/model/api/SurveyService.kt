package com.agrosurvey.survey.model.api

import com.agrosurvey.survey.data.question.QuestionError
import com.agrosurvey.survey.data.question.QuestionIn
import com.agrosurvey.survey.data.question.feedback.FeedbackPayload
import com.agrosurvey.survey.data.question.feedback.FeedbackPayloadError
import com.agrosurvey.survey.data.remote.NetworkResponse
import com.agrosurvey.survey.data.survey.SurveyError
import com.agrosurvey.survey.data.survey.SurveyIn
import retrofit2.Response
import retrofit2.http.*

interface SurveyService {
    @GET("surveys")
    suspend fun getSurveys() : NetworkResponse<List<SurveyIn>, SurveyError>

    @GET("surveys/{survey_id}/questions")
    suspend fun getSurveyQuestions(@Path("survey_id") surveyId: String) : NetworkResponse<List<QuestionIn>, QuestionError>

    @GET("surveys/{survey_id}/feedbacks")
    suspend fun getFeedBackForSurvey(@Path("survey_id") survey_id: String) : NetworkResponse<FeedbackPayload, FeedbackPayloadError>
}