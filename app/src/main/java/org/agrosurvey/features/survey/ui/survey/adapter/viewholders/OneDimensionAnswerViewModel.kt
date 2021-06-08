package org.agrosurvey.features.survey.ui.survey.adapter.viewholders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agrosurvey.survey.data.ResponseValue
import com.agrosurvey.survey.data.question.QuestionAndResponse

class OneDimensionAnswerViewModel:  ViewModel() {

    var questionTitle = MutableLiveData<String>()
    var userResponse = MutableLiveData<String>()

    fun bind(item: QuestionAndResponse) {
        questionTitle.value = item.question?.question?.full_title.toString()
        //userResponse.value = (item.response?.value as ResponseValue.RadioButton?)?.value.toString()
    }


}