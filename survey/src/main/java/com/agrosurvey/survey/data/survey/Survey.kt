package com.agrosurvey.survey.data.survey

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.OffsetDateTime

@Entity
class Survey(
    @PrimaryKey
    val id: String,
    val organisationId: String? = null,
    val sequence: String? = null,
    val survey_status_id: String? = null,
    val created_at : OffsetDateTime? = null,
    val updated_at : OffsetDateTime? = null,
    val deleted_at : OffsetDateTime? = null,
    val title: String? = null,
    val gist: String? = null,
) : Serializable