package com.agrosurvey.survey.model.api.adapters

import com.agrosurvey.survey.data.mappers.toOffsetDateTimeString
import com.agrosurvey.survey.data.mappers.toOffsetDateTime
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.OffsetDateTime

class OffsetDatetimeAdapter {
    @FromJson
    fun fromJson(type: String?): OffsetDateTime? {
        return type?.toOffsetDateTime()
    }

    @ToJson
    fun toJson(type: OffsetDateTime?) = type?.toOffsetDateTimeString()
}