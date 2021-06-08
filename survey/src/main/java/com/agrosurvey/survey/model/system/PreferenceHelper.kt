package com.agrosurvey.survey.model.system

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import javax.inject.Inject


class PreferenceHelper @Inject constructor(context: Context) {

    companion object {
        private const val SP_NAME = "com.agrosurvey.survey"
        private const val FIRST_TIME_LAUNCH = SP_NAME + "first_time_launch"
        private const val AUTH_STATUS_KEY = SP_NAME + "auth_status"
        private const val ACCESS_TOKEN_KEY = SP_NAME + "access_token"

    }


    private val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun firstTimeLaunch(): Boolean = sp.getBoolean(FIRST_TIME_LAUNCH,true)

    fun setFirstTimeLaunch(value: Boolean) = sp.edit().putBoolean(FIRST_TIME_LAUNCH, value).apply()

    fun getAccessToken(): String = sp.getString(ACCESS_TOKEN_KEY, "EMPTY_ACCESS_TOKEN").toString()

    fun setAccessToken(token: String) {
        sp.edit().putString(ACCESS_TOKEN_KEY, token).apply()
    }

    fun setAuthStatus(status: Boolean) {
        sp.edit().putBoolean(AUTH_STATUS_KEY, status).apply()
    }

    fun isAuthenticated(): Boolean = sp.getBoolean(AUTH_STATUS_KEY, false)
}