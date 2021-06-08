package com.agrosurvey.survey

import java.time.format.DateTimeFormatter

object Constants{
    const val API_URL = "http://agp-api.agrosfer.org/agrosurvey/"

    val DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
    val TIME_FORMAT = "HH:mm"
    val DATE_FORMAT = "yyyy-MM-dd"

    val RICH_DATE_FORMAT = "EEEE, dd MMMM YYYY"


    val DATE_VALUE_FORMATER = DateTimeFormatter.ofPattern(DATE_FORMAT)
    val TIME_VALUE_FORMATER = DateTimeFormatter.ofPattern(TIME_FORMAT)
    val DATETIME_VALUE_FORMATER = DateTimeFormatter.ofPattern(DATETIME_FORMAT)


}


