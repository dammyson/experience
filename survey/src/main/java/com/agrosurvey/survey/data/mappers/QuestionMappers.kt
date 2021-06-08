package com.agrosurvey.survey.data.mappers

import com.agrosurvey.survey.data.question.Question
import com.agrosurvey.survey.data.question.QuestionIn

fun QuestionIn.toQuestionDb(): Question {
    return Question(
        id = this.id,
        survey_id = this.survey_id,
        question_type_id = this.question_type_id,
        sequence = this.sequence,
        display_question = this.display_question,
        parent_question_id = this.parent_question_id,
        is_mandatory = this.is_mandatory,
        is_language = this.is_language,
        variable_label = this.variable_label,
        decimal_place = this.decimal_place,
        map_accuracy = this.map_accuracy,
        survey_section_id = this.survey_section_id,
        full_title = this.full_title,
        label = this.label,
        audio_file = this.audio_file,
        title = this.title,
        options = this.options?.map { it?.toOption() },
        questionSkips = this.question_skips?.map {it?.toSkipLogic()}
    )
}