package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.time

import android.content.Context
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.agrosurvey.R
import com.agrosurvey.databinding.ItemShortTextBinding
import com.agrosurvey.databinding.ItemTimeBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.agrosurvey.survey.data.question.QuestionAndType
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.BaseViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.QuestionInterface
import java.util.*

class TimeViewHolder(val context: Context, private val binding: ItemTimeBinding) :
    BaseViewHolder(binding.root) {
    private val viewModel =
        TimeViewModel()

    lateinit var questionInterface: QuestionInterface

    override fun bind(questionAndResponse: QuestionAndResponse) {
        viewModel.bind(questionAndResponse)
        binding.viewModel = viewModel
        binding.lifecycleOwner = context as LifecycleOwner

        binding.timeField.setOnClickListener {
            viewModel.setTimeEditing()
        }



        viewModel.timeEditing.observe(context, Observer {


            if (it) {
                val datePicker = TimePickerDialog.newInstance(
                    viewModel.timeEditListener,
                    viewModel.calendar.get(Calendar.HOUR),
                    viewModel.calendar.get(Calendar.MINUTE),
                    viewModel.calendar.get(Calendar.SECOND),
                    true
                )
                datePicker.accentColor = context.resources.getColor(R.color.black)
                datePicker.isThemeDark = false
                datePicker.show((context as AppCompatActivity).fragmentManager, "Picker")
            }

        })

        viewModel.timeLabel.observe(context, Observer {
            binding.timeField.text = it
        })

        viewModel.time.observe(context, Observer {
            questionInterface.onChange(questionAndResponse.question!!, it)
        })

        viewModel.isMandatory.observe(context, Observer {
            if (it!!) {
                binding.isMandatory.visibility = View.VISIBLE
            } else {
                binding.isMandatory.visibility = View.GONE
                questionInterface.onChange(questionAndResponse.question!!, null)
            }

        })
        /*viewModel.userResponse.observe(context, Observer { enteredText ->
            if(enteredText.isNullOrBlank().not()){
                //TODO set the text from start
                //binding.subtitle.setText(enteredText.toString())
            }
        })*/

        binding.executePendingBindings()
    }


}
