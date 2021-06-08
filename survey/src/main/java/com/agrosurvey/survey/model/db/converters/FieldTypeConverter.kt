package com.agrosurvey.survey.model.db.converters

import androidx.room.TypeConverter
import com.agrosurvey.survey.data.question.FieldType
import com.agrosurvey.survey.data.mappers.toFieldType
import com.agrosurvey.survey.data.mappers.toStringFormat

class FieldTypeConverter {
    @TypeConverter
    fun toFieldType(value: String): FieldType? {
        return value.toFieldType()
    }

    @TypeConverter
    fun fromTextField(value: FieldType): String? {
        return  value.toStringFormat()
    }
}