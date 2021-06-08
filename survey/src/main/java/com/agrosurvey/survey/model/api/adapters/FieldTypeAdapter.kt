package com.agrosurvey.survey.model.api.adapters

import com.agrosurvey.survey.data.question.FieldType
import com.agrosurvey.survey.data.mappers.toFieldType
import com.agrosurvey.survey.data.mappers.toStringFormat
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class FieldTypeAdapter {
    @FromJson
    fun fromJson(type: String): FieldType {
        return type.toFieldType()
    }

    @ToJson
    fun toJson(type: FieldType) = type.toStringFormat()
}