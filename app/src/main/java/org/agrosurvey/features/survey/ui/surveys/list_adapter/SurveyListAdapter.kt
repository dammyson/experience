package org.agrosurvey.features.survey.ui.surveys.list_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agrosurvey.databinding.ListItemSurveyBinding
import org.agrosurvey.features.survey.ui.surveys.viewitems.SurveyViewItem


class SurveyListAdapter(private val onSelectedItem: SurveyViewClick) :
    ListAdapter<SurveyViewItem, SurveyViewHolder>(DiffSurvey) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder {
        return SurveyViewHolder(
            ListItemSurveyBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SurveyViewHolder, position: Int) {
        holder.bind(getItem(position), onSelectedItem)
    }
}

interface SurveyViewClick {
    fun onClick(view: View, item: SurveyViewItem)
}

class SurveyViewHolder(
    private val binding: ListItemSurveyBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: SurveyViewItem,
        onClick: SurveyViewClick
    ) {
        binding.run {
            this.item = item

            this.clickHandler = onClick
            showSurveyStatus()

            executePendingBindings()
        }
    }


    private fun showSurveyStatus() {
        // todo display the right icon according to survey status
    }
}