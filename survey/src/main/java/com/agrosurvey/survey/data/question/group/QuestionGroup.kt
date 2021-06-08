package com.agrosurvey.survey.data.question.group

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class QuestionGroup(
    @PrimaryKey
    var id: Int? = null,
    var name: String? = null,
    var gist: String? = null
)