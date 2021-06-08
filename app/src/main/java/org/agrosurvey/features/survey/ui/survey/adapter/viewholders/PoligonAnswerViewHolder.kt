package org.agrosurvey.features.survey.ui.survey.adapter.viewholders

import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.agrosurvey.databinding.ItemPoligonBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.agrosurvey.survey.data.question.QuestionAndType
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon

class PoligonAnswerViewHolder(val context: Context, private val binding: ItemPoligonBinding):
    BaseViewHolder(binding.root)  , GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener{
    private val viewModel =
        PoligonAnswerViewModel()

    lateinit var questionInterface: QuestionInterface
    lateinit var mMap: MapView
    var locationManager: LocationManager? = null

    val geoPoints = ArrayList<GeoPoint>();
    val polygon = Polygon();

    interface QuestionInterface {
        fun onChange(question: QuestionAndType, response : Any)
    }

    override fun bind(questionAndResponse: QuestionAndResponse){
        viewModel.bind(questionAndResponse)
        binding.viewModel = viewModel
        binding.lifecycleOwner = context as LifecycleOwner
        mMap = binding.mapView
        binding.action.bringToFront()


        binding.action.setOnClickListener{
            if( binding.action.text=="START"){
                getLocation()
                binding.action.text="STOP"
            }
            if( binding.action.text=="STOP"){
                questionInterface.onChange(questionAndResponse.question!!, geoPoints)
            }


        }


        mMap.setTileSource(TileSourceFactory.MAPNIK)
        Configuration.getInstance()
            .load(context, context.getSharedPreferences("survey_app",
                AppCompatActivity.MODE_PRIVATE
            ))

        val controller = mMap.controller
        val mapPoint = GeoPoint(6.465422, 3.406448)

        controller.setZoom(8)
        controller.animateTo(mapPoint)


        binding.executePendingBindings()
    }



    override fun onConnected(p0: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }

    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            drawPoligon(location.latitude,location.longitude)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    fun drawPoligon(latidude: Double, longitude: Double ){
        geoPoints.add(GeoPoint(latidude, longitude))
        polygon.fillPaint.color = Color.parseColor("#1EFFE70E")
        polygon.setPoints(geoPoints);
        polygon.title = "area covered"
        mMap.overlays.add(polygon)
    }

    fun getLocation() {

        locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager?

        try {
            // Request location updates
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0f,
                locationListener
            )
        } catch (ex: SecurityException) {
            Log.e("myTag", ex.toString())
        }
    }
}
