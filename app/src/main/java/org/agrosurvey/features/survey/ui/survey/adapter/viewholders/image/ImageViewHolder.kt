package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.image

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.agrosurvey.R
import com.agrosurvey.databinding.ItemImageBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.BaseViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.QuestionInterface

class ImageViewHolder(val context: Context, private val binding: ItemImageBinding):
    BaseViewHolder(binding.root) {
    private val viewModel = ImageViewModel()

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
            R.id.image_frame_layout,
            ImageQuestionFragment.newInstance(
                questionAndResponse = questionAndResponse,
                filePathInterface = object : ImageQuestionFragment.FilePathInterface{
                    override fun onNewPath(imagePath: String) {
                        questionInterface.onChange(questionAndResponse.question!!,  imagePath)
                    }

            })
        ).commit()

        binding.executePendingBindings()
    }



}
