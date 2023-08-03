package com.mbs.mbsapp.Utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast

class InternetConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Constants.isInternetConnected(context)) {
            Toast.makeText(context, "You are Online Now", Toast.LENGTH_SHORT).show()
        } else {
            // No internet connection
            // You can handle this case if needed
            Toast.makeText(context, "You are Disconnected", Toast.LENGTH_SHORT).show()

        }
    }

//    private fun isInternetConnected(context: Context): Boolean {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val network = connectivityManager.activeNetwork ?: return false
//            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
//            return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//        } else {
//            val activeNetworkInfo = connectivityManager.activeNetworkInfo
//            return activeNetworkInfo != null && activeNetworkInfo.isConnected
//        }
//    }
}
