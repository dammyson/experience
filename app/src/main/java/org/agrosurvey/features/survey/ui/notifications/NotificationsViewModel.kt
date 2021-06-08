package org.agrosurvey.features.survey.ui.notifications

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.agrosurvey.PreferenceHelper
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    val preferences: PreferenceHelper
) : ViewModel()