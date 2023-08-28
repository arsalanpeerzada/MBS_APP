package com.mbs.mbsapp.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class Constants {

    companion object{
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
                    NetworkCapabilities.NET_CAPABILITY_INTERNET)
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                return activeNetworkInfo != null && activeNetworkInfo.isConnected
            }
        }
    }

}