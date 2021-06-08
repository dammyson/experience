package com.agrosurvey.survey.model.db.converters

import androidx.room.TypeConverter
import com.agrosurvey.survey.data.LatLng
import com.agrosurvey.survey.data.question.FieldType
import com.agrosurvey.survey.data.mappers.toFieldType
import com.agrosurvey.survey.data.mappers.toLatLng
import com.agrosurvey.survey.data.mappers.toStringFormat

class LatLngConverter {
    @TypeConverter
    fun toFieldType(value: String?): LatLng? {
        return value?.toLatLng()
    }

    @TypeConverter
    fun fromTextField(value: LatLng?): String? {
        return  value?.toStringFormat()
    }
}