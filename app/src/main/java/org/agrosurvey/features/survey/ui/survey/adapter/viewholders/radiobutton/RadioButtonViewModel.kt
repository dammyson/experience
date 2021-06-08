package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.radiobutton

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agrosurvey.survey.data.ResponseValue
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.agrosurvey.survey.data.question.option.Option

class RadioButtonViewModel:  ViewModel() {

    var questionTitle = MutableLiveData<String?>()
    var userResponse = MutableLiveData<Option?>()
    var isMandatory = MutableLiveData<Boolean?>()


    fun bind(item: QuestionAndResponse) {
        questionTitle.value = item.question?.question?.full_title.toString()
        isMandatory.value = item.question?.question?.is_mandatory != 0
        userResponse.value = (item.response?.value as ResponseValue.RadioButton?)?.value
    }


}