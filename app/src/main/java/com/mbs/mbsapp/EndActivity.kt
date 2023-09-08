package com.mbs.mbsapp

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.util.Util
import com.inksy.Database.MBSDatabase
import com.inksy.Remote.APIClient
import com.inksy.Remote.APIInterface
import com.mbs.mbsapp.Database.Entities.MediaEntity
import com.mbs.mbsapp.Dialog.TwoButtonDialog
import com.mbs.mbsapp.Interfaces.OnDialogClickListener
import com.mbs.mbsapp.Model.ActivitySubmitModel
import com.mbs.mbsapp.Model.AnswerModel
import com.mbs.mbsapp.Utils.Constants
import com.mbs.mbsapp.Utils.FileUtil
import com.mbs.mbsapp.Utils.Permissions
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityEndBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.OutputStream
import java.net.URI
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tinyDB = TinyDB(this@EndActivity)


        currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        var stoken = tinyDB?.getString("token")!!
        token = "Bearer $stoken"
        mbsDatabase = MBSDatabase.getInstance(this@EndActivity)!!
        mediacount = mbsDatabase.getMBSData().getmedia().size
        var token = tinyDB.getString("token")
        binding.back.setOnClickListener {


        }
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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }

        binding.EndActivity.setOnClickListener {

            if (selfiecount == 1 && teamcount == 1 && locationcount == 1)
                endActivity()
            else {
                Toast.makeText(
                    this@EndActivity,
                    "Please Select All Picture",
                    Toast.LENGTH_SHORT
                ).show()
            }

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

            if (!Permissions.Check_CAMERA(this@EndActivity) || !Permissions.Check_STORAGE(
                    this@EndActivity
                )
            ) {
                Permissions.Request_CAMERA_STORAGE(this@EndActivity, 11)
            } else {
                selfiecount = 0
                dispatchTakePictureIntent(Selfie)
            }


        }

        binding.cardview2.setOnClickListener {

            if (!Permissions.Check_CAMERA(this@EndActivity) || !Permissions.Check_STORAGE(
                    this@EndActivity
                )
            ) {
                Permissions.Request_CAMERA_STORAGE(this@EndActivity, 11)
            } else {
                teamcount = 0
                dispatchTakePictureIntent(Team)
            }

        }

        binding.cardview3.setOnClickListener {

            if (!Permissions.Check_CAMERA(this@EndActivity) || !Permissions.Check_STORAGE(
                    this@EndActivity
                )
            ) {
                Permissions.Request_CAMERA_STORAGE(this@EndActivity, 11)
            } else {
                locationcount = 0
                dispatchTakePictureIntent(Location)
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
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.imageView5.setImageBitmap(imageBitmap)
                URI_Selfie = saveBitmapToGallery(this, imageBitmap!!, displayName)!!
                selfiecount = 1


            }
            if (requestCode == Team) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.imageView6.setImageBitmap(imageBitmap)
                URI_TEAM = saveBitmapToGallery(this, imageBitmap!!, displayName)!!
                teamcount = 1

            }

            if (requestCode == Location) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.imageView8.setImageBitmap(imageBitmap)
                URI_LOCATION = saveBitmapToGallery(this, imageBitmap!!, displayName)!!
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

        var activitylog = mbsDatabase.getMBSData().getactivityLogs(campaignID)
        var finalUpdate =
            mbsDatabase.getMBSData()
                .updateFinal(1, activitylogid, currentDate, currentTime)
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
            )
            var data = mbsDatabase.getMBSData().insertMedia(mediaEntity)
        }
    }

    fun SubmitProducts() {
        var products = mbsDatabase.getMBSData().getProductStocks(campaignID, activityDetailID)

        var producutId = ArrayList<String>()
        var producutCount = ArrayList<String>()

        for (item in products) {
            producutId.add(item.productId.toString())
            producutCount.add(item.count.toString())
        }


        apiInterface.SubmitProducts(
            token,
            newactivityLog,
            campaignID,
            producutId,
            producutCount
        ).enqueue(object : Callback<APIInterface.ApiResponse<ActivitySubmitModel>> {
            override fun onResponse(
                call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>,
                response: Response<APIInterface.ApiResponse<ActivitySubmitModel>>
            ) {
                if (response.isSuccessful) {

                }
            }

            override fun onFailure(
                call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>,
                t: Throwable
            ) {
                Toast.makeText(this@EndActivity, "Error in Products", Toast.LENGTH_SHORT)
            }

        })
    }

    fun SubmitMedia() {
        var data = mbsDatabase.getMBSData().getmediabyID(activitylogid)
        var count = 0
        for (item in data) {
            count++
            val activity_log_id = RequestBody.create(MultipartBody.FORM, newactivityLog.toString())
            val form_id = RequestBody.create(MultipartBody.FORM, item.form_id.toString())
            val form_name = RequestBody.create(MultipartBody.FORM, item.form_name!!)
            val data_id = RequestBody.create(MultipartBody.FORM, item.data_id.toString())
            val data_name = RequestBody.create(MultipartBody.FORM, item.data_name!!)

            var media = item.media_name
            var uri = Uri.parse(media)
            var file = FileUtil.from(this@EndActivity, uri)

            val mediaRequestBody = RequestBody.create(".png".toMediaTypeOrNull(), file)
            apiInterface.SubmitMediaData(
                token,
                activity_log_id,
                form_id,
                form_name, data_id, data_name, mediaRequestBody
            ).enqueue(object : Callback<APIInterface.ApiResponse<ActivitySubmitModel>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>,
                    response: Response<APIInterface.ApiResponse<ActivitySubmitModel>>
                ) {
                    if (response.isSuccessful) {

                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@EndActivity, "Error in Media", Toast.LENGTH_SHORT)
                }
            })
        }

        if (count == data.size) {
            tinyDB.putString("time", "")
            startActivity(Intent(this@EndActivity, SelectActivity::class.java))
            this@EndActivity.finish()
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
                    Log.d("MSB", "data Inserted")
                }
            }

            override fun onFailure(
                call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>,
                t: Throwable
            ) {
                Toast.makeText(this@EndActivity, "Error in Answer", Toast.LENGTH_SHORT)
            }


        })
    }

    fun SubmitData() {


        mbsDatabase.getMBSData().updateEndActivity(1, activitylogid)


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
        ).enqueue(object :
            Callback<APIInterface.ApiResponse<ActivitySubmitModel>> {
            override fun onResponse(
                call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>,
                response: Response<APIInterface.ApiResponse<ActivitySubmitModel>>
            ) {
                newactivityLog = response.body()?.data?.activityLogId!!
                SubmitAnswer()
                SubmitMedia()
                SubmitProducts()
            }

            override fun onFailure(
                call: Call<APIInterface.ApiResponse<ActivitySubmitModel>>,
                t: Throwable
            ) {
                Toast.makeText(this@EndActivity, "Error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun openDialog() {
        val twoButtonDialog: TwoButtonDialog = TwoButtonDialog(
            this, "MSB APP",
            "Are you sure?, Your unsaved data will be lost",
            getString(android.R.string.yes),
            getString(android.R.string.no),
            object : OnDialogClickListener {
                override fun onDialogClick(callBack: String?) {
                    if (callBack == "Yes") {
                        selfiecount = 0
                        teamcount = 0
                        locationcount = 0
                        tinyDB.putString("time", "")
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


}