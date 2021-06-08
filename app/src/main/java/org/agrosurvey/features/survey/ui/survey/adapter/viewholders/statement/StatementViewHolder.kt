package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.statement

import android.content.Context
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.agrosurvey.databinding.ItemShortTextBinding
import com.agrosurvey.databinding.ItemStatementBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.agrosurvey.survey.data.question.QuestionAndType
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.BaseViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.QuestionInterface

class StatementViewHolder(val context: Context, private val binding: ItemStatementBinding):
    BaseViewHolder(binding.root) {
    private val viewModel =
        StatementViewModel()

    lateinit var questionInterface: QuestionInterface

    override fun bind(questionAndResponse: QuestionAndResponse){
        viewModel.bind(questionAndResponse)
        binding.viewModel = viewModel
        binding.lifecycleOwner = context as LifecycleOwner

        viewModel.isMandatory.observe(context, Observer {
            questionInterface.onChange(questionAndResponse.question!!, null)
        })

        binding.executePendingBindings()
    }



}
