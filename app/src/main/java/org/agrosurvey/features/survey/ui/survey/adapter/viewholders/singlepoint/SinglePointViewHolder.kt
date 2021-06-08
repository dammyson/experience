package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.singlepoint

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.agrosurvey.R
import com.agrosurvey.databinding.ItemSinglePointBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.google.android.gms.maps.model.LatLng
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.BaseViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.QuestionInterface

class SinglePointViewHolder(val context: Context, private val binding: ItemSinglePointBinding):
    BaseViewHolder(binding.root) {
    private val viewModel = SinglePointViewModel()

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
        beginTransaction().replace(R.id.single_point_frame_layout,
            SinglePointMapFragment.newInstance(
                questionAndResponse = questionAndResponse,
                locationInterface = object : SinglePointMapFragment.MapLocationInterface{
                    override fun onNewLocation(location: LatLng) {
                        questionInterface.onChange(questionAndResponse.question!!,
                            com.agrosurvey.survey.data.LatLng(latitute = location.latitude, longitude = location.longitude))
                    }

                }
            )
        ).commit()
        binding.executePendingBindings()
    }




}
