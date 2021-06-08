package org.agrosurvey.forms.widget


import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import org.agrosurvey.forms.R
import org.agrosurvey.forms.rendering.FieldView


class RadioBox : LinearLayout, FieldView {

    private lateinit var field: RadioGroup
    private lateinit var title: TextView
    private lateinit var help: TextView
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
        val view = View.inflate(context, R.layout.widget_form_radio_box, null)
        val c = view.findViewById<CardView>(R.id.container)
        field = c.findViewById(R.id.field)
        title = c.findViewById(R.id.title)
        help = c.findViewById(R.id.help)
        setHelperTextEnabled(false)

        addView(view)

        val layoutParams = c.layoutParams
        layoutParams.width = LayoutParams.MATCH_PARENT
        layoutParams.height = LayoutParams.WRAP_CONTENT
        c.layoutParams = layoutParams

        invalidate()
    }

    fun setHelperTextEnabled(enable: Boolean) {
        help.isVisible = enable
    }

    fun setHelperText(helperText: String) {
        help.text = helperText
    }

    private fun RadioGroup.setListItems(items: List<String>, defaultPosition: Int = 0) {
        val optionViews = mutableListOf<RadioButton>()
        items.forEach {
            RadioButton(context).run {
                text = it
                id = View.generateViewId()
                val lp = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                val margin =
                    if (items.indexOf(it) == items.size - 1) 0 else resources.getDimensionPixelSize(
                        R.dimen.grid_0_25
                    )

                lp.setMargins(0, margin, 0, 0)
                addView(this, lp)
                optionViews.add(this)
            }
        }

        invalidate()
    }
}