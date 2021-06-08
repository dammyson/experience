package org.agrosurvey.features.survey.ui.feedback.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.agrosurvey.survey.data.mappers.toOffsetDateTimeString
import com.agrosurvey.survey.data.question.feedback.Feedback
import com.agrosurvey.survey.data.question.feedback.FeedbackAndSurvey
import org.agrosurvey.Constants
import org.agrosurvey.Navigator
import java.time.format.DateTimeFormatter
import java.util.*
import java.time.LocalDate

class FeedbackViewModel:  BaseObservable() {
    lateinit var navigator: Navigator<FeedbackAndSurvey>

    @get:Bindable
    var feedbackCreatedOn: String = ""

    @get:Bindable
    var feedbackCreatedTime: String = ""

    @get:Bindable
    var feedbackUpdatedOn: String = ""


    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(item: FeedbackAndSurvey) {

        feedbackCreatedOn = item.feedback.createdOn?.
                            toOffsetDateTimeString(format = Constants.FEED_RICH_DATETIME_PATTERN).toString()
        feedbackCreatedTime = item.feedback.createdOn?.
                            toOffsetDateTimeString(format = Constants.FEED_RICH_TIME_PATTERN).toString()
    }

    fun itemClicked(item: FeedbackAndSurvey){
        navigator.itemClicked(item)
    }
}