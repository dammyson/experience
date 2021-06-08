package com.agrosurvey.survey.data.mappers

import com.agrosurvey.survey.Constants
import com.agrosurvey.survey.data.question.feedback.Feedback
import com.agrosurvey.survey.data.question.feedback.FeedbackIn
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


fun FeedbackIn.toDbFeedBack(surveyID: String): Feedback {
    return Feedback(
        surveyId = surveyID,
        createdOn = this.created_at
    )
}




