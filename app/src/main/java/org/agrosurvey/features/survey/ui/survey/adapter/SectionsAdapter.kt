package org.agrosurvey.features.survey.ui.survey.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agrosurvey.R
import org.agrosurvey.features.survey.ui.survey.viewitems.Sections


class SectionsAdapter(private val products: ArrayList<Sections>  ) : RecyclerView.Adapter<SectionsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.section_row, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

      holder.title.text = products[position].title
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val title:  TextView = itemView.findViewById(R.id.title)
    }
}