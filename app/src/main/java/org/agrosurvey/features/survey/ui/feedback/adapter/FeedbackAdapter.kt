package org.agrosurvey.features.survey.ui.feedback.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.agrosurvey.R
import com.agrosurvey.databinding.ItemFeedbackBinding
import com.agrosurvey.survey.data.question.feedback.Feedback
import com.agrosurvey.survey.data.question.feedback.FeedbackAndSurvey

class FeedbackAdapter (val context: Context) : PagedListAdapter<FeedbackAndSurvey, RecyclerView.ViewHolder>(
    diffCallback
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        val databinding: ItemFeedbackBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_feedback, parent, false
            )
        return FeedbackViewHolder(context, databinding)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FeedbackViewHolder).bind(getItem(position)!!)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val item = getItem(position)
            (holder as FeedbackViewHolder).bind(item!!)
        } else {
            onBindViewHolder(holder, position)
        }
    }


    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<FeedbackAndSurvey>() {
            override fun areContentsTheSame(oldItem: FeedbackAndSurvey, newItem: FeedbackAndSurvey): Boolean =
                oldItem.feedback.createdOn == newItem.feedback.createdOn

            override fun areItemsTheSame(oldItem: FeedbackAndSurvey, newItem: FeedbackAndSurvey): Boolean =
                oldItem.survey.id == newItem.survey.id
        }
    }

}