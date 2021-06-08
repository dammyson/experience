package com.agrosurvey.survey.data.question.skips

import com.agrosurvey.survey.data.question.FieldType
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SkipLogicIn (
    @SerializedName("subject_question") val subject_question : QuestionSkipIn? = null,
    @SerializedName("logic") val logic : LogicSkipIn? = null,
    @SerializedName("option") val option : OptionSkipIn? = null,
    @SerializedName("value") val value : String? = null
)

@JsonClass(generateAdapter = true)
class LogicSkipIn(
    @SerializedName("slug") var slug : String? = null,
    @SerializedName("logic") var logic : String? = null
)

@JsonClass(generateAdapter = true)
class QuestionSkipIn(
    @SerializedName("id") var id : Long? = null,
    @SerializedName("question_type_slug") var question_type_slug : FieldType? = null
)

@JsonClass(generateAdapter = true)
class OptionSkipIn(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("text") var text : String? = null
)