package com.agrosurvey.survey.data.question.option

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import java.time.OffsetDateTime

@JsonClass(generateAdapter = true)
class OptionIn (
    @SerializedName("id") val id : Int? = null,
    @SerializedName("text") val text : String? = null,
)