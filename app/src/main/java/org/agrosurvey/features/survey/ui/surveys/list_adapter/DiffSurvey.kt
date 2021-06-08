package org.agrosurvey.features.survey.ui.surveys.list_adapter

import androidx.recyclerview.widget.DiffUtil
import org.agrosurvey.features.survey.ui.surveys.viewitems.SurveyViewItem

object DiffSurvey : DiffUtil.ItemCallback<SurveyViewItem>() {
    override fun areItemsTheSame(oldItem: SurveyViewItem, newItem: SurveyViewItem) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SurveyViewItem, newItem: SurveyViewItem) =
        oldItem == newItem
}