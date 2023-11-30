package com.mbs.mbsapp


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.inksy.Database.MBSDatabase
import com.mbs.mbsapp.Database.Entities.CampaignEntity
import com.mbs.mbsapp.Database.Entities.CityEntity
import com.mbs.mbsapp.Database.Entities.LocationEntity
import com.mbs.mbsapp.Database.Entities.StoreEntity
import com.mbs.mbsapp.Database.Entities.UserEntity
import com.mbs.mbsapp.Dialog.OnSearchItemSelected
import com.mbs.mbsapp.Dialog.SearchListItem
import com.mbs.mbsapp.Dialog.SearchableDialog
import com.mbs.mbsapp.Utils.Constants
import com.mbs.mbsapp.Utils.Permissions
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivitySelectBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SelectActivity : AppCompatActivity() {

    lateinit var binding: ActivitySelectBinding
    lateinit var mbsDatabase: MBSDatabase
    var searchableDialog: SearchableDialog? = null
    var SELECTED_BRAND_ID: Int = 0
    var SELECTED_CAMPAIGN_ID: Int = 0
    var SELECTED_CITY_ID: Int = 0
    var SELECTED_LOCATION_ID: Int = 0
    var SELECTED_STORE_ID: Int = 0
    var USER_ID: Int = 0
    var getcampaign: List<CampaignEntity> = ArrayList()
    var getcity: List<CityEntity> = ArrayList()
    var getlocation: List<LocationEntity> = ArrayList()
    var getStore: List<StoreEntity> = ArrayList()

    lateinit var userdata: UserEntity
    lateinit var tinydb: TinyDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mbsDatabase = MBSDatabase.getInstance(this)!!
        tinydb = TinyDB(this)

        userdata = mbsDatabase.getMBSData().getUser()

        Permissions.Check_FINE_LOCATION(this)
        Permissions.Check_CAMERA(this)
        Permissions.Check_STORAGE(this)




        binding.next.setOnClickListener {
            if (getStore.size > 0) {
                var activityName =
                    "${binding.selectActivity.text}"
                tinydb.putString("activityName", activityName)


                if (SELECTED_BRAND_ID != 0 && SELECTED_CAMPAIGN_ID != 0 && SELECTED_CITY_ID != 0 && SELECTED_LOCATION_ID != 0 && SELECTED_STORE_ID != 0) {
                    var activityName =
                        "${binding.selectBrand.text} ${binding.selectActivity.text} Activity"
                    tinydb.putString("activityName", activityName)
                    val intent = Intent(this, ClusterStartActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.left, R.anim.left2);
                } else {
                    Toast.makeText(
                        this@SelectActivity, "Please select all the field", Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                var activityName =
                    "${binding.selectActivity.text}"
                tinydb.putString("activityName", activityName)
                if (SELECTED_BRAND_ID != 0 && SELECTED_CAMPAIGN_ID != 0 && SELECTED_CITY_ID != 0 && SELECTED_LOCATION_ID != 0) {
                    val intent = Intent(this, ClusterStartActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.left, R.anim.left2);
                } else {
                    Toast.makeText(
                        this@SelectActivity, "Please select all the field", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        var brands = mbsDatabase.getMBSData().getAllBrands()

        val searchListItems = ArrayList<SearchListItem>()
        for (i in brands.indices) {

            var searchListItem =
                SearchListItem(brands[i].id!!, brands[i].brandName!!, Constants.BRANDS)
            searchListItems.add(searchListItem);
        }
        searchableDialog = SearchableDialog(this, searchListItems, "Select Activity")

        binding.reset.setOnClickListener {

            searchListItems.clear()

            for (i in brands.indices) {

                var searchListItem =
                    SearchListItem(brands[i].id!!, brands[i].brandName!!, Constants.BRANDS)
                searchListItems.add(searchListItem);
            }
            binding.selectBrand.isEnabled = true
            binding.selectActivity.isEnabled = true
            binding.selectCity.isEnabled = true
            binding.selectLocation.isEnabled = true


            binding.selectBrand.setText("")
            binding.selectActivity.setText("")
            binding.selectCity.setText("")
            binding.selectLocation.setText("")
            binding.selectStore.setText("")


        }

        binding.selectBrand.setOnClickListener {

            searchListItems.clear()
            for (i in brands.indices) {

                var searchListItem =
                    SearchListItem(brands[i].id!!, brands[i].brandName!!, Constants.BRANDS)
                searchListItems.add(searchListItem);
            }
            searchableDialog?.show()
        }

        binding.selectActivity.setOnClickListener {

            searchListItems.clear()
            for (i in getcampaign.indices) {

                var campaignSelected = SearchListItem(
                    getcampaign[i].id!!, getcampaign[i].campaignName!!, Constants.CAMPAIGN
                )
                searchListItems.add(campaignSelected);
            }

            searchableDialog?.show()
//            binding.selectActivity.isEnabled = false
        }

        binding.selectCity.setOnClickListener {
            searchListItems.clear()
            var indices = getcity.indices
            for (i in indices) {

                var campaignSelected = SearchListItem(
                    getcity[i].id!!, getcity[i].cityName!!, Constants.CITY
                )
                searchListItems.add(campaignSelected);
            }
            searchableDialog?.show()
//            binding.selectCity.isEnabled = false
        }

        binding.selectLocation.setOnClickListener {


            searchListItems.clear()
            for (i in getlocation.indices) {

                var campaignSelected = SearchListItem(
                    getlocation[i].id!!, getlocation[i].locationName!!, Constants.LOCATION
                )
                searchListItems.add(campaignSelected);
            }

            searchableDialog?.show()
        }

        binding.selectStore.setOnClickListener {


            searchListItems.clear()
            for (i in getStore.indices) {

                var campaignSelected = SearchListItem(
                    getStore[i].id!!, getStore[i].storeName!!, Constants.STORE
                )
                searchListItems.add(campaignSelected);
            }
            searchableDialog?.show()
        }

        binding.logout.setOnClickListener {

            if (Constants.isInternetConnected(this@SelectActivity)) {
                var tinyDB = TinyDB(this@SelectActivity)
                tinyDB.clear()

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

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.left, R.anim.left2);
                this.finish()

            } else Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()

        }

        searchableDialog?.setOnItemSelected(object : OnSearchItemSelected {
            override fun onClick(position: Int, searchListItem: SearchListItem, type: Int) {

                when (type) {
                    Constants.BRANDS -> {
                        binding.selectActivity.text.clear()
                        binding.selectCity.text.clear()
                        binding.selectLocation.text.clear()
                        binding.selectStore.text.clear()

                        SELECTED_CAMPAIGN_ID = 0
                        SELECTED_CITY_ID = 0
                        SELECTED_LOCATION_ID = 0
                        SELECTED_STORE_ID = 0


                        binding.selectBrand.setText(searchListItem.title)
                        SELECTED_BRAND_ID = searchListItem.id
                        tinydb.putInt("brandId", SELECTED_BRAND_ID)
                        tinydb.putString("brandName", searchListItem.title)

                        getcampaign =
                            mbsDatabase.getMBSData().getCampaignbyBrand(SELECTED_BRAND_ID)


                    }

                    Constants.CAMPAIGN -> {

                        binding.selectCity.text.clear()
                        binding.selectLocation.text.clear()
                        binding.selectStore.text.clear()

                        SELECTED_CITY_ID = 0
                        SELECTED_LOCATION_ID = 0
                        SELECTED_STORE_ID = 0


                        binding.selectActivity.setText(searchListItem.title)
                        SELECTED_CAMPAIGN_ID = searchListItem.id

                        searchListItems.clear()
                        USER_ID = userdata.id!!


                        var city = mbsDatabase.getMBSData()
                            .getCitybyCampaign(SELECTED_CAMPAIGN_ID, USER_ID)
                        getcity = city

                    }

                    Constants.CITY -> {

                        binding.selectLocation.text.clear()
                        binding.selectStore.text.clear()

                        SELECTED_LOCATION_ID = 0
                        SELECTED_STORE_ID = 0


                        binding.selectCity.setText(searchListItem.title)
                        SELECTED_CITY_ID = searchListItem.id


                        getlocation = mbsDatabase.getMBSData()
                            .getLocationbyCity(SELECTED_CAMPAIGN_ID, USER_ID, SELECTED_CITY_ID)


                    }

                    Constants.LOCATION -> {

                        binding.selectStore.text.clear()

                        SELECTED_STORE_ID = 0

                        binding.selectLocation.setText(searchListItem.title)
                        SELECTED_LOCATION_ID = searchListItem.id

                        tinydb.putInt("brandid", SELECTED_BRAND_ID)
                        tinydb.putInt("locationid", SELECTED_LOCATION_ID)
                        tinydb.putInt("cityId", SELECTED_CITY_ID)
                        tinydb.putInt("campaignId", SELECTED_CAMPAIGN_ID)

                        getStore = mbsDatabase.getMBSData().getStorebyLocation(
                            SELECTED_CAMPAIGN_ID, USER_ID, SELECTED_LOCATION_ID
                        )

                        if (getStore.size > 0) {
                            binding.selectStore.visibility = View.VISIBLE
                        } else {
                            binding.selectStore.visibility = View.GONE
                        }


                    }

                    Constants.STORE -> {

                        binding.selectStore.setText(searchListItem.title)
                        SELECTED_STORE_ID = searchListItem.id
                        tinydb.putInt("storeId", SELECTED_STORE_ID)
                    }
                }


            }


        })


        binding.refresh.setOnClickListener {

            if (!Constants.isInternetConnected(this@SelectActivity)) {
                Toast.makeText(
                    this@SelectActivity,
                    "Please Connect to Internet",
                    Toast.LENGTH_SHORT
                ).show()

            }else {
                var isSyncCheck = mbsDatabase.getMBSData().getAllMediaForSync(0)
                var data = mbsDatabase.getMBSData().getmediabyID(0)
                var activitylogid = mbsDatabase.getMBSData().getUnSyncActivityLogID()
                if (activitylogid.size > 0) {
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
                    createWorkRequest(true)


                } else if (isSyncCheck.size > 0) {

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
                    createWorkRequest(false)
                } else {
                    Toast.makeText(this@SelectActivity, "No Data to sync", Toast.LENGTH_SHORT).show()
                }
            }
            }

    }

    fun createWorkRequest(FullData: Boolean) {


        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        if (FullData) {
            val workRequest = OneTimeWorkRequest.Builder(FullWorker::class.java)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(this@SelectActivity).enqueue(workRequest)
        } else {
            val workRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(this@SelectActivity).enqueue(workRequest)
        }

    }

    fun calculateInitialDelay(): Long {
        // Calculate the initial delay in milliseconds for the service to run every 10 minutes
        val tenMinutesInMilliseconds = 2 * 60 * 1000
        val currentTimeMillis = System.currentTimeMillis()

        // Calculate the next time the service should run
        val nextRunTime = currentTimeMillis + tenMinutesInMilliseconds

        // Calculate the initial delay
        return nextRunTime - currentTimeMillis
    }

    override fun onBackPressed() {

    }
}