package com.agrosurvey.survey.model.db.converters

import androidx.room.TypeConverter
import com.agrosurvey.survey.Constants
import com.agrosurvey.survey.data.mappers.toOffsetDateTimeString
import com.agrosurvey.survey.data.mappers.toOffsetDateTime
import java.time.OffsetDateTime

class OffsetDateTimeConverter {

    @TypeConverter
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return it.toOffsetDateTime(format = Constants.DATETIME_FORMAT)
        }
    }

    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.toOffsetDateTimeString()
    }
}