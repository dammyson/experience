package org.agrosurvey

import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.agrosurvey.features.login.LoginActivity
import org.agrosurvey.features.survey.MainActivity
import org.agrosurvey.features.survey.ui.home.HomeViewModel
import org.agrosurvey.features.survey.ui.notifications.NotificationsViewModel
import org.agrosurvey.features.survey.ui.surveys.SurveysViewModel
import javax.inject.Inject

@AndroidEntryPoint
class Starter @Inject constructor() : AppCompatActivity() {

    private val homeViewModel by viewModels<HomeViewModel>()
    private val surveyViewModel by viewModels<SurveysViewModel>()
    private val notificationViewModel by viewModels<NotificationsViewModel>()

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    override fun onResume() {
        super.onResume()

        if (preferenceHelper.credentialsAreValid()) {
            goToMainScreen()
        } else {
            goToLoginScreen()
        }
    }

    private fun goToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun goToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}