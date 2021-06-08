package org.agrosurvey.features.survey.ui.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agrosurvey.survey.Survey
import com.agrosurvey.survey.data.LatLng

class FeedbacksVMFactory (val single_survey : Survey,
                          val deviceID: String,
                          val startLocation: LatLng
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FeedbacksViewModel(
            single_survey = single_survey,
            deviceID = deviceID,
            startLocation = startLocation
        ) as T
    }
}