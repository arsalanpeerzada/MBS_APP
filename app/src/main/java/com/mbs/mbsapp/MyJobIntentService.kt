package com.mbs.mbsapp

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService

class MyJobIntentService : JobIntentService() {

    companion object {
        private const val JOB_ID = 1000

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, MyJobIntentService::class.java, JOB_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        // Make your API call here
        // You can use libraries like Retrofit or Volley to make the API request
        // Handle the API response accordingly
    }
}