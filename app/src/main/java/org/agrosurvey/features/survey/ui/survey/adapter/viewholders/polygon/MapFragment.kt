package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.polygon

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.agrosurvey.R
import com.agrosurvey.databinding.FragmentMapBinding
import com.agrosurvey.survey.data.ResponseValue
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import org.agrosurvey.features.survey.ui.survey.adapter.SingleShotLocationProvider


class MapFragment(val questionAndResponse: QuestionAndResponse?) : Fragment(), GoogleMap.OnPolygonClickListener {

    var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null


    var polyline: Polyline? = null
    var polylineOptions : PolylineOptions? = null

    var polygon : Polygon? = null
    var polygonOptions : PolygonOptions? = null

    var isMarking = false
    var isManual = false
    var isOptionChoosen = false

    lateinit var polygonInterface: MapPolygonInterface

    interface MapPolygonInterface{
        fun onNewPolygon(linePoints: List<LatLng>)
    }


    val handler = Handler(Looper.getMainLooper())

    lateinit var binding : FragmentMapBinding



    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)

        mMapView = binding.map
        binding.reauiredAccuracy.text = "Req. ${questionAndResponse?.question?.question?.map_accuracy?.toFloat()?:25.0F}"

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
            binding.startButton.text = "PICK A MODE"


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
                            .PolyGonPlot).points?.map { LatLng(it.latitute, it.longitude) }
                            polygonOptions = PolygonOptions().addAll(points)
                            polygon = googleMap?.addPolygon(polygonOptions)
                            polygon?.isVisible = true
                            polygon?.tag = "alpha"
                            stylePolygon(polygon!!)
                            polygonInterface.onNewPolygon(points!!)
                            refocusMapCameraOn(points!![0])

                        }else{
                            refocusMapCameraOn(currentLocation)
                        }



                    }

                })

        })


        binding.completeManualBtn.setOnClickListener {
            stopMarking()


            if(polyline?.points?.size!! >= 5) {


                polygonOptions = PolygonOptions().addAll(
                    polyline?.points
                )
                polygon = googleMap?.addPolygon(polygonOptions)
                polygon?.isVisible = true
                polygon?.tag = "alpha"
                stylePolygon(polygon!!)
                polygonInterface.onNewPolygon(polygon?.points!!)
            }
            polyline?.points = listOf()
            binding.completeManualBtn.visibility = View.GONE
            isOptionChoosen = false
            binding.startButton.text = "PICK A MODE"




        }
        val startBtn = binding.startButton
        startBtn.setOnClickListener {

            if(isOptionChoosen.not()){
                val mapOptionsDialog =
                    MarkingOptionDialog()
                mapOptionsDialog.showDialog(requireContext(),
                    onAutomaticChosen = {
                        isManual = false
                        Toast.makeText(requireActivity(), "Automatic mode", Toast.LENGTH_SHORT).show()
                    },
                    onManualChosen = {
                        isManual = true
                        Toast.makeText(requireActivity(), "Manual mode", Toast.LENGTH_SHORT).show()

                    })
                isOptionChoosen = true

                polyline?.points = listOf()
                polygon?.isVisible = false
                startBtn.text = "START"

            }else{
                initMarking()
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

    fun initMarking(){


        //Manual option
        if (isManual) {

            if(isMarking){
                markCurrentLocation()
            }else{
                startMarking()
                binding.startButton.text = "MARK"
                binding.completeManualBtn.visibility = View.VISIBLE
            }


        } else {

            //Automatic and going
            if (isMarking) {
                stopMarking()
                handler.removeCallbacksAndMessages(null)


                if(polyline?.points?.size!! >= 5) {
                    polygonOptions = PolygonOptions().addAll(
                        polyline?.points
                    )
                    polygon = googleMap?.addPolygon(polygonOptions)
                    polygon?.isVisible = true
                    polyline?.points = listOf()
                    polygon?.tag = "alpha"
                    stylePolygon(polygon!!)
                    polygonInterface.onNewPolygon(polygon?.points!!)
                    isOptionChoosen = false
                    binding.startButton.text = "PICK A MODE"


                }

            } else {

                //Automatic but not going
                startMarking()
                polygon?.isVisible = false
                binding.startButton.text = "Complete"
                Toast.makeText(requireActivity(), "Marking...", Toast.LENGTH_SHORT).show()
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        markCurrentLocation()
                        handler.postDelayed(this, 1000)
                    }
                }, 1000)
            }
        }
    }

    fun stopMarking(){
        isMarking = false
        binding.startButton.text = "Start"
        binding.mappingIndicator.visibility = View.GONE
        binding.mappingIndicator.apply {
            animation = null
            visibility = View.GONE
        }

    }

    fun startMarking(){
        isMarking = true
        binding.mappingIndicator.apply {
            visibility = View.VISIBLE
        }
        animateMappingImage()
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
                    //refocusMapCameraOn(currentLocation)

                    val nonNUlleAaccuracy = questionAndResponse?.question?.question?.map_accuracy?.toFloat()?:25.0F
                    if (location.accuracy <= nonNUlleAaccuracy) {

                        if (polylineOptions != null) {
                            val points: MutableList<LatLng> = polyline!!.points
                            points.add(currentLocation)
                            polyline?.points = points
                            //Toast.makeText(requireActivity(), "Marked", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireActivity(), "Wrong accuracy !", Toast.LENGTH_SHORT)
                            .show()
                    }

                }

            })
    }



    private val COLOR_BLACK_ARGB = -0x1000000
    private val COLOR_WHITE_ARGB = -0x1
    private val COLOR_GREEN_ARGB = -0xc771c4
    private val COLOR_PURPLE_ARGB = -0x7e387c
    private val POLYGON_STROKE_WIDTH_PX = 8
    private val PATTERN_DASH_LENGTH_PX = 20

    private val DASH: PatternItem = Dash(PATTERN_DASH_LENGTH_PX.toFloat())
    private val PATTERN_GAP_LENGTH_PX = 20
    private val DOT: PatternItem = Dot()
    private val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())

    // Create a stroke pattern of a gap followed by a dash.
    private val PATTERN_POLYGON_ALPHA = listOf(GAP, DASH)


    private fun stylePolygon(polygon: Polygon) {
        // Get the data object stored with the polygon.
        val type = polygon.tag?.toString() ?: ""
        var pattern: List<PatternItem>? = null
        var strokeColor = COLOR_BLACK_ARGB
        var fillColor = COLOR_WHITE_ARGB
        when (type) {
            "alpha" -> {
                // Apply a stroke pattern to render a dashed line, and define colors.
                pattern = PATTERN_POLYGON_ALPHA
                strokeColor = COLOR_GREEN_ARGB
                fillColor = COLOR_PURPLE_ARGB
            }
        }
        polygon.strokePattern = pattern
        polygon.strokeWidth = POLYGON_STROKE_WIDTH_PX.toFloat()
        polygon.strokeColor = strokeColor
        polygon.fillColor = fillColor
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

    override fun onPolygonClick(p0: Polygon?) {
        polygon?.points = listOf()
    }
}