package org.agrosurvey.features.survey.ui.surveys.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.agrosurvey.R
import com.agrosurvey.databinding.ItemSurveyBinding
import com.agrosurvey.survey.data.survey.Survey

class SurveyAdapter(val context: Context) : PagedListAdapter<Survey, RecyclerView.ViewHolder>(
    diffCallback
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        val databinding: ItemSurveyBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_survey, parent, false
            )
        return SurveyViewHolder(context, databinding)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SurveyViewHolder).bind(getItem(position)!!)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val item = getItem(position)
            (holder as SurveyViewHolder).bind(item!!)
        } else {
            onBindViewHolder(holder, position)
        }
    }


    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Survey>() {
            override fun areContentsTheSame(oldItem: Survey, newItem: Survey): Boolean =
                oldItem.title == newItem.title

            override fun areItemsTheSame(oldItem: Survey, newItem: Survey): Boolean =
                oldItem.id == newItem.id
        }
    }

}
