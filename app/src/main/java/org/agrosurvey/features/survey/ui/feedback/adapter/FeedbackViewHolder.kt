package org.agrosurvey.features.survey.ui.feedback.adapter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.agrosurvey.databinding.ItemFeedbackBinding
import com.agrosurvey.survey.data.question.feedback.Feedback
import com.agrosurvey.survey.data.question.feedback.FeedbackAndSurvey
import org.agrosurvey.Navigator
import org.agrosurvey.features.survey.ui.survey.SurveyActivity

class FeedbackViewHolder(val context: Context, private val binding: ItemFeedbackBinding):
    RecyclerView.ViewHolder(binding.root),
    Navigator<FeedbackAndSurvey> {
    private val viewModel = FeedbackViewModel()

    fun bind(feedback: FeedbackAndSurvey){
        viewModel.bind(feedback)
        viewModel.navigator = this
        binding.viewModel = viewModel
        binding.feedback = feedback
        binding.lifecycleOwner = context as LifecycleOwner
        binding.executePendingBindings()
    }

    override fun itemClicked(item: FeedbackAndSurvey) {

        (context as AppCompatActivity).startActivity(
            Intent(context, SurveyActivity::class.java)
                .apply {
                    putExtra("feedback", item)
                }
        )
    }

}
