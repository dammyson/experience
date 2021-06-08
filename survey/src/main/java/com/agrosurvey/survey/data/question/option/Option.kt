package com.agrosurvey.survey.data.question.option

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Option (
    var id : Int? = null,
    var text : String? = null,
)