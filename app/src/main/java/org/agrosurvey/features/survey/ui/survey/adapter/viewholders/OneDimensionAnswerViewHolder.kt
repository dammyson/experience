package org.agrosurvey.features.survey.ui.survey.adapter.viewholders

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.agrosurvey.R
import com.agrosurvey.databinding.ItemOneDimensionBinding
import com.agrosurvey.databinding.ItemRadioButtonBinding
import com.agrosurvey.databinding.ItemSelectBoxBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.agrosurvey.survey.data.question.QuestionAndType

class OneDimensionAnswerViewHolder(val context: Context, private val binding: ItemOneDimensionBinding):
    BaseViewHolder(binding.root) {
    private val viewModel = OneDimensionAnswerViewModel()

    lateinit var questionInterface: QuestionInterface

    interface QuestionInterface {
        fun onChange(question: QuestionAndType, response : Any)
    }

    override fun bind(questionAndResponse: QuestionAndResponse){
        viewModel.bind(questionAndResponse)
        binding.viewModel = viewModel
        binding.lifecycleOwner = context as LifecycleOwner


        viewModel.userResponse.observe(context, Observer { enteredText ->
            questionInterface.onChange(questionAndResponse.question!!,  enteredText)

        })

        binding.executePendingBindings()
    }


}
