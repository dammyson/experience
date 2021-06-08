package org.agrosurvey.features.survey.ui.survey.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle


object SingleShotLocationProvider {
    // calls back to calling thread, note this is for low grain: if you want higher precision, swap the
    // contents of the else and if. Also be sure to check gps permission/settings are allowed.
    // call usually takes <10ms
    @SuppressLint("MissingPermission")
    fun requestSingleUpdate(context: Context, callback: LocationCallback) {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isNetworkEnabled =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (isNetworkEnabled) {
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_COARSE
            locationManager.requestSingleUpdate(criteria, object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    callback.onNewLocationAvailable(
                        GPSCoordinates(
                            location.latitude,
                            location.longitude,
                            location.accuracy.toDouble()
                        )
                    )
                }

                override fun onStatusChanged(
                    provider: String,
                    status: Int,
                    extras: Bundle
                ) {
                }

                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }, null)
        } else {
            val isGPSEnabled =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (isGPSEnabled) {
                val criteria = Criteria()
                criteria.accuracy = Criteria.ACCURACY_FINE
                locationManager.requestSingleUpdate(criteria, object : LocationListener {
                    override fun onLocationChanged(location: Location) {

                        location.accuracy
                        callback.onNewLocationAvailable(
                            GPSCoordinates(
                                location.latitude,
                                location.longitude,
                                location.accuracy.toDouble()
                            )
                        )
                    }

                    override fun onStatusChanged(
                        provider: String,
                        status: Int,
                        extras: Bundle
                    ) {
                    }

                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }, null)
            }
        }
    }

    interface LocationCallback {
        fun onNewLocationAvailable(location: GPSCoordinates?)
    }

    // consider returning Location instead of this dummy wrapper class
    class GPSCoordinates {
        var longitude = -1f
        var latitude = -1f
        var accuracy = 0f

        constructor(theLatitude: Float, theLongitude: Float, theAccuracy: Float) {
            longitude = theLongitude
            latitude = theLatitude
            accuracy = theAccuracy
        }

        constructor(theLatitude: Double, theLongitude: Double, theAccuracy: Double) {
            longitude = theLongitude.toFloat()
            latitude = theLatitude.toFloat()
            accuracy = theAccuracy.toFloat()
        }
    }
}