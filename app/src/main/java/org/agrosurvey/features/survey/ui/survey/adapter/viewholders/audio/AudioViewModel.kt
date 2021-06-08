package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.audio

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agrosurvey.survey.data.ResponseValue
import com.agrosurvey.survey.data.question.QuestionAndResponse

class AudioViewModel:  ViewModel() {

    var questionTitle = MutableLiveData<String?>()
    var userResponse = MutableLiveData<String?>()
    var isMandatory = MutableLiveData<Boolean?>()


    fun bind(item: QuestionAndResponse) {
        questionTitle.value = item.question?.question?.full_title.toString()
        isMandatory.value = item.question?.question?.is_mandatory != 0

    }


}