package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.time

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agrosurvey.survey.Constants
import com.agrosurvey.survey.data.ResponseValue
import com.agrosurvey.survey.data.mappers.toTime
import com.agrosurvey.survey.data.mappers.toTimeString
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class TimeViewModel : ViewModel() {

    var questionTitle = MutableLiveData<String?>()
    var userResponse = MutableLiveData<String?>()
    var isMandatory = MutableLiveData<Boolean?>()

    var timeEditing = MutableLiveData<Boolean>(false)
    val time = MutableLiveData<Time?>(null)
    val timeLabel = MutableLiveData<String>("Choisir une heure")
    var calendar = Calendar.getInstance()


    val timeEditListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, second ->


        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)



        val providedTime = Time(calendar?.timeInMillis!!)

        time.value = providedTime
        timeLabel.value = providedTime.toTimeString()

    }

    fun bind(item: QuestionAndResponse) {
        questionTitle.value = item.question?.question?.full_title
        isMandatory.value = item.question?.question?.is_mandatory != 0
        (item.response?.value as ResponseValue.TimePicker?)?.time?.let {
            time.value = it
            timeLabel.value = it.toTimeString()
        }
    }

    fun setTimeEditing() {
        timeEditing.value = true
    }

}