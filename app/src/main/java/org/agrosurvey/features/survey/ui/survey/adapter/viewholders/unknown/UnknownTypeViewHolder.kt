package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.unknown

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.agrosurvey.databinding.ItemUnknownBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.agrosurvey.survey.data.question.QuestionAndType
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.BaseViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.QuestionInterface

class UnknownTypeViewHolder(val context: Context, private val binding: ItemUnknownBinding):
    BaseViewHolder(binding.root) {
    private val viewModel =
        UnknowTypeViewModel()

    lateinit var questionInterface: QuestionInterface

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
