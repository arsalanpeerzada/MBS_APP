package com.mbs.mbsapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.inksy.Database.MBSDatabase
import com.inksy.Remote.APIClient
import com.inksy.Remote.APIInterface
import com.mbs.mbsapp.Database.Entities.MediaEntity
import com.mbs.mbsapp.Model.ActivitySubmitModel
import com.mbs.mbsapp.Utils.Constants
import com.mbs.mbsapp.Utils.FileUtil
import com.mbs.mbsapp.Utils.TinyDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.security.Permission
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class FullWorker(var context: Context, var workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    lateinit var mbsDatabase: MBSDatabase
    var apiInterface: APIInterface = APIClient.createService(APIInterface::class.java)
    lateinit var tinyDB: TinyDB
    var token = ""
    var newactivityLog = 0
    var campaignID = 0
    var activityDetailID = 0
    var activityLogId = 0
    var activityMasterId = 0
    var currentDate = ""
    override fun doWork(): Result {
        if (Constants.isInternetConnected(context)) {
            // Make your API call here
            // You can use libraries like Retrofit or Volley to make the API request
            // Handle the API response accordingly
            tinyDB = TinyDB(context)
            var stoken = tinyDB.getString("token").toString()
            token = "Bearer $stoken"
            mbsDatabase = MBSDatabase.getInstance(context)!!
            SubmitData()

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

//    fun SubmitMedia() {
//
//        var data = mbsDatabase.getMBSData().getAllMediaForSync(0)
//
//        var count = 0
//        var tinyDB = TinyDB(context)
//
//        var token = token
//
//        for (item in data) {
//            count++
//
//            var newActivityLogId = data[count].new_activity_log_id
//
//
//            val activity_log_id =
//                RequestBody.create(MultipartBody.FORM, newActivityLogId.toString())
//            val form_id = RequestBody.create(MultipartBody.FORM, item.form_id.toString())
//            val form_name = RequestBody.create(MultipartBody.FORM, item.form_name!!)
//            val data_id = RequestBody.create(MultipartBody.FORM, item.data_id.toString())
//            val data_name = RequestBody.create(MultipartBody.FORM, item.data_name!!)
//            val mobile_media_id = RequestBody.create(MultipartBody.FORM, item.mid!!.toString())
//
//
//            var media = item.media_name
//            var uri = Uri.parse(media)
//            var file = FileUtil.from(context, uri)
//
//            val mediaRequestBody = RequestBody.create(".png".toMediaTypeOrNull(), file)
//            apiInterface.SubmitMediaData(
//                token,
//                activity_log_id,
//                form_id,
//                form_name,
//                data_id,
//                data_name,
//                mediaRequestBody,
//                mobile_media_id
//            ).enqueue(object : Callback<APIInterface.ApiResponse<ActivitySubmitModel>> {
//                override fun onResponse(
//                    call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>,
//                    response: Response<APIInterface.ApiResponse<ActivitySubmitModel>>
//                ) {
//                    if (response.isSuccessful) {
//
//                    }
//                }
//
//                override fun onFailure(
//                    call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>, t: Throwable
//                ) {
//                    Toast.makeText(context, "Error in Media", Toast.LENGTH_SHORT)
//                }
//            })
//        }
//
//        if (count == data.size) {
//        }
//    }

    fun SubmitData() {


        var activitylogids = mbsDatabase.getMBSData().getUnSyncActivityLogID()


//        var datalog = mbsDatabase.getMBSData().getactivityLogs(campaignID)
        var finaldata = activitylogids[activitylogids.size - 1]


        campaignID = finaldata.campaignId!!
        activityLogId = finaldata.mid!!
        currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())


        apiInterface.SubmitActivity(
            token,
            finaldata.activityId,
            finaldata.activityId,
            finaldata.brandId,
            finaldata.campaignId,
            finaldata.cityId,
            finaldata.locationId,
            finaldata.storeId,
            finaldata.activityStartDate,
            finaldata.activityStartTime,
            finaldata.deviceLocationLat,
            finaldata.deviceLocationLong,
            finaldata.startActivityTasksCompleted,
            finaldata.isQuestionnaireCompleted,
            finaldata.stockEntryCompleted,
            finaldata.storePictureCompleted,
            finaldata.baChecklistCompleted,
            finaldata.allTaskCompleted,
            finaldata.endActivityTasksCompleted,
            finaldata.activityEndDate,
            finaldata.activityEndTime,
        ).enqueue(object : Callback<APIInterface.ApiResponse<ActivitySubmitModel>> {
            override fun onResponse(
                call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>,
                response: Response<APIInterface.ApiResponse<ActivitySubmitModel>>
            ) {

                if (response.body()?.validation == false) {
//                    Toast.makeText(this@EndActivity, "Initial Data Inserted", Toast.LENGTH_SHORT)
//                        .show()
                    newactivityLog = response.body()?.data?.activityLogId!!
                    mbsDatabase.getMBSData().updateServerId(finaldata.mid!!, newactivityLog)
                    mbsDatabase.getMBSData().updateMedia(newactivityLog, finaldata.mid!!)
                    SubmitAnswer()
                } else {
//                    Toast.makeText(
//                        this@EndActivity,
//                        "${response.errorBody().toString()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    Toast.makeText(this@EndActivity, "Error In Initial Data", Toast.LENGTH_SHORT)
//                        .show()

                }


            }

            override fun onFailure(
                call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>, t: Throwable
            ) {
//                Toast.makeText(
//                    this@EndActivity,
//                    "${t.message.toString()}",
//                    Toast.LENGTH_SHORT
//                ).show()
//                Toast.makeText(this@EndActivity, "Error In Initial Data", Toast.LENGTH_SHORT).show()
//                binding.transparentLoader.visibility = View.GONE
//                binding.imageView3.visibility = View.GONE
            }

        })
    }

    fun SubmitAnswer() {


        var getquestionnaireid = mbsDatabase.getMBSData().getQuestionnaire(campaignID)
        var answers = mbsDatabase.getMBSData()
            .getanswersbyID(activityDetailID, getquestionnaireid[0].id!!, activityLogId)

        var ans = ArrayList<String>()
        var answerComment = ArrayList<String>()
        var questionId = ArrayList<String>()
        var isMediaAttached = ArrayList<String>()
        var attachedMediaCount = ArrayList<String>()
        var submittedBy = ArrayList<String>()

        for (item in answers) {
            ans.add(item.answer.toString())
            answerComment.add(item.answerComment.toString())
            questionId.add(item.question_id.toString())
            isMediaAttached.add(item.isMediaAttached.toString())
            attachedMediaCount.add(item.attachedMediaCount.toString())
            submittedBy.add(item.submittedBy.toString())
        }

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        apiInterface.SubmitAnswers(
            token,
            newactivityLog,
            getquestionnaireid[0].id!!,
            campaignID,
            activityMasterId,
            activityDetailID,
            1,
            currentDate,
            ans,
            questionId,
            answerComment,
            isMediaAttached,
            attachedMediaCount,
            submittedBy
        ).enqueue(object : Callback<APIInterface.ApiResponse<ActivitySubmitModel>> {
            override fun onResponse(
                call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>,
                response: Response<APIInterface.ApiResponse<ActivitySubmitModel>>
            ) {
                if (response.isSuccessful) {

                    if (response.body()?.validation == false) {


                        SubmitProducts()
                    } else {
//                        Toast.makeText(this@EndActivity, "Error in Answer", Toast.LENGTH_SHORT)
//                            .show()
//                        binding.transparentLoader.visibility = View.GONE
//                        binding.imageView3.visibility = View.GONE
                    }

                }
            }

            override fun onFailure(
                call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>, t: Throwable
            ) {
//                Toast.makeText(this@EndActivity, "Error in Answer", Toast.LENGTH_SHORT).show()
//                binding.transparentLoader.visibility = View.GONE
//                binding.imageView3.visibility = View.GONE
            }


        })
    }

    fun SubmitProducts() {
        var products = mbsDatabase.getMBSData().getProductStocksbyID(activityLogId)


        if (products.size == 0) {
            SubmitAudioMedia()

        } else {
            var producutId = ArrayList<String>()
            var producutCount = ArrayList<String>()


            for (item in products) {
                producutId.add(item.productId.toString())
                producutCount.add(item.count.toString())
            }


            apiInterface.SubmitProducts(
                token, newactivityLog, campaignID, producutId, producutCount
            ).enqueue(object : Callback<APIInterface.ApiResponse<ActivitySubmitModel>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>,
                    response: Response<APIInterface.ApiResponse<ActivitySubmitModel>>
                ) {
                    if (response.isSuccessful) {


                        if (response.body()?.validation == false) {
                            SubmitAudioMedia()
//                            Log.d("MSB", "Products data Inserted")
//                            Toast.makeText(
//                                this@EndActivity,
//                                "Products data Inserted",
//                                Toast.LENGTH_SHORT
//                            ).show()

                        } else {
//                            Toast.makeText(
//                                this@EndActivity,
//                                "Error in Products",
//                                Toast.LENGTH_SHORT
//                            )
//                                .show()
//                            binding.transparentLoader.visibility = View.GONE
//                            binding.imageView3.visibility = View.GONE
                        }

                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>, t: Throwable
                ) {
//                    Toast.makeText(this@EndActivity, "Error in Products", Toast.LENGTH_SHORT).show()
//                    binding.transparentLoader.visibility = View.GONE
//                    binding.imageView3.visibility = View.GONE

                }

            })
        }
    }

    fun SubmitAudioMedia() {
        var data = mbsDatabase.getMBSData().getBApitchesNew(activityLogId)
        var count = 0
        for (item in data) {
            count++
            val activity_log_id = RequestBody.create(MultipartBody.FORM, newactivityLog.toString())
            val form_id = RequestBody.create(MultipartBody.FORM, Constants.ba_pitch_num.toString())
            val form_name = RequestBody.create(MultipartBody.FORM, Constants.ba_pitch_name)
            val data_id = RequestBody.create(MultipartBody.FORM, item.baId.toString())
            val data_name = RequestBody.create(MultipartBody.FORM, item.ba_name!!)
            val mobile_media_id = RequestBody.create(MultipartBody.FORM, item.mid!!.toString())

            val audioMediaType = "audio/*".toMediaTypeOrNull()
            var audio = item.bapPath
            var audioFile = File(audio)

            val requestBody = object : RequestBody() {
                override fun contentType(): MediaType? {
                    return audioMediaType
                }

                override fun contentLength(): Long {
                    return audioFile.length()
                }

                @Throws(IOException::class)
                override fun writeTo(sink: BufferedSink) {
                    audioFile.source().use { source ->
                        sink.writeAll(source)
                    }
                }
            }

            apiInterface.SubmitMediaDataA(
                token,
                activity_log_id,
                form_id,
                form_name,
                data_id,
                data_name,
                requestBody,
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
//                    Toast.makeText(this@EndActivity, "Error in Media", Toast.LENGTH_SHORT).show()
//                    binding.transparentLoader.visibility = View.GONE
//                    binding.imageView3.visibility = View.GONE

                }
            })
        }

        if (count == data.size) {
            GlobalScope.launch { SubmitMediaAnswer() }
        }
    }

    suspend fun SubmitMediaAnswer() {
        var questionnaireList = mbsDatabase.getMBSData().getQuestionnaire(campaignID)
        var questiondata = mbsDatabase.getMBSData()
            .getanswersbyID(activityDetailID, questionnaireList[0].id!!, activityDetailID)

        var data = questiondata
        var count = 0

        Log.d("MSB", "${data.size} Answers Media")
        for (item in data) {
            count++
            val activity_log_id = RequestBody.create(MultipartBody.FORM, newactivityLog.toString())
            val form_id =
                RequestBody.create(MultipartBody.FORM, Constants.questionnaire_num.toString())
            val form_name = RequestBody.create(MultipartBody.FORM, Constants.questionnaire_name)
            val data_id = RequestBody.create(MultipartBody.FORM, item.question_id.toString())
            val data_name = RequestBody.create(MultipartBody.FORM, "null")
            val mobile_media_id = RequestBody.create(MultipartBody.FORM, item.mid!!.toString())

            for (i in 1..4) {

                var media: String? = ""
                when (i) {
                    1 -> media = item.media1
                    2 -> media = item.media2
                    3 -> media = item.media3
                    4 -> media = item.media4
                }
                if (media != "") {
                    var uri = Uri.parse(media)
                    var file = FileUtil.from(context, uri)

                    val mediaRequestBody = RequestBody.create(".png".toMediaTypeOrNull(), file)
                    var call = apiInterface.SubmitMediaData(
                        token,
                        activity_log_id,
                        form_id,
                        form_name,
                        data_id,
                        data_name,
                        mediaRequestBody,
                        mobile_media_id
                    )

                    val response = executeCallAsync(call)

                    if (response.isSuccessful) {
                        Log.d("MSB", count.toString() + " Answer Media Done")
                    } else {
                        Log.d("MSB", count.toString() + " Answer Media Failed")
                        // Toast.makeText(this@EndActivity, "Error in Media", Toast.LENGTH_SHORT)
                    }
                } else {

                }


            }

        }

        if (count == data.size) {
            var data = mbsDatabase.getMBSData().getmediabyID(activityLogId)
            GlobalScope.launch { SubmitMedia(data) }


        }
    }

    private suspend fun <T> executeCallAsync(call: Call<T>): Response<T> {
        return kotlinx.coroutines.withContext(Dispatchers.IO) {
            call.execute()
        }
    }
    suspend fun SubmitMedia(data: List<MediaEntity>) {

        var count = 0
        Log.d("MSB", "${data.size} Media")
        for (item in data) {
            count++
            val activity_log_id = RequestBody.create(MultipartBody.FORM, newactivityLog.toString())
            val form_id = RequestBody.create(MultipartBody.FORM, item.form_id.toString())
            val form_name = RequestBody.create(MultipartBody.FORM, item.form_name!!)
            val data_id = RequestBody.create(MultipartBody.FORM, item.data_id.toString())
            val data_name = RequestBody.create(MultipartBody.FORM, item.data_name!!)
            val mobile_media_id = RequestBody.create(MultipartBody.FORM, item.mid!!.toString())


            var media = item.media_name
            var uri = Uri.parse(media)
            var file = FileUtil.from(context, uri)

            val mediaRequestBody = RequestBody.create(".png".toMediaTypeOrNull(), file)
            var call = apiInterface.SubmitMediaData(
                token,
                activity_log_id,
                form_id,
                form_name,
                data_id,
                data_name,
                mediaRequestBody,
                mobile_media_id
            )

            val response = executeCallAsync(call)

            if (response.isSuccessful) {
                if (response.isSuccessful) {

                    Log.d("MSB", count.toString() + "Media Done")
                    var mobileid = response.body()?.mobile_media_id
                    if (mobileid?.isNotEmpty() == true) mbsDatabase.getMBSData()
                        .updateMediaSync(mobileid.toInt(), 1)
                }
            } else {
                Log.d("MSB", count.toString() + " Media Failed")
                // Toast.makeText(this@EndActivity, "Error in Media", Toast.LENGTH_SHORT)
            }
        }

        if (count == data.size) {
//            tinyDB.putString("time", "")
//            startActivity(Intent(this@EndActivity, SelectActivity::class.java))
//            this@EndActivity.finish()
        }
    }


}