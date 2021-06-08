package org.agrosurvey.features.survey.ui.feedback

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.agrosurvey.R
import com.agrosurvey.databinding.ActivityFeedbackBinding
import com.agrosurvey.survey.MySurvey
import com.agrosurvey.survey.Survey
import com.agrosurvey.survey.data.LatLng
import kotlinx.android.synthetic.main.activity_feedback.*
import org.agrosurvey.Constants
import org.agrosurvey.features.survey.ui.feedback.adapter.FeedbackAdapter
import org.agrosurvey.features.survey.ui.survey.SurveyActivity
import org.agrosurvey.features.survey.ui.survey.adapter.SingleShotLocationProvider


class FeedbackActivity : AppCompatActivity() {


    private lateinit var viewModel: FeedbacksViewModel
    lateinit var adapter: FeedbackAdapter
    lateinit var binding: ActivityFeedbackBinding
    private lateinit var linearLayoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        var single_survey =
            intent.getSerializableExtra("survey") as com.agrosurvey.survey.data.survey.Survey


        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback)


        val surveyDetail: Survey = Survey(single_survey)

        var startLatLng: LatLng
        SingleShotLocationProvider.requestSingleUpdate(
            this,
            object :
                SingleShotLocationProvider.LocationCallback {
                override fun onNewLocationAvailable(location: SingleShotLocationProvider.GPSCoordinates?) {

                    startLatLng = LatLng(
                        latitute = location?.latitude?.toDouble()!!,
                        longitude = location.longitude.toDouble()
                    )

                    viewModel = ViewModelProvider(this@FeedbackActivity,
                        FeedbacksVMFactory(
                            single_survey = surveyDetail,
                            startLocation = startLatLng,
                            deviceID = Settings.Secure.getString(
                                contentResolver,
                                Settings.Secure.ANDROID_ID)
                        ))
                        .get(
                            FeedbacksViewModel::class.java
                        )
                    binding.viewModel = viewModel
                    binding.lifecycleOwner = this@FeedbackActivity

                    binding.close.setOnClickListener {
                        finish()
                    }


                    initRecyclerView()
                    initObservables()


                }

            })




    }


    fun initRecyclerView() {
        adapter = FeedbackAdapter(this)
        list.layoutManager = GridLayoutManager(this, 3)
        list.adapter = adapter
    }

    fun initObservables() {
        viewModel.feedbackCollection.observe(this, Observer {
            val recyclerView = list
            adapter.submitList(it) {
                linearLayoutManager = GridLayoutManager(this, 3)
                recyclerView.layoutManager = linearLayoutManager
            }
        })

        viewModel.noFeedbackFound.observe(this, Observer { noRequestFound ->
            if (noRequestFound) {
                // binding.notFoundView.visibility = View.VISIBLE
            } else {
                // binding.notFoundView.visibility = View.GONE
            }
        })

        viewModel.messages.observe(this, Observer { messages ->
            when (messages.first) {
                FeedbacksViewModel.ERROR_CREATING_FEEDBACK -> Toast.makeText(
                    this,
                    messages.second,
                    Toast.LENGTH_LONG
                ).show()
                FeedbacksViewModel.FEEDBACK_CREATED -> Toast.makeText(
                    this,
                    messages.second,
                    Toast.LENGTH_LONG
                ).show()
            }

        })

        viewModel.newlyCreatedFeedback.observe(this, Observer { feedback ->
            if (feedback != null) {
                startActivity(
                    Intent(this, SurveyActivity::class.java)
                        .apply {
                            putExtra("feedback", feedback)
                        }
                )
            }

        })
    }
}