package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.polygon

import android.app.Dialog
import android.content.Context
import android.view.Window
import androidx.constraintlayout.widget.ConstraintLayout
import com.agrosurvey.R


class MarkingOptionDialog {
    fun showDialog(context: Context,
                   onAutomaticChosen : () -> Unit,
                   onManualChosen : () -> Unit) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.map_options_dialog)
        val optionAutomatic = dialog.findViewById(R.id.option_automatic) as ConstraintLayout
        val optionManual = dialog.findViewById(R.id.option_manual) as ConstraintLayout

        optionAutomatic.setOnClickListener {
            onAutomaticChosen.invoke()
            dialog.dismiss()
        }

        optionManual.setOnClickListener {
            onManualChosen.invoke()
            dialog.dismiss()
        }


        dialog.show()
    }
}