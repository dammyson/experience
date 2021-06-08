package org.agrosurvey.forms.widget


import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.textfield.TextInputLayout
import org.agrosurvey.forms.R
import org.agrosurvey.forms.rendering.FieldView


class SelectBox : LinearLayout, FieldView {

    private lateinit var field: TextInputLayout
    private lateinit var title: TextView
    private lateinit var type: EnumFieldType
    private var isRequired: Boolean = false


    private val selectableList = mutableListOf<String>()
    override fun keepType(myType: EnumFieldType) {
        type = myType
    }

    override fun setTitle(string: String, isRequired: Boolean) {
        this.title.text = string
        this.isRequired = isRequired
    }

    fun setSelectableItems(items: List<String>) {
        selectableList.clear()
        selectableList.addAll(items)
        field.setListItems(selectableList)
        invalidate()
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
        val view = View.inflate(context, R.layout.widget_form_select_box, null)
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

    private fun TextInputLayout.setListItems(items: List<String>, defaultPosition: Int = 0) {
        val adapter = ArrayAdapter(context, R.layout.list_item_input, items)
        (editText as? AutoCompleteTextView)?.apply {
            setAdapter(adapter)
            if (items.size > defaultPosition) {
                setSelection(defaultPosition)
            }
        }
    }
}