package org.agrosurvey.forms.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.textfield.TextInputLayout
import org.agrosurvey.forms.R
import org.agrosurvey.forms.rendering.FieldView
import org.agrosurvey.forms.widget.EnumFieldType.*


class TextInput : LinearLayout, FieldView {

    private lateinit var field: TextInputLayout
    private lateinit var title: TextView
    private lateinit var type: EnumFieldType
    private var isRequired: Boolean = false


    override fun setTitle(string: String, isRequired: Boolean) {
        this.title.text = string
        this.isRequired = isRequired
    }

    override fun keepType(myType: EnumFieldType) {
        if (myType.isTextType()) {
            type = myType
            format(type)
        }
    }

    private fun format(textFormat: EnumFieldType) {

        field.editText?.run {
            when (textFormat) {

                short_text -> {
                    inputType = TextInputUtils.FieldInputTypes.getValue("text")
                    enableScroll(false)
                }

                long_text -> {
                    inputType = TextInputUtils.FieldInputTypes.getValue("textMultiLine")
                    enableScroll(true)
                }

                integer -> {
                    inputType = TextInputUtils.FieldInputTypes.getValue("number")
                    enableScroll(false)
                }

                decimal -> {
                    inputType = TextInputUtils.FieldInputTypes.getValue("numberDecimal")
                    enableScroll(false)
                }

                phone_number -> {
                    inputType = TextInputUtils.FieldInputTypes.getValue("phone")
                    enableScroll(false)
                }
                else -> {
                }
            }
        }

        invalidate()
    }

    private fun enableScroll(scroll: Boolean) {
        field.editText?.isSingleLine = !scroll
    }

    /**
     * Let you grab input data provided by user after filling the form; Return a Response or a FormError if something went wrong
     */
    fun collectData(): String {
        return ""//editText.text.toString()

    }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initView()
    }

    private fun initView() {
        val view = View.inflate(context, R.layout.widget_form_text, null)
        val c = view.findViewById<CardView>(R.id.container)
        field = c.findViewById(R.id.field)
        title = c.findViewById(R.id.title)
        addView(view)

        val layoutParams = c.layoutParams
        layoutParams.width = LayoutParams.MATCH_PARENT
        layoutParams.height = LayoutParams.WRAP_CONTENT
        c.layoutParams = layoutParams

        invalidate()
    }
}