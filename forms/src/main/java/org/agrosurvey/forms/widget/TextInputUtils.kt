package org.agrosurvey.forms.widget

import android.text.InputType
import androidx.annotation.ColorInt

object TextInputUtils {
    val FieldInputTypes = mapOf(

        "date" to (InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_DATE),

        "datetime" to (InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_NORMAL),

        "none" to (InputType.TYPE_NULL),
        "number" to (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL),

        "numberDecimal" to (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL),

        "numberPassword" to (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD),
        "numberSigned" to (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED),

        "phone" to (InputType.TYPE_CLASS_PHONE),

        "text" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL),

        "textAutoComplete" to (InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE),
        "textAutoCorrect" to (InputType.TYPE_TEXT_FLAG_AUTO_CORRECT),
        "textCapCharacters" to (InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS),
        "textCapSentences" to (InputType.TYPE_TEXT_FLAG_CAP_SENTENCES),
        "textCapWords" to (InputType.TYPE_TEXT_FLAG_CAP_WORDS),

        "textEmailAddress" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS),

        "textEmailSubject" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT),
        "textFilter" to (InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE),

        "textLongMessage" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE),
        "textMultiLine" to (InputType.TYPE_TEXT_FLAG_MULTI_LINE),
        "textNoSuggestions" to (InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS),

        "textPassword" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD),
        "textPersonName" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME),

        "textPhonetic" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PHONETIC),

        "textPostalAddress" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS),

        "textShortMessage" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE),

        "textUri" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_URI),

        "textVisiblePassword" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD),
        "textWebEditText" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT),
        "textWebEmailAddress" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS),

        "textWebPassword" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD),
        "time" to (InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_TIME)

    )

    fun createCircularTextDrawable(
        text: String,
        @ColorInt textColor: Int,
        @ColorInt background: Int
    ): TextDrawable {
        val builder = TextDrawable.builder()
            .beginConfig()
            .textColor(textColor)
            .endConfig()
            .round()
        return builder.build(text, background)
    }
}