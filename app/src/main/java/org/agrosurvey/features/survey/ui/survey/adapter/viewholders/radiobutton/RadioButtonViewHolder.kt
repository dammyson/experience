package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.radiobutton

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.core.view.get
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.agrosurvey.databinding.ItemRadioButtonBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.agrosurvey.survey.data.question.QuestionAndType
import com.agrosurvey.survey.data.question.option.Option
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.BaseViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.QuestionInterface


class RadioButtonViewHolder(val context: Context, private val binding: ItemRadioButtonBinding) :
    BaseViewHolder(binding.root) {
    private val viewModel = RadioButtonViewModel()

    lateinit var questionInterface: QuestionInterface


    @SuppressLint("ResourceType")
    override fun bind(questionAndResponse: QuestionAndResponse) {
        viewModel.bind(questionAndResponse)
        binding.viewModel = viewModel
        binding.lifecycleOwner = context as LifecycleOwner

        viewModel.isMandatory.observe(context, Observer {
            if(it!!){
                binding.isMandatory.visibility = View.VISIBLE
            } else {
                binding.isMandatory.visibility =  View.GONE
                questionInterface.onChange(questionAndResponse.question!!, null)
            }

        })

        val options = questionAndResponse.question?.question?.options as List<Option>
        val radioGroup = binding.radiomain

        val memoOptions : HashMap<Int, Int>  = HashMap()

        for (i in options.indices) {
            val radioButton1 = RadioButton(context)
            radioButton1.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            memoOptions[options[i].id!!] = i
            radioButton1.text = options[i].text
            radioButton1.id = i

            radioGroup.addView(radioButton1)
        }



        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            questionInterface.onChange(questionAndResponse.question!!, options[checkedId])
        }

        viewModel.userResponse.observe(context, Observer { option ->
            option?.let {
                (radioGroup[memoOptions[it.id]!!] as RadioButton).isChecked = true
            }
        })

        binding.executePendingBindings()
    }

}