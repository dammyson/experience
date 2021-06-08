package com.agrosurvey.survey.data.question

import androidx.room.Embedded
import androidx.room.Relation
import com.agrosurvey.survey.data.Response

class QuestionAndResponse(
        @Embedded val question: QuestionAndType ?,
        @Embedded var response: Response ?
)