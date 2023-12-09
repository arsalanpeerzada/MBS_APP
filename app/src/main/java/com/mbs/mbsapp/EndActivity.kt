package com.mbs.mbsapp

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.inksy.Database.MBSDatabase
import com.inksy.Remote.APIClient
import com.inksy.Remote.APIInterface
import com.mbs.mbsapp.Database.Entities.MediaEntity
import com.mbs.mbsapp.Dialog.TwoButtonDialog
import com.mbs.mbsapp.Interfaces.OnDialogClickListener
import com.mbs.mbsapp.Model.ActivitySubmitModel
import com.mbs.mbsapp.Utils.Constants
import com.mbs.mbsapp.Utils.FileUtil
import com.mbs.mbsapp.Utils.Permissions
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityEndBinding
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
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EndActivity : AppCompatActivity() {

    private var activityMasterId: Int = 0
    lateinit var binding: ActivityEndBinding
    lateinit var mbsDatabase: MBSDatabase
    lateinit var tinyDB: TinyDB
    private val Selfie = 10
    private val Team = 20
    private val Location = 30

    private lateinit var cameraUri: Uri
    var selfiecount = 0
    var teamcount = 0
    var locationcount = 0
    lateinit var URI_Selfie: Uri
    lateinit var URI_TEAM: Uri
    lateinit var URI_LOCATION: Uri
    var mediacount: Int = 0
    var activitylogid = 0
    var campaignID = 0
    var apiInterface: APIInterface = APIClient.createService(APIInterface::class.java)
    var token = ""
    var activityDetailID = 0
    var cityid = 0
    var locationId = 0
    var storeId = 0
    var currentTime: String = ""
    var currentDate: String = ""
    var newactivityLog = 0
    var syncStarted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.upperlayout.visibility = View.VISIBLE
        binding.syncReport.visibility = View.GONE
        binding.EndActivity.visibility = View.VISIBLE
        binding.EndActivity2.visibility = View.GONE
        tinyDB = TinyDB(this@EndActivity)
        binding.logout.visibility = View.GONE

        Glide.with(this).asGif().load(R.drawable.loader).into(binding.imageView3)

        currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        var stoken = tinyDB?.getString("token")!!
        token = "Bearer $stoken"
        mbsDatabase = MBSDatabase.getInstance(this@EndActivity)!!
        mediacount = mbsDatabase.getMBSData().getmedia().size
        var token = tinyDB.getString("token")
        activityMasterId = tinyDB.getInt("activitymasterid")
        activitylogid = tinyDB.getInt("activityLogID")
        campaignID = tinyDB.getInt("campaignId")
        activityDetailID = tinyDB.getInt("activitydetailid")
        locationId = tinyDB.getInt("locationid")
        cityid = tinyDB.getInt("cityId")
        storeId = tinyDB.getInt("storeId")

        binding.back.setOnClickListener {
            openDialog()
        }

        binding.logout.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            overridePendingTransition(R.anim.left, R.anim.left2);
//            this.finish()
        }

        binding.EndActivity.setOnClickListener {

            if (syncStarted) {

                tinyDB.putString("time", "")
                startActivity(Intent(this@EndActivity, SelectActivity::class.java))
                this@EndActivity.finish()
            } else {
                if (selfiecount == 1 && teamcount == 1 && locationcount == 1) endActivity()
                else {
                    Toast.makeText(
                        this@EndActivity, "Please Select All Picture", Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }

        binding.EndActivity2.setOnClickListener {

            tinyDB.putString("time", "")
            startActivity(Intent(this@EndActivity, SelectActivity::class.java))
            this@EndActivity.finish()

        }

        binding.button.setOnClickListener {
            binding.cardview1.performClick()
        }

        binding.button2.setOnClickListener {
            binding.cardview2.performClick()
        }

        binding.button3.setOnClickListener {
            binding.cardview3.performClick()
        }

        binding.cardview1.setOnClickListener {

            if (!Permissions.Check_CAMERA(this@EndActivity)) {
                Permissions.Request_CAMERA_STORAGE(this@EndActivity, 11, 12)
            } else {
                selfiecount = 0
                val intent = Intent(this, CameraActivity::class.java)
                startActivityForResult(intent, Selfie)
//                dispatchTakePictureIntent(Selfie)
            }


        }

        binding.cardview2.setOnClickListener {

            if (!Permissions.Check_CAMERA(this@EndActivity)) {
                Permissions.Request_CAMERA_STORAGE(this@EndActivity, 11, 12)
            } else {
                teamcount = 0
//                dispatchTakePictureIntent(Team)
                val intent = Intent(this, CameraActivity::class.java)
                startActivityForResult(intent, Team)
            }

        }

        binding.cardview3.setOnClickListener {

            if (!Permissions.Check_CAMERA(this@EndActivity)) {
                Permissions.Request_CAMERA_STORAGE(this@EndActivity, 11, 12)
            } else {
                locationcount = 0
                val intent = Intent(this, CameraActivity::class.java)
                startActivityForResult(intent, Location)
//                dispatchTakePictureIntent(Location)
            }

        }


    }

    fun saveBitmapToGallery(context: Context, bitmap: Bitmap, displayName: String): Uri? {
        val contentResolver: ContentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$displayName.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis() / 1000)
            put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis())
        }

        val imageUri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        imageUri?.let {
            val outputStream: OutputStream? = contentResolver.openOutputStream(it)
            outputStream?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            }
        }

        return imageUri
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val displayName = "image_${System.currentTimeMillis()}"
            if (requestCode == Selfie) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)
                val bitmap: Bitmap? = uriToBitmap(URI)
                binding.imageView5.setImageBitmap(bitmap)
                URI_Selfie = saveBitmapToGallery(this, bitmap!!, displayName)!!


                selfiecount = 1


            }
            if (requestCode == Team) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)
                val bitmap: Bitmap? = uriToBitmap(URI)
                binding.imageView6.setImageBitmap(bitmap)
                URI_TEAM = saveBitmapToGallery(this, bitmap!!, displayName)!!

                teamcount = 1

            }

            if (requestCode == Location) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)
                val bitmap: Bitmap? = uriToBitmap(URI)
                binding.imageView8.setImageBitmap(bitmap)
                URI_LOCATION = saveBitmapToGallery(this, bitmap!!, displayName)!!


                locationcount = 1

            }
        }

    }

    private fun dispatchTakePictureIntent(request: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(takePictureIntent, request)

    }

    fun endActivity() {

        for (i in 1..3) {
            when (i) {
                1 -> {
                    insertIntoDB(URI_Selfie, mediacount)
                    mediacount++
                }

                2 -> {
                    insertIntoDB(URI_TEAM, mediacount)
                    mediacount++
                }

                3 -> {
                    insertIntoDB(URI_LOCATION, mediacount)
                    mediacount++

                }
            }
        }

        // var activitylog = mbsDatabase.getMBSData().getactivityLogs(campaignID)
        var finalUpdate =
            mbsDatabase.getMBSData().updateFinal(1, activitylogid, currentDate, currentTime)
        SubmitData()

    }

    fun insertIntoDB(uri: Uri, mediacount: Int) {
        GlobalScope.launch {
            var mediaEntity = MediaEntity(
                mediacount,
                Constants.end_activity_num,
                Constants.end_activity_name,
                activitylogid,
                0,
                "",
                "picture",
                "",
                uri.toString(),
                "",
                "",
                "",
                "",
                ""
            )
            var data = mbsDatabase.getMBSData().insertMedia(mediaEntity)
        }
    }

    fun SubmitProducts() {
        var products = mbsDatabase.getMBSData().getProductStocksbyID(activitylogid)


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
                            Log.d("MSB", "Products data Inserted")
//                            Toast.makeText(
//                                this@EndActivity,
//                                "Products data Inserted",
//                                Toast.LENGTH_SHORT
//                            ).show()
                            binding.ProductResult.setText("Sync Successfully")
                            binding.ProductResult.setTextColor(resources.getColor(R.color.darkgreen))

                        } else {
//                            Toast.makeText(
//                                this@EndActivity,
//                                "Error in Products",
//                                Toast.LENGTH_SHORT
//                            ).show()
                            binding.transparentLoader.visibility = View.GONE
                            binding.imageView3.visibility = View.GONE
                            binding.ProductResult.setText("Sync UnSuccessful")
                            binding.ProductResult.setTextColor(resources.getColor(R.color.appred))
                        }

                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>, t: Throwable
                ) {
                    // Toast.makeText(this@EndActivity, "Error in Products", Toast.LENGTH_SHORT).show()
                    binding.transparentLoader.visibility = View.GONE
                    binding.imageView3.visibility = View.GONE
                    binding.ProductResult.setText("Sync UnSuccessful")
                    binding.ProductResult.setTextColor(resources.getColor(R.color.appred))

                }

            })
        }
    }

    fun SubmitAudioMedia() {
        var data = mbsDatabase.getMBSData().getBApitchesNew(activitylogid)
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
                        binding.BAPitchesResult.setText("Sync Successfully")
                        binding.BAPitchesResult.setTextColor(resources.getColor(R.color.darkgreen))
                    } else {
                        binding.BAPitchesResult.setText("Sync UnSuccessful")
                        binding.BAPitchesResult.setTextColor(resources.getColor(R.color.appred))
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>, t: Throwable
                ) {
                    // Toast.makeText(this@EndActivity, "Error in Media", Toast.LENGTH_SHORT).show()
                    binding.transparentLoader.visibility = View.GONE
                    binding.imageView3.visibility = View.GONE
                    binding.BAPitchesResult.setText("Sync UnSuccessful")
                    binding.BAPitchesResult.setTextColor(resources.getColor(R.color.appred))

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
            .getanswersbyID(activityDetailID, questionnaireList[0].id!!, activitylogid)

        var data = questiondata
        var count = 0
        var countDone = 0
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
                    var file = FileUtil.from(this@EndActivity, uri)

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
                        countDone++
                        Log.d("MSB", count.toString() + " Answer Media Done")
                        runOnUiThread {
                            binding.AnswerMediaResult.setText("$countDone/${data.size}")
                            binding.AnswerMediaResult.setTextColor(resources.getColor(R.color.darkgreen))
                        }
                    } else {
                        Log.d("MSB", count.toString() + " Answer Media Failed")
                        // Toast.makeText(this@EndActivity, "Error in Media", Toast.LENGTH_SHORT)
                    }
                } else {

                }


            }

        }

        if (count == data.size) {
            var data = mbsDatabase.getMBSData().getmediabyID(activitylogid)
            GlobalScope.launch { SubmitMedia(data) }


        }
    }

    suspend fun SubmitMedia(data: List<MediaEntity>) {

        var count = 0
        var countDone = 0
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
            var file = FileUtil.from(this@EndActivity, uri)

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
                    countDone++
                    Log.d("MSB", count.toString() + "Media Done")
                    runOnUiThread {
                        binding.TotalMediaResult.setText("$countDone/${data.size}")
                        binding.TotalMediaResult.setTextColor(resources.getColor(R.color.darkgreen))
                    }

                    var mobileid = response.body()?.mobile_media_id
                    if (mobileid?.isNotEmpty() == true) mbsDatabase.getMBSData().updateMediaSync(mobileid.toInt(), 1)
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
            binding.transparentLoader.visibility = View.GONE
            binding.imageView3.visibility = View.GONE

            binding.textView13.setText("Here is your sync report, click on End Activity to end the Activity, and try to sync remaining items on first screeen.")
        }
    }

    private suspend fun <T> executeCallAsync(call: Call<T>): Response<T> {
        return kotlinx.coroutines.withContext(Dispatchers.IO) {
            call.execute()
        }
    }


    fun SubmitAnswer() {
        var getquestionnaireid = mbsDatabase.getMBSData().getQuestionnaire(campaignID)
        var answers = mbsDatabase.getMBSData()
            .getanswersbyID(activityDetailID, getquestionnaireid[0].id!!, activitylogid)

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
                        Log.d("MSB", "Answers data Inserted")
//                        Toast.makeText(
//                            this@EndActivity,
//                            "Answers data Inserted",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        binding.AnswersResult.setText("Sync Successfully")
                        binding.AnswersResult.setTextColor(resources.getColor(R.color.darkgreen))
                        SubmitProducts()
                    } else {
//                        Toast.makeText(this@EndActivity, "Error in Answer", Toast.LENGTH_SHORT)
//                            .show()
                        binding.transparentLoader.visibility = View.GONE
                        binding.imageView3.visibility = View.GONE
                        binding.AnswersResult.setText("Sync UnSuccessful")
                        binding.AnswersResult.setTextColor(resources.getColor(R.color.appred))
                    }

                }
            }

            override fun onFailure(
                call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>, t: Throwable
            ) {
                //  Toast.makeText(this@EndActivity, "Error in Answer", Toast.LENGTH_SHORT).show()
                binding.transparentLoader.visibility = View.GONE
                binding.imageView3.visibility = View.GONE
                binding.AnswersResult.setText("Sync UnSuccessful")
                binding.AnswersResult.setTextColor(resources.getColor(R.color.appred))

            }


        })
    }

    fun SubmitData() {

        binding.transparentLoader.visibility = View.VISIBLE
        binding.imageView3.visibility = View.VISIBLE
        binding.syncReport.visibility = View.VISIBLE

        binding.upperlayout.visibility = View.GONE
        binding.syncReport.visibility = View.VISIBLE
        binding.EndActivity.visibility = View.GONE
        binding.EndActivity2.visibility = View.VISIBLE
        binding.textView13.text = "This is your sync report, now click on Finish Activity to Finish."
        syncStarted = true
        mbsDatabase.getMBSData().updateEndActivity(1, activitylogid)
        if (!Constants.isInternetConnected(this@EndActivity)) {
            tinyDB.putString("time", "")
            startActivity(Intent(this@EndActivity, SelectActivity::class.java))
            this@EndActivity.finish()
        } else {
            var datalog = mbsDatabase.getMBSData().getactivityLogs(campaignID)
            var finaldata = datalog[datalog.size - 1]

            apiInterface.SubmitActivity(
                token,
                finaldata.activityId,
                activityDetailID,
                finaldata.brandId,
                finaldata.campaignId,
                cityid,
                locationId,
                storeId,
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
//                        Toast.makeText(
//                            this@EndActivity,
//                            "Initial Data Inserted",
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
                        newactivityLog = response.body()?.data?.activityLogId!!
                        mbsDatabase.getMBSData().updateServerId(activitylogid, newactivityLog)
                        mbsDatabase.getMBSData().updateMedia(newactivityLog, activitylogid)
                        SubmitAnswer()
                        binding.IntialResult.setText("Sync Successfully")
                        binding.IntialResult.setTextColor(resources.getColor(R.color.darkgreen))
                    } else {
//                        Toast.makeText(
//                            this@EndActivity,
//                            "${response.errorBody().toString()}",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        Toast.makeText(
//                            this@EndActivity,
//                            "Error In Initial Data",
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
                        binding.transparentLoader.visibility = View.GONE
                        binding.imageView3.visibility = View.GONE
                        binding.IntialResult.setText("Sync UnSuccessful")
                        binding.IntialResult.setTextColor(resources.getColor(R.color.appred))
                        binding.textView13.setText("Theres some issue with the sync, your data is saved locally, Click on End Activity and sync your data after some time")
                    }


                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>, t: Throwable
                ) {
//                    Toast.makeText(
//                        this@EndActivity,
//                        "${t.message.toString()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    Toast.makeText(this@EndActivity, "Error In Initial Data", Toast.LENGTH_SHORT)
//                        .show()
                    binding.transparentLoader.visibility = View.GONE
                    binding.imageView3.visibility = View.GONE
                    binding.IntialResult.setText("Sync UnSuccessful")
                    binding.IntialResult.setTextColor(resources.getColor(R.color.appred))


                    binding.textView13.setText("Theres some issue with the sync, your data is saved locally, Click on End Activity and sync your data after some time")

                }

            })
        }
    }

    private fun openDialog() {
        val twoButtonDialog: TwoButtonDialog = TwoButtonDialog(true,
            this,
            "MSB APP",
            "Are you sure?, Your unsaved data will be lost",
            getString(android.R.string.yes),
            getString(android.R.string.no),
            object : OnDialogClickListener {
                override fun onDialogClick(callBack: String?) {
                    if (callBack == "Yes") {
                        selfiecount = 0
                        teamcount = 0
                        locationcount = 0
                        val intent = Intent(this@EndActivity, Dashboard::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.right2, R.anim.right);
                        this@EndActivity.finish()
                    } else {
                    }
                }
            })
        twoButtonDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        twoButtonDialog.show()


    }

    fun uriToBitmap(uri: Uri): Bitmap? {
        try {
            // Use content resolver to open the input stream from the URI
            val inputStream = contentResolver.openInputStream(uri)
            // Decode the input stream into a Bitmap
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onBackPressed() {

    }


}
