package com.agrosurvey.survey.data.mappers

import com.agrosurvey.survey.Constants
import com.agrosurvey.survey.data.LatLng
import com.agrosurvey.survey.data.question.option.Option
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


fun String.toLatLng(): LatLng? {
    return try{

        val jsonObject = JSONObject(this)
        val longitude =  jsonObject.getDouble("longitude")
        val latitude = jsonObject.getDouble("latitude")

        LatLng(longitude = longitude, latitute = latitude)

    }catch(e: JSONException){
        null
    }
}

fun LatLng.toStringFormat(): String? {
    return  "{\"longitude\" : ${this.longitude}," +
            " \"latitude\" : ${this.latitute}}"
}

fun List<LatLng>.toLatLngsString(): String {
    return  "[ ${this?.map{it?.toStringFormat()}?.joinToString(",")} ]"
}

fun String.toLatLngs(): List<LatLng>? {

    val data = JSONArray(this)
    val list = ArrayList<LatLng>()
    for (i  in 0 until data.length()){
        list.add(data[i].toString().toLatLng()!!)
    }

    return list
}



