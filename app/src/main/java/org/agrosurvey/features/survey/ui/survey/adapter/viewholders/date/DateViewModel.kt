package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.date

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agrosurvey.survey.Constants
import com.agrosurvey.survey.data.ResponseValue
import com.agrosurvey.survey.data.mappers.toDateString
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.*

class DateViewModel:  ViewModel() {

    var questionTitle = MutableLiveData<String?>()
    var userResponse = MutableLiveData<Date?>()
    var isMandatory = MutableLiveData<Boolean?>()

    var dateEditing = MutableLiveData<Boolean>(false)
    val date = MutableLiveData<Date?>(null)
    val dateLabel = MutableLiveData<String>("Choisir une date")
    var calendar = Calendar.getInstance()

    val dateEditListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)


        date.value = calendar.time
        dateLabel.value = calendar.time.toDateString(format = Constants.RICH_DATE_FORMAT)


    }


    fun bind(item: QuestionAndResponse) {
        questionTitle.value = item.question?.question?.full_title
        isMandatory.value = item.question?.question?.is_mandatory != 0

        (item.response?.value as ResponseValue.DatePicker?)?.date?.let {
            date.value = it
            dateLabel.value = it.toDateString(format = Constants.RICH_DATE_FORMAT)
        }

    }

    fun setDateEditing() {
        dateEditing.value = true
    }




}