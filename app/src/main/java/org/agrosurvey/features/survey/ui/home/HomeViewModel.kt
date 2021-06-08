package org.agrosurvey.features.survey.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.agrosurvey.PreferenceHelper
import org.agrosurvey.data.remote.ApiErrorType
import org.agrosurvey.domain.entities.get.Survey
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val preferences: PreferenceHelper
) : ViewModel() {
    val isLoading = MutableLiveData(false)
    val onRequestFailed = MutableLiveData<Pair<String, ApiErrorType>>()
    val onRequestSuccessfulSurveys = MutableLiveData<List<Survey>>()


    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
}