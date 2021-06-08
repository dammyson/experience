package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.datetime

import android.content.Context
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.agrosurvey.R
import com.agrosurvey.databinding.ItemDateBinding
import com.agrosurvey.databinding.ItemDatetimeBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.BaseViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.QuestionInterface
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.time.OffsetDateTime
import java.util.*

class DateTimeViewHolder(val context: Context, private val binding: ItemDatetimeBinding):
    BaseViewHolder(binding.root) {
    private val viewModel =
        DateTimeViewModel()

    lateinit var questionInterface: QuestionInterface


    override fun bind(questionAndResponse: QuestionAndResponse){
        viewModel.bind(questionAndResponse)
        binding.viewModel = viewModel
        binding.lifecycleOwner = context as LifecycleOwner


        binding.dateField.setOnClickListener {
            viewModel.setDateEditing()
        }

        binding.timeField.setOnClickListener {
            viewModel.setTimeEditing()
        }

        viewModel.isMandatory.observe(context, Observer {
            if(it!!){
                binding.isMandatory.visibility = View.VISIBLE
            } else {
                binding.isMandatory.visibility =  View.GONE
                questionInterface.onChange(questionAndResponse.question!!, null)
            }

        })

        viewModel.dateEditing.observe(context, Observer {
            if(it){
                val datePicker = DatePickerDialog.newInstance(
                    viewModel.dateEditListener,
                    viewModel.calendar.get(Calendar.YEAR),
                    viewModel.calendar.get(Calendar.MONTH),
                    viewModel.calendar.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.accentColor = context.resources.getColor(R.color.black)
                datePicker.isThemeDark = false
                datePicker.show((context as AppCompatActivity).fragmentManager, "Picker")
            }

        })
        viewModel.dateLabel.observe(context, Observer {
            binding.dateField.text = it
        })


        //TIME
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

        viewModel.response.observe(context, Observer {
            if(viewModel.isFullyAnswered())
                questionInterface.onChange(questionAndResponse.question!!, it)
        })



        binding.executePendingBindings()
    }



}
