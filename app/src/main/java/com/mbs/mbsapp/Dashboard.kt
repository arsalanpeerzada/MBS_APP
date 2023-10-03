package com.mbs.mbsapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
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
import com.mbs.mbsapp.Utils.Constants
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityDashboardBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mbsDatabase = MBSDatabase.getInstance(this)!!
        tinydb = TinyDB(this)

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
        var activityLogid = activityLog[activityLog.size - 1].id!!
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


        val workManager = WorkManager.getInstance(applicationContext)
        val workRequest = createWorkRequest()

        workManager.enqueue(workRequest)

        // Observe the work status if needed
        workManager.getWorkInfoByIdLiveData(workRequest.id)
            .observe(this, Observer { workInfo: WorkInfo? ->
                if (workInfo != null) {
                    when (workInfo.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            // Work has been successfully completed
                        }

                        WorkInfo.State.FAILED -> {
                            // Work failed (e.g., due to no internet connectivity)
                        }

                        else -> {
                            // Work is still in progress
                        }
                    }
                }
            })

    }

    private fun updateproducts(activityLogid: Int) {
        var products = mbsDatabase.getMBSData().getProductStocks(campaignid, activitydetailID)




        if (products.size > 0) {
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
        var ba_list = mbsDatabase.getMBSData().getBA(activitydetailID, campaignid, activitydetailID)


//        var isPitchCompleted = 0
//        for (item in ba_list) {
//            if (isPitchCompleted != 0) {
//                isPitchCompleted++
//            }
//        }

        if (ba_list.size == 0) {
            mbsDatabase.getMBSData().updateBA(1, activityLogid)
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
                if (item.answer != "0") {
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
            var data = Constants.getlocation(this@Dashboard, apiInterface)
            handler.postDelayed(this, 1 * 60 * 1000) // 10 minutes in milliseconds
        }
    }

    fun createWorkRequest(): OneTimeWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        return OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setConstraints(constraints)
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .addTag("api_work")
            .build()
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


}