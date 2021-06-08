package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.document

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.agrosurvey.R
import com.agrosurvey.databinding.ItemDocumentBinding
import com.agrosurvey.databinding.ItemImageBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.BaseViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.QuestionInterface

class DocumentViewHolder(val context: Context, private val binding: ItemDocumentBinding):
    BaseViewHolder(binding.root) {
    private val viewModel = DocumentViewModel()

    lateinit var questionInterface: QuestionInterface

    override fun bind(questionAndResponse: QuestionAndResponse){
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

        (context as AppCompatActivity).supportFragmentManager.
        beginTransaction().replace(
            R.id.document_frame_layout,
            DocumentFragment.newInstance(
                questionID = questionAndResponse.question?.question?.id!!,
                filePathInterface = object : DocumentFragment.FilePathInterface{
                    override fun onNewPath(imagePath: String) {
                        questionInterface.onChange(questionAndResponse.question!!,  imagePath)
                    }

            })
        ).commit()

        binding.executePendingBindings()
    }



}
