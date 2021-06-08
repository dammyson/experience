package org.agrosurvey.domain.entities.get

import com.google.gson.annotations.SerializedName

data class QuestionType(

    @field:SerializedName("is_allowed_in_table_question")
    val isAllowedInTableQuestion: Int? = null,

    @field:SerializedName("gist")
    val gist: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("question_type_group")
    val questionTypeGroup: QuestionTypeGroup? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("slug")
    val slug: String? = null
)

data class QuestionTypeGroup(

    @field:SerializedName("gist")
    val gist: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)
