package com.agrosurvey.survey.data.mappers

import com.agrosurvey.survey.Constants
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


fun String.toTime(hasNanoSecond: Boolean = true): Time? {
    return if(hasNanoSecond) Time.valueOf(this) else Time.valueOf("$this:00")
}

fun Time.toTimeString(): String? {
    return  this.toString()
}




