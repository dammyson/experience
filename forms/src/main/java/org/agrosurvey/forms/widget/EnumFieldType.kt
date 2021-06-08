package org.agrosurvey.forms.widget

import org.agrosurvey.domain.entities.get.Question
import org.agrosurvey.domain.entities.get.QuestionType
import org.agrosurvey.forms.widget.EnumFieldType.*

enum class EnumFieldType {
    select_box,
    radio_button,
    checkbox,
    reference_data_responses,
    auto_generated_id,
    date_time_picker,
    date_picker,
    time_picker,
    month_picker,
    day_of_week_picker,
    signature,
    image,
    audio,
    polygon_plot,
    path_line,
    single_point,
    one_dimensional_table,
    two_dimensional_table,
    barcode,
    qr_code,
    payment_button,
    finger_print,
    statement,
    pdf_file,

    short_text,
    long_text,
    integer,
    decimal,
    phone_number,
}


fun List<QuestionType>.isSupportedType(q: Question): Boolean {
    return any { it.id == q.questionTypeId }
}

fun EnumFieldType.isTextType(): Boolean {
    return when (this) {
        short_text,
        long_text,
        integer,
        decimal,
        phone_number -> true
        else -> false
    }
}

fun List<QuestionType>.getQuestionType(q: Question): EnumFieldType? {
    val matcher: QuestionType? = firstOrNull { it.id == q.questionTypeId }
    return matcher?.asInputType()
}


fun QuestionType.asInputType(): EnumFieldType? =
    values().firstOrNull { it.toString() == slug }
