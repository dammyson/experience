package com.agrosurvey.survey.model.repository

import com.agrosurvey.survey.data.question.QuestionError
import com.agrosurvey.survey.data.question.QuestionIn
import com.agrosurvey.survey.data.question.type.QuestionType
import com.agrosurvey.survey.data.question.type.QuestionTypeError
import com.agrosurvey.survey.data.question.type.QuestionTypeIn
import com.agrosurvey.survey.data.remote.NetworkResponse
import com.agrosurvey.survey.data.survey.SurveyError
import com.agrosurvey.survey.data.survey.SurveyIn
import com.agrosurvey.survey.di.Scopes
import com.agrosurvey.survey.exception.NotConnectedException
import com.agrosurvey.survey.model.api.QuestionService
import com.agrosurvey.survey.model.api.SurveyService
import com.agrosurvey.survey.model.system.NetworkHelper
import toothpick.Toothpick
import javax.inject.Inject

class QuestionRepository {

    @Inject
    lateinit var apiService: QuestionService

    @Inject
    lateinit var networkHelper: NetworkHelper

    init {
        Toothpick.inject(this, Toothpick.openScopes(Scopes.NETWORK_SCOPE))
    }


    suspend fun getSurveyQuestions(): NetworkResponse<List<QuestionTypeIn>, QuestionTypeError> {
        if (!networkHelper.isConnected()) {
            throw NotConnectedException()
        }
        return apiService.getQuestionTypes()
    }
}