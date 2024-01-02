package com.mbs.mbsapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.inksy.Database.MBSDatabase
import com.inksy.Remote.APIClient
import com.inksy.Remote.APIInterface
import com.mbs.mbsapp.Model.ActivitySubmitModel
import com.mbs.mbsapp.Utils.Constants
import com.mbs.mbsapp.Utils.FileUtil
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityDashboardBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.concurrent.TimeUnit

class Dashboard : AppCompatActivity() {

    lateinit var binding: ActivityDashboardBinding
    lateinit var mbsDatabase: MBSDatabase
    lateinit var tinydb: TinyDB
    var campaignid: Int = 0
    var brandId: Int = 0
    var locationId: Int = 0
    var cityId: Int = 0
    var storeId: Int = 0
    var activitydetailID: Int = 0
    var handler: Handler = Handler()
    var apiInterface: APIInterface = APIClient.createService(APIInterface::class.java)
    var activityLogid: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mbsDatabase = MBSDatabase.getInstance(this)!!
        tinydb = TinyDB(this)
        Glide.with(this).asGif().load(R.drawable.loading).into(binding.loading)
        if (Constants.isInternetConnected(this@Dashboard)) {

        } else {
            binding.logout.visibility = View.GONE
            binding.EndActivity.isEnabled = false
        }
        campaignid = tinydb.getInt("campaignId")
        brandId = tinydb.getInt("brandId")
        locationId = tinydb.getInt("locationid")
        cityId = tinydb.getInt("cityId")
        storeId = tinydb.getInt("storeId")


        var time = tinydb.getString("time")
        var activityName = tinydb.getString("activityName")
        var storename = tinydb.getString("storeName")

        binding.textView7.setText("Activity Started at $time")
        var getBrand = mbsDatabase.getMBSData().getBrandByID(brandId)
        var getCity = mbsDatabase.getMBSData().getCityById(cityId)
        var getLocation = mbsDatabase.getMBSData().getLocationByID(locationId)
        activitydetailID = tinydb.getInt("activitydetailid")

        if (storename?.isNotEmpty() == true) {
            binding.storeName.setText(storename)
            binding.storee.visibility = View.VISIBLE
        } else binding.storee.visibility = View.GONE

        if (getBrand.brandPrimaryColor != null)
            if (getBrand.brandPrimaryColor != "") {
                binding.view.setBackgroundColor(Color.parseColor(getBrand.brandPrimaryColor))
            }

        Glide.with(this)
            .load(Constants.baseURLforPicture + getBrand.picturepath + getBrand.pictureName)
            .into(binding.dashboardImage)

        binding.cityName.text = getCity.cityName
        binding.locationName.text = getLocation.locationName
        binding.textView5.text = activityName

        var activityLog = mbsDatabase.getMBSData().getactivityLogs(campaignid)

        activityLogid = activityLog[activityLog.size - 1].id!!


        var datamedia = mbsDatabase.getMBSData().getmediabyID(activityLogid)
        updateQuestions(activityLogid)
        updateproducts(activityLogid)
        updateBA(activityLogid)
        updateLocation(activityLogid)




        binding.dashboardImage.setOnClickListener {
            var data = mbsDatabase.getMBSData().getBAaa()
            var data2 = data
        }

        binding.questionnaire.setOnClickListener {
            val intent = Intent(this, QuestionnaireActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);

        }

        binding.campaign.setOnClickListener {
            val intent = Intent(this, CampaignActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
        }

        binding.stock.setOnClickListener {
            val intent = Intent(this, Stock_Gift_CountActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);

        }

        binding.store.setOnClickListener {
            val intent = Intent(this, StorePictureActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);

        }

        binding.BAPitch.setOnClickListener {
            val intent = Intent(this, BAPitch::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
        }

        binding.EndActivity.setOnClickListener {

            if (activityLog[activityLog.size - 1].startActivityTasksCompleted == 1 && activityLog[activityLog.size - 1].isQuestionnaireCompleted == 1 && activityLog[activityLog.size - 1].stockEntryCompleted == 1 && activityLog[activityLog.size - 1].storePictureCompleted == 1 && activityLog[activityLog.size - 1].baChecklistCompleted == 1) {

                if (Constants.isInternetConnected(this@Dashboard)) {
                    val intent = Intent(this, EndActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.left, R.anim.left2);
                } else Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@Dashboard, "Please Complete All Tasks", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.logout.setOnClickListener {

            if (Constants.isInternetConnected(this@Dashboard)) {

                GlobalScope.launch {
                    mbsDatabase.getMBSData().deleteAllUsers()
                    mbsDatabase.getMBSData().deleteAllActivityMasters()
                    mbsDatabase.getMBSData().deleteAllActivityDetails()
                    mbsDatabase.getMBSData().deleteAllBrands()
                    mbsDatabase.getMBSData().deleteAllCampaigns()
                    mbsDatabase.getMBSData().deleteAllCities()
                    mbsDatabase.getMBSData().deleteAllLocations()
                    mbsDatabase.getMBSData().deleteAllStores()
                    mbsDatabase.getMBSData().deleteAllQuestionnaires()
                    mbsDatabase.getMBSData().deleteAllQuestions()
                    mbsDatabase.getMBSData().deleteQuestionSection()
                    mbsDatabase.getMBSData().deleteActivityLogs()
                    mbsDatabase.getMBSData().deleteProducts()
                    mbsDatabase.getMBSData().deleteAdetail()
                    mbsDatabase.getMBSData().deleteAMaster()
                    mbsDatabase.getMBSData().deletemedia()
                    mbsDatabase.getMBSData().deleteProductStocks()
                    mbsDatabase.getMBSData().deletebapitches()
                    mbsDatabase.getMBSData().deletebrandambbassadors()
                    mbsDatabase.getMBSData().deleteCampaignChannel()

                }

                var tinyDB = TinyDB(this)
                tinyDB.clear();
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.left, R.anim.left2);
                this.finish()
            } else Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()

        }

        handler.post(apiRunnable);




        binding.refresh.setOnClickListener {

            binding.refresh.visibility = View.GONE
            binding.loading.visibility = View.VISIBLE

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                // Your code to be executed after the delay
                // This is where you can perform actions after the delay has finished.
                // Add your logic here.

                binding.refresh.visibility = View.VISIBLE
                binding.loading.visibility = View.GONE
            }, 60000)

            val workManager = WorkManager.getInstance(applicationContext)
            createWorkRequest()
        }

    }

