package com.agrosurvey.survey.data.question.type


import com.agrosurvey.survey.data.question.FieldType
import com.agrosurvey.survey.data.question.group.QuestionGroupIn
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class QuestionTypeIn (
    @SerializedName("id") val id : Int? = null,
    @SerializedName("name") val name : String? = null,
    @SerializedName("gist") val gist : String? = null,
    @SerializedName("is_allowed_in_table_question") val is_allowed_in_table_question : Int? = null,
    @SerializedName("slug") val slug : FieldType? = null,
    @SerializedName("question_type_group") val question_type_group : QuestionGroupIn? = null
)