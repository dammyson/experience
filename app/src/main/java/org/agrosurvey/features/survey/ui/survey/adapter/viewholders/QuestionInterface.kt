package org.agrosurvey.features.survey.ui.survey.adapter.viewholders

import com.agrosurvey.survey.data.question.QuestionAndType

interface QuestionInterface {
    fun onChange(
        question: QuestionAndType,
        response: Any?
    )
}