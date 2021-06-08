package com.agrosurvey.survey.model.db.converters

import androidx.room.TypeConverter
import com.agrosurvey.survey.data.mappers.toSkipLogic
import com.agrosurvey.survey.data.mappers.toStringFormat
import com.agrosurvey.survey.data.question.skips.SkipLogic

class SkipLogicConverter {
    @TypeConverter
    fun toSkipLogicType(value: String): SkipLogic {
        return value.toSkipLogic()
    }

    @TypeConverter
    fun fromSkipLogic(value: SkipLogic): String? {
        return  value.toStringFormat()
    }
}