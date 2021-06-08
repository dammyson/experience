package org.agrosurvey

import java.time.format.DateTimeFormatter

object Constants{
    const val API_URL = "http://agp-api.agrosfer.org/agrosurvey/"
    val SURVEY_RICH_DATETIME_PATTERN = DateTimeFormatter.ofPattern("dd MMMM yyyy à HH:mm:ss")
    val FEED_RICH_DATETIME_PATTERN = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val FEED_RICH_TIME_PATTERN = DateTimeFormatter.ofPattern("HH:mm")

}


