package org.agrosurvey

import android.annotation.SuppressLint
import android.util.Log
import androidx.multidex.MultiDexApplication
import com.agrosurvey.survey.MySurvey
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : MultiDexApplication() {
    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @SuppressLint("LogNotTimber")
    override fun onCreate() {
        super.onCreate()
        // An easier replacement for Log.x(TAG, message)
        Timber.plant(Timber.DebugTree())

        val token: String = preferenceHelper.getToken();
        if (!token.isNullOrEmpty()) {

        val mySurvey = MySurvey.Builder(this)
            .setUserToken(token)
            .setBaseUrl(Constants.API_URL)
            .build()

        GlobalScope.launch(CoroutineExceptionHandler { _, e ->Log.e("RETRIEVE",e.stackTraceToString())}) {
            mySurvey.fetchAndSaveSurveys()
            mySurvey.fetchQuestionsType()
        }
        }


    }
}
