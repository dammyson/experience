package com.agrosurvey.survey.di.providers

import android.content.Context
import android.util.Log
import com.agrosurvey.survey.model.AppDataBase
import javax.inject.Provider

class DatabaseProvider(private val context: Context): Provider<AppDataBase> {
    override fun get(): AppDataBase {
        return AppDataBase.getInstance(context)
    }

}