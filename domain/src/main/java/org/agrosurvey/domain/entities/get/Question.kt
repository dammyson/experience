package org.agrosurvey.domain.entities.get

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

data class Question(

    @field:SerializedName("question_localization")
    val questionLocalization: String? = null,

    @field:SerializedName("display_question")
    val displayQuestion: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("is_mandatory")
    val isMandatory: Int? = null,

    @field:SerializedName("survey_section_id")
    val surveySectionId: Any? = null,

    @field:SerializedName("label")
    val label: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: Any? = null,

    @field:SerializedName("question_type_id")
    val questionTypeId: Int? = null,

    @field:SerializedName("variable_label")
    val variableLabel: String? = null,

    @field:SerializedName("is_language")
    val isLanguage: Int? = null,

    @field:SerializedName("survey_id")
    val surveyId: String? = null,

    @field:SerializedName("sequence")
    val sequence: Int? = null,

    @field:SerializedName("full_title")
    val fullTitle: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("audio_file")
    val audioFile: Any? = null,

    @field:SerializedName("parent_question_id")
    val parentQuestionId: Any? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("decimal_place")
    val decimalPlace: Any? = null,

    @field:SerializedName("map_accuracy")
    val mapAccuracy: Any? = null,

    @field:SerializedName("options")
    val options: List<QuestionOption?>? = null

) {
    // todo unit test
    fun getOptionsText(): List<String> = options.orEmpty().map { it?.text.toString() }

    override fun toString(): String {
        return GsonBuilder().setPrettyPrinting().create().toJson(this)
    }
}


data class QuestionOption(

    @field:SerializedName("sequence")
    val sequence: Int? = null,

    @field:SerializedName("is_other_option")
    val isOtherOption: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("audio_file")
    val audioFile: Any? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("text")
    val text: String? = null,

    @field:SerializedName("option_localization")
    val optionLocalization: String? = null,

    @field:SerializedName("question_id")
    val questionId: Int? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: Any? = null
)
