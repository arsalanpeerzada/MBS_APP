package com.mbs.mbsapp.Utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.inksy.Database.MBSDatabase
import com.inksy.Remote.APIInterface
import com.mbs.mbsapp.Model.ActivitySubmitModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Constants {

    companion object {
        var baseURL = "https://msb.count-square.com/api/"
        var baseURLforPicture = "https://msb.count-square.com"


        var BRANDS = 1
        var CAMPAIGN = 2
        var CITY = 3
        var LOCATION = 4
        var STORE = 5

        var activity_start_num = 1
        var questionnaire_num = 2
        var store_location_pictures_num = 3
        var ba_pitch_num = 4
        var end_activity_num = 5

        var activity_start_name = "activity_start"
        var questionnaire_name = "questionnaire"
        var store_location_pictures_name = "store_location_pictures"
        var ba_pitch_name = "ba_pitch"
        var end_activity_name = "end_activity"


        fun isInternetConnected(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return false
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                return networkCapabilities != null && networkCapabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                )
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                return activeNetworkInfo != null && activeNetworkInfo.isConnected
            }
        }


        fun getlocation(
            context: Context,
            apiInterface: APIInterface,
            activityLogId: Int
        ): ArrayList<String> {

            var fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)
            var data = ArrayList<String>()
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {


            } else {
                val locationRequest = LocationRequest.create().apply {
                    interval = 10000 // Update interval in milliseconds
                    fastestInterval = 5000 // Fastest update interval
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        if (locationResult.lastLocation != null) {
                            val location: Location = locationResult.lastLocation!!
                            // Handle the location here
                            val latitude = location.latitude.toString()
                            val longitude = location.longitude.toString()
                            // Do something with latitude and longitude

                            if (latitude.isNotEmpty() && longitude.isNotEmpty()) {


                                data.add(latitude)
                                data.add(longitude)

                                var mbsDatabase = MBSDatabase.getInstance(context)!!

                                if (activityLogId != 0) {
                                    var update = mbsDatabase.getMBSData().updateLocation(
                                        latitude.toString(),
                                        longitude.toString(),
                                        activityLogId
                                    )
                                }
                                var tinyDB = TinyDB(context)
                                var token = tinyDB.getString("token")
                                val finaltoken = "Bearer $token"

                                apiInterface.SubmitLocationPeriodically(
                                    finaltoken,
                                    data[0],
                                    data[1]
                                )
                                    .enqueue(object :
                                        Callback<APIInterface.ApiResponse<ActivitySubmitModel>> {
                                        override fun onResponse(
                                            call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>,
                                            response: Response<APIInterface.ApiResponse<ActivitySubmitModel>>
                                        ) {
                                            Log.d("LiveLocation", response.message());
                                        }

                                        override fun onFailure(
                                            call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>,
                                            t: Throwable
                                        ) {
                                            Log.d("LiveLocation", t.message.toString());
                                        }

                                    })

                            }
                        }
                    }
                }

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null
                )
            }


            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // Check if the location provider is enabled
            /* if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                 // Get the last known location
                 val lastKnownLocation: Location?

                 if (ActivityCompat.checkSelfPermission(
                         context,
                         Manifest.permission.ACCESS_FINE_LOCATION
                     ) != PackageManager.PERMISSION_GRANTED &&
                     ActivityCompat.checkSelfPermission(
                         context, Manifest.permission.ACCESS_COARSE_LOCATION
                     ) != PackageManager.PERMISSION_GRANTED
                 ) {

                 } else {
                     lastKnownLocation =
                         locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)


                     // Check if the location is available
                     if (lastKnownLocation != null) {

                         var latitude = lastKnownLocation.latitude.toString()
                         var longitude = lastKnownLocation.longitude.toString()


                         // Now you have the latitude and longitude
                     } else {
                         // Location data not available
                     }
                 }

             }*/

            return data;
        }
    }

}