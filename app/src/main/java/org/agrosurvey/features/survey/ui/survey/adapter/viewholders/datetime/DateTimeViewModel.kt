package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.datetime

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agrosurvey.survey.Constants
import com.agrosurvey.survey.data.ResponseValue
import com.agrosurvey.survey.data.mappers.toDateString
import com.agrosurvey.survey.data.mappers.toTimeString
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

class DateTimeViewModel:  ViewModel() {

    var questionTitle = MutableLiveData<String?>()
    var userResponse = MutableLiveData<String?>()
    var isMandatory = MutableLiveData<Boolean?>()

    var dateEditing = MutableLiveData<Boolean>(false)
    val dateLabel = MutableLiveData<String>("Choisir une date")
    var dateEdited = false

    var timeEditing = MutableLiveData<Boolean>(false)
    val timeLabel = MutableLiveData<String>("Choisir une heure")
    var timeEdited = false

    var calendar = Calendar.getInstance()

    val response = MutableLiveData<OffsetDateTime>()

    val dateEditListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        dateEdited = true
        response.value =
            OffsetDateTime.of(
                year,
                monthOfYear,
                dayOfMonth,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND),
                0,
                ZoneOffset.UTC)

        dateLabel.value = calendar.time.toDateString(format = Constants.RICH_DATE_FORMAT)

    }

    val timeEditListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, second ->


        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)


        timeEdited = true
        response.value =
            OffsetDateTime.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                hourOfDay,
                minute,
                second,
                0,
                ZoneOffset.UTC)

        val providedTime = Time(calendar?.timeInMillis!!)
        timeLabel.value = providedTime.toTimeString()

    }



    fun bind(item: QuestionAndResponse) {
        questionTitle.value = item.question?.question?.full_title
        isMandatory.value = item.question?.question?.is_mandatory != 0
        (item.response?.value as ResponseValue.DateTimePicker?)?.dateTime?.let { dateTime ->
            response.value = dateTime
            timeLabel.value = Time((dateTime.toLocalTime().toSecondOfDay()*1000).toLong()).toTimeString()
            dateLabel.value = Date.from(dateTime.toLocalDate()
                .atStartOfDay(ZoneId.systemDefault()).toInstant())
                .toDateString(format = Constants.RICH_DATE_FORMAT)
        }
        //userResponse.value = .text?:""
    }

    fun setDateEditing() {
        dateEditing.value = true
    }

    fun setTimeEditing() {
        timeEditing.value = true
    }

    fun isFullyAnswered(): Boolean {
        return timeEdited && dateEdited
    }


}