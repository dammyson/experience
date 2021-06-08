package com.agrosurvey.survey.model.db.converters

import androidx.room.TypeConverter
import com.agrosurvey.survey.data.mappers.toOption
import com.agrosurvey.survey.data.mappers.toResponseValue
import com.agrosurvey.survey.data.mappers.toStringFormat
import com.agrosurvey.survey.data.question.option.Option
import com.agrosurvey.survey.data.question.option.OptionIn
import org.json.JSONArray

class OptionConverter {
    @TypeConverter
    fun toOption(value: String?): Option? {
        return value.toOption()
    }

    @TypeConverter
    fun fromOption(value: Option?): String? {
        return value?.toStringFormat()
    }
}