package com.agrosurvey.survey.di.modules

import android.content.Context
import com.agrosurvey.survey.di.providers.DatabaseProvider
import com.agrosurvey.survey.model.AppDataBase
import com.agrosurvey.survey.model.db.FeedBackDao
import com.agrosurvey.survey.model.db.QuestionDao
import com.agrosurvey.survey.model.db.SurveyDao
import com.agrosurvey.survey.model.system.NetworkHelper
import com.agrosurvey.survey.model.system.NetworkHelperImpl
import com.agrosurvey.survey.model.system.PreferenceHelper
import toothpick.config.Module

class AppModule(context: Context) : Module(){

    init {

        bind(Context::class.java).toInstance(context)

        bind(PreferenceHelper::class.java).singleton()

        bind(AppDataBase::class.java).toProviderInstance(DatabaseProvider(context))
            .providesSingleton()

        //Survey
        bind(SurveyDao::class.java).toInstance(DatabaseProvider(context = context).get().surveyDao())
        bind(QuestionDao::class.java).toInstance(DatabaseProvider(context = context).get().questionDao())
        bind(FeedBackDao::class.java).toInstance(DatabaseProvider(context = context).get().feedBackDao())

        //Helper
        bind(NetworkHelper::class.java).to(NetworkHelperImpl::class.java)
    }
}