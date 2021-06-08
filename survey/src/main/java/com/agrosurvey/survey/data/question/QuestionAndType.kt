package com.agrosurvey.survey.data.question

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.agrosurvey.survey.data.question.type.QuestionType
import com.google.gson.annotations.SerializedName

class QuestionAndType(
    @Embedded val question : Question,
    @Relation(
        parentColumn = "question_type_id",
        entityColumn = "type_id"
    )
    val type: QuestionType
)