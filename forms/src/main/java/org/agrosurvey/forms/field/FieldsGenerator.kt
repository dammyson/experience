package org.agrosurvey.forms.field

import android.content.Context
import org.agrosurvey.domain.entities.get.Question
import org.agrosurvey.domain.entities.get.QuestionType
import org.agrosurvey.forms.rendering.FieldView
import org.agrosurvey.forms.widget.*
import org.agrosurvey.forms.widget.EnumFieldType.*

class FieldsGenerator(private val supportedQuestionTypes: List<QuestionType>) {
    // todo delegate this to FieldView or FormField

    fun createField(
        context: Context,
        q: Question,
        onCreated: (FieldView, EnumFieldType) -> Unit
    ) {
        val type = supportedQuestionTypes.getQuestionType(q)

        if (type?.isTextType() == true) {
            createTextField(context, q, onCreated)
        }

        when (type) {

            select_box -> {
                createSelectBoxField(context, q, onCreated)
            }

            radio_button -> {
                createRadioBoxField(context, q, onCreated)
            }

            checkbox -> {
                createCheckboxField(context, q, onCreated)
            }

            reference_data_responses -> {
            }
            auto_generated_id -> {
            }
            date_time_picker -> {
            }
            date_picker -> {
            }
            time_picker -> {
            }
            month_picker -> {
            }
            day_of_week_picker -> {
            }
            signature -> {
            }
            image -> {
            }
            audio -> {
            }
            polygon_plot -> {
            }
            path_line -> {
            }
            single_point -> {
            }
            one_dimensional_table -> {
            }
            two_dimensional_table -> {
            }
            barcode -> {
            }
            qr_code -> {
            }
            payment_button -> {
            }
            finger_print -> {
            }
            statement -> {
            }
            pdf_file -> {
            }

            null -> {
            }
            else -> {
            }
        }
    }

    private fun createTextField(
        context: Context, q: Question,
        onCreated: (TextInput, EnumFieldType) -> Unit
    ) {
        val type = supportedQuestionTypes.getQuestionType(q)

        if (type?.isTextType() == true) {
            val widget = TextInput(context)

            with(widget) {
                q.fullTitle?.let { setTitle(it, q.isMandatory.toBoolean()) }
                keepType(type)
                onCreated(this, type)
            }
        }
    }

    private fun createSelectBoxField(
        context: Context, q: Question,
        onCreated: (SelectBox, EnumFieldType) -> Unit
    ) {

        val type = supportedQuestionTypes.getQuestionType(q)

        if (type?.isTextType() != true) {
            val widget = SelectBox(context)
            widget.setSelectableItems(q.getOptionsText())

            with(widget) {
                q.fullTitle?.let { setTitle(it, q.isMandatory.toBoolean()) }
                type?.let {
                    keepType(it)
                    onCreated(this, it)
                }

            }
        }
    }

    private fun createCheckboxField(
        context: Context, q: Question,
        onCreated: (CheckBox, EnumFieldType) -> Unit
    ) {

        val type = supportedQuestionTypes.getQuestionType(q)

        if (type?.isTextType() != true) {
            val widget = CheckBox(context)
            widget.setSelectableItems(q.getOptionsText())

            with(widget) {
                q.fullTitle?.let { setTitle(it, q.isMandatory.toBoolean()) }
                type?.let {
                    keepType(it)
                    onCreated(this, it)
                }

            }
        }
    }

    fun createRadioBoxField(
        context: Context, q: Question,
        onCreated: (RadioBox, EnumFieldType) -> Unit
    ) {

        val type = supportedQuestionTypes.getQuestionType(q)

        if (type?.isTextType() != true) {
            val widget = RadioBox(context)
            widget.setSelectableItems(q.getOptionsText())

            with(widget) {
                q.fullTitle?.let { setTitle(it, q.isMandatory.toBoolean()) }
                type?.let {
                    keepType(it)
                    onCreated(this, it)
                }

            }
        }
    }
}


fun Int?.toBoolean() = this != null && this > 0
