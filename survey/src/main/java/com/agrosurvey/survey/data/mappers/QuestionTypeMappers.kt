package com.agrosurvey.survey.data.mappers

import com.agrosurvey.survey.data.question.group.QuestionGroup
import com.agrosurvey.survey.data.question.group.QuestionGroupIn
import com.agrosurvey.survey.data.question.type.QuestionType
import com.agrosurvey.survey.data.question.type.QuestionTypeIn

fun QuestionTypeIn.toQuestionTypeDb(): QuestionType {
    return QuestionType(
        type_id = this.id,
        name = this.name,
        gist = this.gist,
        is_allowed_in_table_question = this.is_allowed_in_table_question,
        slug = this.slug,
        questionTypeGroupId = this.question_type_group?.id
    )
}


fun QuestionGroupIn?.toQuestionGroupDb(): QuestionGroup {

    return QuestionGroup(
        id = this?.id,
        name = this?.name,
        gist = this?.gist
    )
}