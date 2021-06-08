package org.agrosurvey.forms.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import com.google.android.material.button.MaterialButton
import org.agrosurvey.forms.R

/**
 *  Represents a the visualization of a list of questions
 *  that are available on the same
 */
class Page @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var contentView: LinearLayout
    private lateinit var continueButton: MaterialButton
    private var onNext: OnNextClickListener? = null

    init {
        init()
    }

    private fun init() {
        val root = View.inflate(context, R.layout.widget_form_page, null)
        val container = root.findViewById<NestedScrollView>(R.id.container)
        contentView = root.findViewById(R.id.content)
        continueButton = root.findViewById(R.id.submit)
        continueButton.setOnClickListener {
            onNext?.onNext()
        }

        addView(root)

        val layoutParams = container.layoutParams
        layoutParams.width = LayoutParams.MATCH_PARENT
        layoutParams.height = LayoutParams.MATCH_PARENT
        container.layoutParams = layoutParams

        invalidate()
    }

    fun addField(item: View) {
        val lp = retrieveLayoutParams()
        val margin = resources.getDimensionPixelSize(R.dimen.grid_1)
        lp.setMargins(0, margin, 0, 0)

        with(contentView) {
            post {
                contentView.addView(item, lp)
            }
        }
    }

    fun setOnNextClickListener(onNext: OnNextClickListener) {
        this.onNext = onNext
    }


    private fun retrieveLayoutParams() = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    fun setButtonText(textRes: Int) {
        continueButton.setText(textRes)
        invalidate()
    }
}

interface OnNextClickListener {
    fun onNext()
}