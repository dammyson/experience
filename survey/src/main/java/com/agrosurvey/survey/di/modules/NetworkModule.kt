package com.agrosurvey.survey.di.modules


import com.agrosurvey.survey.di.providers.RestServiceProvider
import com.agrosurvey.survey.domain.FeedbackInteractor
import com.agrosurvey.survey.domain.QuestionInteractor
import com.agrosurvey.survey.domain.SurveyInteractor
import com.agrosurvey.survey.model.api.QuestionService
import com.agrosurvey.survey.model.api.SurveyService
import com.agrosurvey.survey.model.repository.QuestionRepository
import com.agrosurvey.survey.model.repository.SurveyRepository
import toothpick.config.Module

class  NetworkModule(baseUrl : String) : Module() {
    init {

        //Survey
        bind(SurveyService::class.java).toInstance(RestServiceProvider(baseUrl).get().create(SurveyService::class.java))
        bind(SurveyRepository::class.java)
        bind(SurveyInteractor::class.java)
        bind(FeedbackInteractor::class.java)

        //Questions type
        bind(QuestionService::class.java).toInstance(RestServiceProvider(baseUrl).get().create(QuestionService::class.java))
        bind(QuestionRepository::class.java)
        bind(QuestionInteractor::class.java)




    }
}