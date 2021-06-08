package com.agrosurvey.survey.model.db.converters

import androidx.room.TypeConverter
import com.agrosurvey.survey.data.mappers.toOption
import com.agrosurvey.survey.data.mappers.toResponseValue
import com.agrosurvey.survey.data.mappers.toStringFormat
import com.agrosurvey.survey.data.question.option.Option
import com.agrosurvey.survey.data.question.option.OptionIn
import org.json.JSONArray

class OptionListConverter {
    @TypeConverter
    fun toOptions(value: String): List<Option?>? {

        val data = JSONArray(value)
        val list = ArrayList<Option?>()
        for (i  in 0 until data.length()){
            list.add(data[i].toString().toOption())
        }

        return list
    }

    @TypeConverter
    fun fromOptions(value: List<Option?>?): String? {

        return  "[ ${value?.map{it?.toStringFormat()}?.joinToString(",")} ]"
    }
}