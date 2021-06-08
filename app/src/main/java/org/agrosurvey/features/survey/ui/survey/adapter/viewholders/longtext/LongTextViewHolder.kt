package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.longtext

import android.content.Context
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.agrosurvey.databinding.ItemLongTextBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.agrosurvey.survey.data.question.QuestionAndType
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.BaseViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.QuestionInterface

class LongTextViewHolder(val context: Context, private val binding: ItemLongTextBinding):
    BaseViewHolder(binding.root) {
    private val viewModel =
        LongTextViewModel()

    lateinit var questionInterface: QuestionInterface

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

        viewModel.userResponse.observe(context, Observer { enteredText ->
            if(enteredText.isNullOrBlank().not()){
                binding.subtitle.setText(enteredText.toString())
            }
        })
        
        binding.executePendingBindings()
    }



}
