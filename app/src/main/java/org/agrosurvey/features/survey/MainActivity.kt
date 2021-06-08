package org.agrosurvey.features.survey

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.agrosurvey.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import org.agrosurvey.features.survey.ui.home.HomeViewModel
import org.agrosurvey.features.survey.ui.notifications.NotificationsViewModel
import org.agrosurvey.features.survey.ui.surveys.SurveysViewModel
import org.agrosurvey.forms.FormsViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // Shared viewModels between all fragments attached to this activity
    private val homeViewModel by viewModels<HomeViewModel>()
    private val surveysViewModel by viewModels<SurveysViewModel>()
    private val notificationViewModel by viewModels<NotificationsViewModel>()
    private val formsViewModel by viewModels<FormsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, _, _ ->
            Handler(Looper.getMainLooper()).post {
                when (navController.currentDestination?.id) {
                    R.id.navigation_survey_form -> toolbar.isVisible = false
                    else -> toolbar.isVisible = true
                }
            }
        }

        //surveysViewModel.getSurveys()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.account, menu)
        return super.onCreateOptionsMenu(menu)
    }
}