package com.agrosurvey.survey.data.mappers

import com.agrosurvey.survey.Constants
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun String.toOffsetDateTime(format: String = Constants.DATETIME_FORMAT): OffsetDateTime {
    return  LocalDateTime.parse(this, DateTimeFormatter.ofPattern(format))
        .atZone(ZoneId.systemDefault())
        .toOffsetDateTime()
}

fun OffsetDateTime.toOffsetDateTimeString(format : DateTimeFormatter = Constants.DATETIME_VALUE_FORMATER): String? {
    return  this.toLocalDateTime()?.format(format)
}




