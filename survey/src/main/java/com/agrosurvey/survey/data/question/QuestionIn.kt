package com.agrosurvey.survey.data.question

import com.agrosurvey.survey.data.question.option.OptionIn
import com.agrosurvey.survey.data.question.skips.SkipLogicIn
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Field

@JsonClass(generateAdapter = true)
class QuestionIn (
    @SerializedName("id") val id : Int? = null,
    @SerializedName("survey_id") val survey_id : String? = null,
    @SerializedName("sequence") val sequence : Int? = null,
    @SerializedName("question_type_id") val question_type_id : Int? = null,
    @SerializedName("display_question") val display_question : Int? = null,
    @SerializedName("parent_question_id") val parent_question_id : Int? = null,
    @SerializedName("is_mandatory") val is_mandatory : Int? = null,
    @SerializedName("is_language") val is_language : Int? = null,
    @SerializedName("variable_label") val variable_label : String? = null,
    @SerializedName("decimal_place") val decimal_place : String? = null,
    @SerializedName("map_accuracy") val map_accuracy : String? = null,
    @SerializedName("survey_section_id") val survey_section_id : String? = null,
    @SerializedName("full_title") val full_title : String? = null,
    @SerializedName("label") val label : String? = null,
    @SerializedName("audio_file") val audio_file : String? = null,
    @SerializedName("title") val title : String? = null,
    @SerializedName("options") val options: List<OptionIn?>? = null,
    @SerializedName("question_skips") val question_skips: List<SkipLogicIn?>? = null
)