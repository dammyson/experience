package org.agrosurvey.features.survey.ui.surveys.viewitems

import com.google.gson.Gson
import org.agrosurvey.data.Logs
import org.agrosurvey.domain.entities.get.Survey
import org.joda.time.DateTime
import java.util.*

data class SurveyViewItem(
    val id: String,
    val title: String,
    val uploaded_at: String,
    val completion_status: String
) {
    fun getUpdateTime(): String {
        val dt = DateTime(uploaded_at).toLocalDate()
        val date =
            "${dt.dayOfMonth} ${dt.monthOfYear().asShortText.toLowerCase(Locale.ROOT)} ${dt.year}"
        Logs.logCat("Date time $date")
        return date
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}


fun Survey.asViewItem(): SurveyViewItem {
    return SurveyViewItem(
        id = id.orEmpty(),
        title = title.orEmpty(),
        uploaded_at = updatedAt.orEmpty(),
        completion_status = ""
    )
}

fun List<Survey>.asViewItems() = map { it.asViewItem() }