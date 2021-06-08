package org.agrosurvey.features.survey.ui.surveys.adapter

import android.content.Intent
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.agrosurvey.survey.data.mappers.toOffsetDateTimeString
import com.agrosurvey.survey.data.survey.Survey
import org.agrosurvey.Constants
import org.agrosurvey.Navigator
import org.agrosurvey.features.survey.ui.feedback.FeedbackActivity

class SurveyViewModel: BaseObservable() {
    lateinit var navigator: Navigator<Survey>

    @get:Bindable
    var surveyTitle: String = ""

    @get:Bindable
    var surveyUpdateDate: String = ""


    fun bind(item: Survey) {
        surveyTitle = item.title?.toUpperCase().toString()
        surveyUpdateDate = item.updated_at?.
                        toOffsetDateTimeString(format = Constants.SURVEY_RICH_DATETIME_PATTERN).toString()

    }


    fun itemClicked(item: Survey){
        navigator.itemClicked(item)
    }
}

