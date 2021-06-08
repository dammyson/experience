package com.agrosurvey.survey.model.interceptors

import android.util.Log
import com.agrosurvey.survey.di.Scopes
import com.agrosurvey.survey.model.system.PreferenceHelper
import okhttp3.Interceptor
import okhttp3.Response
import toothpick.Toothpick
import javax.inject.Inject

class AuthInterceptor : Interceptor {

    init {
        Toothpick.inject(this, Toothpick.openScopes(Scopes.APP_SCOPE, Scopes.NETWORK_SCOPE))
    }

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    override fun intercept(chain: Interceptor.Chain): Response {

        Log.e("INTERCEPT ", preferenceHelper.getAccessToken() )
        return  if (preferenceHelper.isAuthenticated()) {
            chain.proceed(chain.request().newBuilder()
                .addHeader("Authorization","Bearer "+ preferenceHelper.getAccessToken())
                .build())
        } else {
            chain.proceed(chain.request())
        }

    }
}