package org.agrosurvey.features.survey.ui.surveys.adapter

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.agrosurvey.databinding.ItemSurveyBinding
import com.agrosurvey.survey.data.survey.Survey
import org.agrosurvey.Navigator
import org.agrosurvey.features.survey.ui.feedback.FeedbackActivity

class SurveyViewHolder(val context: Context, private val binding: ItemSurveyBinding):
        RecyclerView.ViewHolder(binding.root),
        Navigator<Survey> {


    private val viewModel = SurveyViewModel()

    fun bind(survey: Survey){
        viewModel.bind(survey)
        viewModel.navigator = this
        binding.viewModel = viewModel
        binding.survey = survey
        binding.lifecycleOwner = context as LifecycleOwner
        binding.executePendingBindings()
    }

    override fun itemClicked(item: Survey) {
        val intent = Intent(context, FeedbackActivity::class.java)
         intent.putExtra("survey", item)
        context.startActivity(intent)
        val surveyDetail = com.agrosurvey.survey.Survey(item)

    }


}