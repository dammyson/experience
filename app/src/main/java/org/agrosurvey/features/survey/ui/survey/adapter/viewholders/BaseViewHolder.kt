package org.agrosurvey.features.survey.ui.survey.adapter.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.agrosurvey.survey.data.question.QuestionAndResponse

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item : QuestionAndResponse)
}

