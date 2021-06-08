package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.singlepoint

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.agrosurvey.R
import com.agrosurvey.databinding.FragmentSinglePointBinding
import com.agrosurvey.survey.data.ResponseValue
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import org.agrosurvey.features.survey.ui.survey.adapter.SingleShotLocationProvider


class SinglePointMapFragment(val questionAndResponse: QuestionAndResponse?) : Fragment() {

    var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null


    lateinit var binding : FragmentSinglePointBinding

    lateinit var mapLocationInterface: MapLocationInterface

    var marker: Marker? = null

    interface MapLocationInterface{
        fun onNewLocation(location: LatLng)
    }



    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_point, container, false)

        mMapView = binding.map
        binding.reauiredAccuracy.text = "Req. ${questionAndResponse!!.question!!.question.map_accuracy}"

        mMapView?.onCreate(savedInstanceState)
        mMapView?.onResume() // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(activity?.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }





        mMapView?.getMapAsync(OnMapReadyCallback { mMap ->
            googleMap = mMap

            googleMap?.isMyLocationEnabled = true


            SingleShotLocationProvider.requestSingleUpdate(
                requireActivity(),
                object :
                    SingleShotLocationProvider.LocationCallback {
                    override fun onNewLocationAvailable(location: SingleShotLocationProvider.GPSCoordinates?) {

                        if(questionAndResponse?.response?.value != null){
                            val savedLocation =
                                (questionAndResponse?.response?.value as ResponseValue.SinglePoint)
                                    .location?.let { LatLng(it.latitute, it.longitude)}!!

                            marker?.remove()
                            marker = googleMap!!.addMarker(
                                MarkerOptions().position(savedLocation).title("Selected position")
                                    .snippet("${savedLocation.longitude},${savedLocation.latitude}")
                            )
                            marker?.showInfoWindow()

                            val cameraPosition =
                                CameraPosition.Builder().target(savedLocation).zoom(22f).build()
                            googleMap!!.animateCamera(
                                CameraUpdateFactory.newCameraPosition(
                                    cameraPosition
                                )
                            )

                        }else{
                            marker?.showInfoWindow()
                            mapLocationInterface.onNewLocation(location = LatLng(
                                location!!.longitude.toDouble(), location.latitude.toDouble()
                            ))


                            val currentLocation = LatLng(
                                location?.latitude?.toDouble()!!,
                                location.longitude.toDouble()
                            )
                            val cameraPosition =
                                CameraPosition.Builder().target(currentLocation).zoom(22f).build()
                            googleMap!!.animateCamera(
                                CameraUpdateFactory.newCameraPosition(
                                    cameraPosition
                                )
                            )
                        }



                    }

                })
        })


        val startBtn = binding.startButton
        startBtn.setOnClickListener {
            markCurrentLocation()
        }

        return binding.root
    }


    fun markCurrentLocation(){
        SingleShotLocationProvider.requestSingleUpdate(
            requireActivity(),
            object :
                SingleShotLocationProvider.LocationCallback {
                override fun onNewLocationAvailable(location: SingleShotLocationProvider.GPSCoordinates?) {

                    binding.accuracy.text = "${location?.accuracy}m"
                    val currentLocation = LatLng(
                        location?.latitude?.toDouble()!!,
                        location.longitude.toDouble()
                    )
                    val cameraPosition =
                        CameraPosition.Builder().target(currentLocation).zoom(22f).build()
                    googleMap!!.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            cameraPosition
                        )
                    )

                    val nonNUllAaccuracy = questionAndResponse?.question?.question?.map_accuracy?.toFloat()?:25.0F
                    if (location.accuracy <= nonNUllAaccuracy) {

                        val dialogClickListener =
                            DialogInterface.OnClickListener { dialog, which ->
                                when (which) {


                                    DialogInterface.BUTTON_POSITIVE -> {



                                        marker?.remove()
                                        marker = googleMap!!.addMarker(
                                            MarkerOptions().position(currentLocation).title("Selected position")
                                                .snippet("${location.longitude},${location.latitude}")
                                        )
                                        marker?.showInfoWindow()
                                        mapLocationInterface.onNewLocation(location = LatLng(
                                            location.longitude.toDouble(), location.latitude.toDouble()
                                        ))
                                        Toast.makeText(requireActivity(), "Marked", Toast.LENGTH_SHORT).show()
                                    }


                                    DialogInterface.BUTTON_NEGATIVE -> {
                                    }


                                }
                            }

                        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
                        builder.setMessage(getString(R.string.msg_confirm_position) + " Point(${location.longitude} long; ${location.latitude} lat)")
                            .setPositiveButton(getString(
                            R.string.yes), dialogClickListener)
                            .setNegativeButton(getString(R.string.No), dialogClickListener).show()


                    } else {
                        Toast.makeText(requireActivity(), "Bad GPS accuracy !", Toast.LENGTH_SHORT)
                            .show()
                    }

                }

            })
    }

    companion object {
        @JvmStatic
        fun newInstance(questionAndResponse: QuestionAndResponse?,
                        locationInterface: MapLocationInterface
        ) = SinglePointMapFragment(questionAndResponse).apply {
            this.mapLocationInterface = locationInterface
        }
    }


    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView?.onLowMemory()
    }

}