    private fun updateproducts(activityLogid: Int) {
        var products = mbsDatabase.getMBSData().getProductStocksbyID(activitydetailID)


        var productCount = 0
        for (item in products) {
            if (item.count != 0) {
                productCount++
            }
        }
        if (productCount == products.size) {
            binding.stockCount.text = "Completed"
            mbsDatabase.getMBSData().updateStockPicture(1, activityLogid)
        }


    }

    private fun updateLocation(activityLogid: Int) {
        var location =
            mbsDatabase.getMBSData().getmedia(activityLogid, Constants.store_location_pictures_num)

        //  mbsDatabase.getMBSData().updateStorePicture(1, activityLogid)

        if (location.size > 0) {
            binding.locationCount.text = "Completed"
            mbsDatabase.getMBSData().updateStorePicture(1, activityLogid)
        }

    }


    private fun updateBA(activityLogid: Int) {
        var ba_list = mbsDatabase.getMBSData().getBApitchesNew(activityLogid)

        mbsDatabase.getMBSData().updateBA(1, activityLogid)
        var isPitchCompleted = 0
        if (ba_list.size > 0) {
            binding.BAPitchPending.text = "BA Pitch Count : ${ba_list.size}"
        }


    }


    fun updateQuestions(activityLogid: Int) {

        var getquestionnaireid = mbsDatabase.getMBSData().getQuestionnaire(campaignid)
        var questions = mbsDatabase.getMBSData().getanswersbyID(
            activitydetailID, getquestionnaireid[0].id!!, activityLogid
        )


        var count = 0
        if (questions.size > 0) {

            for (item in questions) {
                if (item.answer != "") {
                    count++
                }
            }
            binding.questionsCount.text = "$count/${questions.size} Answered"

            if (count == questions.size) {
                mbsDatabase.getMBSData().updateQuestionnaire(1, activityLogid)
            }
        } else {
            var rawquestions = mbsDatabase.getMBSData().getQuestion(getquestionnaireid[0].id!!)
            binding.questionsCount.text = "$count/${rawquestions.size} Answered"
        }
    }

    var apiRunnable: Runnable = object : Runnable {
        override fun run() {
            var data = Constants.getlocation(this@Dashboard, apiInterface, activityLogid)
            handler.postDelayed(this, 120000) // 1 minutes in milliseconds
        }
    }


    fun createWorkRequest() {

        var activitylogid = mbsDatabase.getMBSData().getUnSyncActivityLogID()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        if (activitylogid.size > 0) {
            val workRequest = OneTimeWorkRequest.Builder(FullWorker::class.java)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(this@Dashboard).enqueue(workRequest)
        } else {
            val workRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(this@Dashboard).enqueue(workRequest)
        }

    }

    private fun calculateInitialDelay(): Long {
        val currentTimeMillis = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTimeMillis
        calendar.set(Calendar.HOUR_OF_DAY, 20) // 8 PM
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val startTimeMillis = calendar.timeInMillis
        val endTimeMillis = startTimeMillis + 2 * 60 * 60 * 1000 // 2 hours (8 PM to 10 PM)

        return if (currentTimeMillis in startTimeMillis until endTimeMillis) {
            // Within the 8 PM to 10 PM window, schedule the first execution in 20 minutes
            startTimeMillis + 20 * 60 * 1000 - currentTimeMillis
        } else {
            // Outside the window, schedule for the next day at 8 PM
            startTimeMillis + 24 * 60 * 60 * 1000 - currentTimeMillis
        }
    }

    /*fun SubmitMedia() {
        mbsDatabase = MBSDatabase.getInstance(this@Dashboard)!!
        var data = mbsDatabase.getMBSData().getAllMediaForSync(0)

        var count = 0
        var tinyDB = TinyDB(this@Dashboard)
        var stoken = tinyDB.getString("token")!!
        var token = "Bearer $stoken"

        for (item in data) {
            count++
            var activityLog = mbsDatabase.getMBSData().getactivitylogsById(item.mid!!)
            var newActivityLogId = activityLog.serverid


            val activity_log_id =
                RequestBody.create(MultipartBody.FORM, newActivityLogId.toString())
            val form_id = RequestBody.create(MultipartBody.FORM, item.form_id.toString())
            val form_name = RequestBody.create(MultipartBody.FORM, item.form_name!!)
            val data_id = RequestBody.create(MultipartBody.FORM, item.data_id.toString())
            val data_name = RequestBody.create(MultipartBody.FORM, item.data_name!!)
            val mobile_media_id = RequestBody.create(MultipartBody.FORM, item.mid!!.toString())


            var media = item.media_name
            var uri = Uri.parse(media)
            var file = FileUtil.from(this@Dashboard, uri)

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
                    Toast.makeText(this@Dashboard, "Error in Media", Toast.LENGTH_SHORT)
                }
            })
        }

        if (count == data.size) {
        }
    }*/

    override fun onBackPressed() {

    }


}