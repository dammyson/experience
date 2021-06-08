package org.agrosurvey.forms.widget


import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import org.agrosurvey.forms.R
import org.agrosurvey.forms.R.color
import org.agrosurvey.forms.widget.FormPagerTab.FormTabMode.SELECTED


class FormPagerTab : LinearLayout {

    private lateinit var icon: ImageView


    fun setMode(mode: FormTabMode, text: String) {
        val textColor = ContextCompat.getColor(
            context, if (mode == SELECTED) color.white_form
            else color.black_form
        )

        val background = ContextCompat.getColor(
            context,
            if (mode == SELECTED) color.black_form
            else color.white_form
        )
        val drawable = TextInputUtils.createCircularTextDrawable(
            text = text,
            textColor = textColor,
            background = Color.RED
        )
        icon.setImageDrawable(drawable)
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
        val view = View.inflate(context, R.layout.tab_item, null)
        val c = view.findViewById<ConstraintLayout>(R.id.container)
        icon = c.findViewById(R.id.icon)
        addView(view)

        val layoutParams = c.layoutParams
        layoutParams.width = LayoutParams.MATCH_PARENT
        layoutParams.height = LayoutParams.WRAP_CONTENT
        c.layoutParams = layoutParams

        invalidate()
    }

    enum class FormTabMode {
        SELECTED, UNSELECTED
    }
}