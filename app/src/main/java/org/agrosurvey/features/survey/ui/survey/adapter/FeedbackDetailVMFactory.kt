package org.agrosurvey.features.survey.ui.survey.adapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agrosurvey.survey.FeedBack
import org.agrosurvey.features.survey.ui.survey.FeedbackDetailViewModel

class FeedbackDetailVMFactory (val feedback: FeedBack) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FeedbackDetailViewModel(
            feedback
        ) as T
    }
}