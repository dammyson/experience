package com.agrosurvey.survey.data.question

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.agrosurvey.survey.data.question.option.Option
import com.agrosurvey.survey.data.question.skips.SkipLogic

@Entity
class Question(
    @PrimaryKey
    var id: Int? = null,
    var survey_id: String? = null,
    var question_type_id: Int? = null,
    var sequence: Int? = null,
    var display_question: Int? = null,
    var parent_question_id: Int? = null,
    var is_mandatory: Int? = null,
    var is_language: Int? = null,
    var variable_label: String? = null,
    var decimal_place: String? = null,
    var map_accuracy: String? = null,
    var survey_section_id: String? = null,
    var full_title: String? = null,
    var label: String? = null,
    var audio_file: String? = null,
    var title: String? = null,
    var options: List<Option?> ? = null,
    var questionSkips: List<SkipLogic?> ? = null
)