package com.agrosurvey.survey.data.survey

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import java.time.OffsetDateTime

@JsonClass(generateAdapter = true)
class SurveyIn (
    @SerializedName("id") val id : String? = null,
    @SerializedName("organisation_id") val organisationId : String? = null,
    @SerializedName("sequence") val sequence : String? = null,
    @SerializedName("survey_status_id") val survey_status_id : String? = null,
    @SerializedName("created_at") val created_at : OffsetDateTime? = null,
    @SerializedName("updated_at") val updated_at : OffsetDateTime? = null,
    @SerializedName("deleted_at") val deleted_at : OffsetDateTime? = null,
    @SerializedName("title") val title : String? = null,
    @SerializedName("gist") val gist : String? = null,
)