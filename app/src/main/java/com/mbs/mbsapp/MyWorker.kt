package com.mbs.mbsapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.inksy.Database.MBSDatabase
import com.inksy.Remote.APIClient
import com.inksy.Remote.APIInterface
import com.mbs.mbsapp.Model.ActivitySubmitModel
import com.mbs.mbsapp.Utils.Constants
import com.mbs.mbsapp.Utils.FileUtil
import com.mbs.mbsapp.Utils.TinyDB
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.Permission
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MyWorker(var context: Context, var workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    lateinit var mbsDatabase: MBSDatabase
    var apiInterface: APIInterface = APIClient.createService(APIInterface::class.java)
    lateinit var tinyDB: TinyDB
    override fun doWork(): Result {
        if (Constants.isInternetConnected(context)) {
            // Make your API call here
            // You can use libraries like Retrofit or Volley to make the API request
            // Handle the API response accordingly

            return Result.success()
        } else {
            return Result.retry()
        }
    }

//    private fun isConnectedToInternet(): Boolean {
//        // Implement your internet connectivity check here
//        // You can use a connectivity manager or another method of your choice
//        return true // Return true if connected, false otherwise
//    }

    fun SubmitMedia() {
        mbsDatabase = MBSDatabase.getInstance(context)!!
        var data = mbsDatabase.getMBSData().getAllMediaForSync(0)
        var count = 0
        var tinyDB = TinyDB(context)
        var stoken = tinyDB.getString("token")!!
        var token = "Bearer $stoken"

        for (item in data) {
            count++
            val activity_log_id = RequestBody.create(MultipartBody.FORM, "newactivityLog.toString()")
            val form_id = RequestBody.create(MultipartBody.FORM, item.form_id.toString())
            val form_name = RequestBody.create(MultipartBody.FORM, item.form_name!!)
            val data_id = RequestBody.create(MultipartBody.FORM, item.data_id.toString())
            val data_name = RequestBody.create(MultipartBody.FORM, item.data_name!!)
            val mobile_media_id = RequestBody.create(MultipartBody.FORM, item.mid!!.toString())


            var media = item.media_name
            var uri = Uri.parse(media)
            var file = FileUtil.from(context, uri)

            val mediaRequestBody = RequestBody.create(".png".toMediaTypeOrNull(), file)
            apiInterface.SubmitMediaData(
                token,
                activity_log_id,
                form_id,
                form_name,
                data_id,
                data_name,
                mediaRequestBody,
                mobile_media_id
            ).enqueue(object : Callback<APIInterface.ApiResponse<ActivitySubmitModel>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>,
                    response: Response<APIInterface.ApiResponse<ActivitySubmitModel>>
                ) {
                    if (response.isSuccessful) {

                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>, t: Throwable
                ) {
                    Toast.makeText(context, "Error in Media", Toast.LENGTH_SHORT)
                }
            })
        }

        if (count == data.size) {
        }
    }


}