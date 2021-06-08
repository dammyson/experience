package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.pathline

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.agrosurvey.R
import com.agrosurvey.databinding.ItemPathLineBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.google.android.gms.maps.model.LatLng
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.BaseViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.QuestionInterface

class PathLineMapViewHolder(val context: Context, private val binding: ItemPathLineBinding):
    BaseViewHolder(binding.root) {
    private val viewModel = PathLineMapViewModel()

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
        beginTransaction().replace(R.id.path_line_frame_layout,
            PathLineMapFragment(questionAndResponse)
                .apply {
                this.mapLineInterface = object : PathLineMapFragment.MapLineInterface{
                    override fun onNewLine(linePoints: List<LatLng>) {
                        questionInterface.onChange(questionAndResponse.question!!,
                            linePoints.map { com.agrosurvey.survey.data.LatLng(it.longitude, it.latitude) })
                    }

                }
            }
        ).commit()
        binding.executePendingBindings()
    }




}
