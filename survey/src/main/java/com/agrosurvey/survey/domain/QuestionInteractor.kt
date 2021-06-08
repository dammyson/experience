package com.agrosurvey.survey.domain


import com.agrosurvey.survey.data.mappers.toQuestionGroupDb
import com.agrosurvey.survey.data.mappers.toQuestionTypeDb
import com.agrosurvey.survey.data.question.type.QuestionType
import com.agrosurvey.survey.data.question.type.QuestionTypeError
import com.agrosurvey.survey.data.question.type.QuestionTypeIn
import com.agrosurvey.survey.data.remote.NetworkResponse
import com.agrosurvey.survey.di.Scopes
import com.agrosurvey.survey.exception.NotConnectedException
import com.agrosurvey.survey.model.db.QuestionDao
import com.agrosurvey.survey.model.repository.QuestionRepository
import com.agrosurvey.survey.model.system.NetworkHelper
import toothpick.Toothpick
import javax.inject.Inject

class QuestionInteractor {



    //Surveys
    @Inject
    lateinit var repository: QuestionRepository

    @Inject
    lateinit var networkHelper: NetworkHelper

    //QUESTIONS
    @Inject
    lateinit var questionDao: QuestionDao

    init {
        Toothpick.inject(this, Toothpick.openScopes(Scopes.APP_SCOPE, Scopes.NETWORK_SCOPE))
    }

    suspend fun insertOrUpdateQuestionType(items: List<QuestionTypeIn>): List<Long> {
        return questionDao.insertQuestionType(items.map {it.toQuestionTypeDb()})
    }

    suspend fun insertOrUpdateQuestionTypeGroup(items: List<QuestionTypeIn>): List<Long> {
        return questionDao.insertQuestionGroupType(items.map {it.question_type_group.toQuestionGroupDb()})
    }


    suspend fun getQuestionTypeFromNetwork(page: Int = 1, pageSize: Int = 20): NetworkResponse<List<QuestionTypeIn>, QuestionTypeError> {
        if(networkHelper.isConnected().not()){
            throw NotConnectedException()
        }
        return repository.getSurveyQuestions()
    }




}