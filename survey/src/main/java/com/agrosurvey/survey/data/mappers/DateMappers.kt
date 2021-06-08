package com.agrosurvey.survey.data.mappers

import com.agrosurvey.survey.Constants
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


fun String.toDate(format: String = Constants.DATE_FORMAT): Date? {
    return  SimpleDateFormat(format).parse(this)
}

fun Date.toDateString(format : String = Constants.DATE_FORMAT): String? {
    return  SimpleDateFormat(format).format(this)
}




