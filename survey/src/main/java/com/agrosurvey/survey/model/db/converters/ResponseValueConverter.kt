package com.agrosurvey.survey.model.db.converters

import androidx.room.TypeConverter
import com.agrosurvey.survey.data.ResponseValue
import com.agrosurvey.survey.data.mappers.toResponseValue
import com.agrosurvey.survey.data.mappers.toStringFormat

class ResponseValueConverter {
    @TypeConverter
    fun toFieldType(value: String): ResponseValue? {
        return value.toResponseValue()
    }

    @TypeConverter
    fun fromTextField(value: ResponseValue): String? {
        return  value.toStringFormat()
    }
}