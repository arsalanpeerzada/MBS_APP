package com.mbs.mbsapp.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class Constants {

    companion object{
        var baseURL = "https://msb.count-square.com/api/"


        var BRANDS = 1
        var CAMPAIGN = 2
        var CITY = 3
        var LOCATION = 4
        var STORE = 5

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