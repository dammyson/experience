package com.agrosurvey.survey.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(primaryKeys = ["questionId", "feedBackId"])
class Response (var questionId: Int,
                var feedBackId: Int,
                var value: ResponseValue? = null
                )