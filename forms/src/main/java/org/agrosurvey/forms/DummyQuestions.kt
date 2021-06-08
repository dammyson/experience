package org.agrosurvey.forms

import android.view.View
import org.agrosurvey.domain.entities.get.Question
import org.agrosurvey.domain.entities.get.QuestionOption

object DummyQuestions {
    fun addDummyCheckBoxQuestion(): Question {
        val options = listOf<QuestionOption>(
            QuestionOption(sequence = 0, text = "Material", id = View.generateViewId()),
            QuestionOption(sequence = 1, text = "Design", id = View.generateViewId()),
            QuestionOption(sequence = 2, text = "Components", id = View.generateViewId()),
            QuestionOption(sequence = 3, text = "Android", id = View.generateViewId())
        )

        return Question(
            questionTypeId = 1,
            fullTitle = "Dummy Checkbox question",
            options = options
        )
    }

    fun addDummySelectBoxQuestion(): Question {
        val options = listOf<QuestionOption>(
            QuestionOption(sequence = 0, text = "Material", id = View.generateViewId()),
            QuestionOption(sequence = 1, text = "Design", id = View.generateViewId()),
            QuestionOption(sequence = 2, text = "Components", id = View.generateViewId()),
            QuestionOption(sequence = 3, text = "Android", id = View.generateViewId())
        )
        return Question(
            questionTypeId = 3,
            fullTitle = "Dummy Select Box question",
            options = options
        )
    }

    fun addDummyRadioBoxQuestion(): Question {
        val options = listOf<QuestionOption>(
            QuestionOption(sequence = 0, text = "Material", id = View.generateViewId()),
            QuestionOption(sequence = 1, text = "Design", id = View.generateViewId()),
            QuestionOption(sequence = 2, text = "Components", id = View.generateViewId()),
            QuestionOption(sequence = 3, text = "Android", id = View.generateViewId())
        )
        return Question(
            questionTypeId = 2,
            fullTitle = "Dummy Select Box question",
            options = options
        )
    }
}