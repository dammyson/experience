package com.agrosurvey.survey.data.mappers

import com.agrosurvey.survey.data.question.option.Option
import com.agrosurvey.survey.data.question.option.OptionIn
import org.json.JSONException
import org.json.JSONObject


fun Option.toStringFormat(): String? {
    return "{\"id\" : ${this.id}," +
           " \"text\" : \"${this.text}\"}"
}

fun String?.toOption(): Option? {

    return try{

        val jsonObject = JSONObject(this)
        val id =  jsonObject.getInt("id")
        val text = jsonObject.getString("text")

        Option(id, text)

    }catch(e: JSONException){
        null
    }

}

fun OptionIn.toOption(): Option {
    return Option(id = this.id, text = this.text)
}