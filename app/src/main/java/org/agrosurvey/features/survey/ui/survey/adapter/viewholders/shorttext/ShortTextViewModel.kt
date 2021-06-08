package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.shorttext

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agrosurvey.survey.data.ResponseValue
import com.agrosurvey.survey.data.question.QuestionAndResponse

class ShortTextViewModel:  ViewModel() {

    var questionTitle = MutableLiveData<String?>()
    var userResponse = MutableLiveData<String?>()
    var isMandatory = MutableLiveData<Boolean?>()


    fun bind(item: QuestionAndResponse) {
        questionTitle.value = item.question?.question?.full_title
        isMandatory.value = item.question?.question?.is_mandatory != 0
        userResponse.value = (item.response?.value as ResponseValue.ShortText?)?.text
    }


}