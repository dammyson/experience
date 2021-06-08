package com.agrosurvey.survey.data.question.feedback

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.agrosurvey.survey.data.question.type.QuestionType
import com.agrosurvey.survey.data.survey.Survey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FeedbackAndSurvey(
    @Embedded val feedback : Feedback,
    @Relation(
        parentColumn = "surveyId",
        entityColumn = "id"
    )
    val survey: Survey
): Serializable