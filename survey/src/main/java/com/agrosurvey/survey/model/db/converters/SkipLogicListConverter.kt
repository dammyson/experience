package com.agrosurvey.survey.model.db.converters

import androidx.room.TypeConverter
import com.agrosurvey.survey.data.mappers.toSkipLogic
import com.agrosurvey.survey.data.mappers.toStringFormat
import com.agrosurvey.survey.data.question.skips.SkipLogic
import org.json.JSONArray

class SkipLogicListConverter {
    @TypeConverter
    fun toSkipLogicType(value: String): List<SkipLogic?>? {

        val data = JSONArray(value)
        val list = ArrayList<SkipLogic>()
        for (i  in 0 until data.length()){
            list.add(data[i].toString().toSkipLogic())
        }

        return list
    }

    @TypeConverter
    fun fromSkipLogic(value: List<SkipLogic?>?): String? {
        return  "[ ${value?.map{it?.toStringFormat()}?.joinToString(",")} ]"
    }
}