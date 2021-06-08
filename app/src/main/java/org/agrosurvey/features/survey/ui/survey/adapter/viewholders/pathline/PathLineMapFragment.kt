package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.pathline

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.agrosurvey.R
import com.agrosurvey.databinding.FragmentPathLineBinding
import com.agrosurvey.survey.data.ResponseValue
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import org.agrosurvey.features.survey.ui.survey.adapter.SingleShotLocationProvider


class PathLineMapFragment(val questionAndResponse: QuestionAndResponse?) : Fragment(), GoogleMap.OnPolylineClickListener {

    var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null


    var polyline: Polyline? = null
    var polylineOptions : PolylineOptions? = null

    lateinit var mapLineInterface: MapLineInterface

    interface MapLineInterface{
        fun onNewLine(linePoints: List<LatLng>)
    }

    lateinit var binding : FragmentPathLineBinding



    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_path_line, container, false)

        mMapView = binding.map
        binding.reauiredAccuracy.text = "Req. ${questionAndResponse?.question?.question?.map_accuracy?.toFloat()}"

        mMapView?.onCreate(savedInstanceState)
        mMapView?.onResume() // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(activity?.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }


        mMapView?.getMapAsync(OnMapReadyCallback { mMap ->
            googleMap = mMap

            polylineOptions = PolylineOptions()
            polyline = googleMap?.addPolyline(polylineOptions)
            polyline?.isVisible = true
            polyline?.tag = "alpha"
            googleMap?.isMyLocationEnabled = true


            SingleShotLocationProvider.requestSingleUpdate(
                requireActivity(),
                object :
                    SingleShotLocationProvider.LocationCallback {
                    override fun onNewLocationAvailable(location: SingleShotLocationProvider.GPSCoordinates?) {
                        val currentLocation = LatLng(
                            location?.latitude?.toDouble()!!,
                            location.longitude.toDouble()
                        )

                        if(questionAndResponse?.response != null){
                            val points = (questionAndResponse.response?.value as ResponseValue
                            .PathLine).points?.map { LatLng(it.latitute, it.longitude) }
                            mapLineInterface.onNewLine(points!!)
                            polyline?.points = points
                            refocusMapCameraOn(points!![0])


                        }else{
                            refocusMapCameraOn(currentLocation)
                        }

                    }

                })
        })


        val startBtn = binding.startButton
        startBtn.setOnClickListener {


            when(polyline?.points?.size!!){
                0 -> {
                    animateMappingImage()
                    markCurrentLocation()
                    binding.mappingIndicator.visibility = View.VISIBLE

                }

                1 -> {
                    markCurrentLocation()
                    binding.mappingIndicator.apply {
                        animation = null
                        visibility = View.GONE
                    }


                }

                2 -> {
                    polyline?.points = listOf()
                    markCurrentLocation()
                    animateMappingImage()
                    binding.mappingIndicator.visibility = View.VISIBLE


                }
            }

        }

        return binding.root
    }

    fun refocusMapCameraOn(location : LatLng){

        val cameraPosition =
            CameraPosition.Builder().target(location).zoom(22f).build()
        googleMap!!.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                cameraPosition
            )
        )

    }


    fun showConfirmationDialog(){

        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {


                    DialogInterface.BUTTON_POSITIVE -> {

                        mapLineInterface.onNewLine(polyline!!.points)
                        Toast.makeText(
                            requireActivity(),
                            R.string.msg_pathline_success,
                            Toast.LENGTH_LONG
                        ).show()
                    }


                    DialogInterface.BUTTON_NEGATIVE -> {
                    }


                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        val coordinates = polyline!!.points
        builder.setMessage(getString(R.string.msg_confirm_pathline) +
                " FROM Point(${coordinates[0].longitude} long; ${coordinates[0].latitude} lat) TO " +
                " Point(${coordinates[1].longitude} long; ${coordinates[1].latitude} lat)")
            .setPositiveButton(getString(
                R.string.yes), dialogClickListener)
            .setNegativeButton(getString(R.string.No), dialogClickListener).show()


    }
    fun animateMappingImage(){
        val animation: Animation = AlphaAnimation(1f, 0f)

        animation.duration = 1000

        animation.interpolator = LinearInterpolator()
        animation.repeatCount = Animation.INFINITE

        animation.repeatMode = Animation.REVERSE

        binding.mappingIndicator.apply {
            startAnimation(animation)
        }

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

                    val nonNUllAaccuracy = questionAndResponse?.question?.question?.map_accuracy?.toFloat()?:25.0F
                    if (location.accuracy <= nonNUllAaccuracy) {

                        if (polylineOptions != null) {
                            val points: MutableList<LatLng> = polyline!!.points
                            points.add(currentLocation)
                            polyline?.points = points

                            if(points.size == 2){
                                showConfirmationDialog()
                            }
                        }
                    } else {
                        Toast.makeText(requireActivity(), "Wrong accuracy !", Toast.LENGTH_SHORT)
                            .show()
                    }

                }

            })
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

    override fun onPolylineClick(p0: Polyline?) {
        p0?.points = listOf()
    }
}