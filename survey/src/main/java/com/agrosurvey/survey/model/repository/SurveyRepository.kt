package com.agrosurvey.survey.model.repository

import com.agrosurvey.survey.data.question.QuestionError
import com.agrosurvey.survey.data.question.QuestionIn
import com.agrosurvey.survey.data.question.feedback.FeedbackPayload
import com.agrosurvey.survey.data.question.feedback.FeedbackPayloadError
import com.agrosurvey.survey.data.remote.NetworkResponse
import com.agrosurvey.survey.data.survey.SurveyError
import com.agrosurvey.survey.data.survey.SurveyIn
import com.agrosurvey.survey.di.Scopes
import com.agrosurvey.survey.exception.NotConnectedException
import com.agrosurvey.survey.model.api.SurveyService
import com.agrosurvey.survey.model.system.NetworkHelper
import toothpick.Toothpick
import javax.inject.Inject

class SurveyRepository {

    @Inject
    lateinit var apiService: SurveyService

    @Inject
    lateinit var networkHelper: NetworkHelper

    init {
        Toothpick.inject(this, Toothpick.openScopes(Scopes.NETWORK_SCOPE))
    }


    suspend fun getSurveys(): NetworkResponse<List<SurveyIn>, SurveyError> {
        if (!networkHelper.isConnected()) {
            throw NotConnectedException()
        }
        return apiService.getSurveys()
    }

    suspend fun getSurveyQuestions(surveyId : String): NetworkResponse<List<QuestionIn>, QuestionError> {
        if (!networkHelper.isConnected()) {
            throw NotConnectedException()
        }
        return apiService.getSurveyQuestions(surveyId)
    }

    suspend fun getFeedBackForSurvey(id: String): NetworkResponse<FeedbackPayload, FeedbackPayloadError> {
        if (!networkHelper.isConnected()) {
            throw NotConnectedException()
        }
        return apiService.getFeedBackForSurvey(id)
    }
}