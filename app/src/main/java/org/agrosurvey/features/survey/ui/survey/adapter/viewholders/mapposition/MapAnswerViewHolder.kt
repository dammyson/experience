package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.mapposition

import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.agrosurvey.R
import com.agrosurvey.databinding.ItemMapBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.agrosurvey.survey.data.question.QuestionAndType
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.BaseViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.QuestionInterface
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapAnswerViewHolder(val context: Context, private val binding: ItemMapBinding):
    BaseViewHolder(binding.root)  , GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener{
    private val viewModel =
        MapAnswerViewModel()

    lateinit var questionInterface: QuestionInterface
    lateinit var mMap: MapView
    var locationManager: LocationManager? = null

    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    override fun bind(questionAndResponse: QuestionAndResponse){
        viewModel.bind(questionAndResponse)
        binding.viewModel = viewModel
        binding.lifecycleOwner = context as LifecycleOwner
        getLocation()
        mMap = binding.mapView

        binding.action.bringToFront()
        binding.action.setOnClickListener{
            questionInterface.onChange(questionAndResponse.question!!, GeoPoint(latitude, longitude))
        }

        mMap.setTileSource(TileSourceFactory.MAPNIK)
        Configuration.getInstance()
            .load(context, context.getSharedPreferences("phonebook_app",
                AppCompatActivity.MODE_PRIVATE
            ))

        val controller = mMap.controller
        val mapPoint = GeoPoint(6.465422, 3.406448)

        controller.setZoom(8)
        controller.animateTo(mapPoint)

        val marker = Marker(mMap)
        marker.position = GeoPoint(latitude, longitude);
        marker.icon = ContextCompat.getDrawable(context, R.drawable.ic_menu_mylocation)
        marker.title = "location"
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)

        mMap.overlays.add(marker)
        mMap.invalidate()

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
            longitude = location.longitude;
            latitude  = location.latitude;
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
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
