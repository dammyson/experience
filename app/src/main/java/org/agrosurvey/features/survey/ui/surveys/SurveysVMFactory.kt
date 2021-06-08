package org.agrosurvey.features.survey.ui.surveys

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agrosurvey.survey.MySurvey


class SurveysVMFactory(val surveyLib: MySurvey) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SurveysViewModel(surveyLib) as T
    }
}