package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.polygon

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.agrosurvey.R
import com.agrosurvey.databinding.ItemPolygonMapBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.google.android.gms.maps.model.LatLng
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.BaseViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.QuestionInterface

class PolygonMapViewHolder(val context: Context, private val binding: ItemPolygonMapBinding):
    BaseViewHolder(binding.root) {
    private val viewModel = PolygonMapViewModel()

    lateinit var questionInterface: QuestionInterface

    override fun bind(questionAndResponse: QuestionAndResponse){
        viewModel.bind(questionAndResponse)
        binding.viewModel = viewModel
        binding.lifecycleOwner = context as LifecycleOwner

        viewModel.userResponse.observe(context, Observer { enteredText ->
            questionInterface.onChange(questionAndResponse.question!!,  enteredText)
        })

        viewModel.isMandatory.observe(context, Observer {
            if(it!!){
                //binding.isMandatory.visibility = View.VISIBLE
            } else {
                //binding.isMandatory.visibility =  View.GONE
                questionInterface.onChange(questionAndResponse.question!!, null)
            }

        })

        (context as AppCompatActivity).supportFragmentManager.
        beginTransaction().replace(R.id.frame_layout,
            MapFragment(
                questionAndResponse
            ).apply {
                this.polygonInterface = object : MapFragment.MapPolygonInterface {
                    override fun onNewPolygon(polygon: List<LatLng>) {
                        questionInterface.onChange(questionAndResponse.question!!,
                            polygon.map { com.agrosurvey.survey.data.LatLng(it.longitude, it.latitude) })
                    }
                }
            }
        ).commit()
        binding.executePendingBindings()
    }




}
