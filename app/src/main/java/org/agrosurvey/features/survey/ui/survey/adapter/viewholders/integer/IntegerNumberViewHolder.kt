package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.integer

import android.content.Context
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.agrosurvey.databinding.ItemIntegerTextBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.agrosurvey.survey.data.question.QuestionAndType
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.BaseViewHolder

class IntegerNumberViewHolder(val context: Context, private val binding: ItemIntegerTextBinding):
    BaseViewHolder(binding.root) {
    private val viewModel =
        IntegerNumberViewModel()

    lateinit var questionInterface: QuestionInterface

    interface QuestionInterface {
        fun onChange(question: QuestionAndType, response: String?)
    }

    override fun bind(questionAndResponse: QuestionAndResponse){
        viewModel.bind(questionAndResponse)
        binding.viewModel = viewModel
        binding.lifecycleOwner = context as LifecycleOwner


        binding.subtitle.doOnTextChanged { enteredText, start, before, count ->
            questionInterface.onChange(questionAndResponse.question!!,  enteredText.toString())
        }

        viewModel.isMandatory.observe(context, Observer {
            if(it!!){
                binding.isMandatory.visibility = View.VISIBLE
            } else {
                binding.isMandatory.visibility =  View.GONE
                questionInterface.onChange(questionAndResponse.question!!, null)
            }

        })
        viewModel.userResponse.observe(context, Observer { value ->
            if(value != null){
                binding.subtitle.setText(value.toString())
            }
        })

        binding.executePendingBindings()
    }



}
