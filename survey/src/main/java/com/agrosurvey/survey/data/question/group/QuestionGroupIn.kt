package com.agrosurvey.survey.data.question.group

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import java.time.OffsetDateTime

@JsonClass(generateAdapter = true)
class QuestionGroupIn (
    @SerializedName("id") val id : Int? = null,
    @SerializedName("name") val name : String? = null,
    @SerializedName("gist") val gist : String? = null
)