package com.mbs.mbsapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.inksy.Database.MBSDatabase
import com.mbs.mbsapp.Utils.Constants
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityDashboardBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Dashboard : AppCompatActivity() {

    lateinit var binding: ActivityDashboardBinding
    lateinit var mbsDatabase: MBSDatabase
    lateinit var tinydb: TinyDB
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

        var brandId = tinydb.getInt("brandId")
        var locationId = tinydb.getInt("locationid")
        var cityId = tinydb.getInt("cityId")
        var storeId = tinydb.getInt("storeId")
        var time = tinydb.getString("time")
        var activityName = tinydb.getString("activityName")

        binding.textView7.setText("Activity Started at $time")
        var getBrand = mbsDatabase.getMBSData().getBrandByID(brandId)
        var getCity = mbsDatabase.getMBSData().getCityById(cityId)
        var getLocation = mbsDatabase.getMBSData().getLocationByID(locationId)
        if (storeId > 0) {
            var getStore = mbsDatabase.getMBSData().getStoresByID(storeId)
            binding.storeName.text = getStore.storeName
        } else {
            binding.storee.visibility = View.GONE
        }

        binding.view.setBackgroundColor(Color.parseColor(getBrand.brandPrimaryColor))
        Glide.with(this)
            .load(Constants.baseURLforPicture + getBrand.picturepath + getBrand.pictureName)
            .into(binding.dashboardImage)

        binding.cityName.text = getCity.cityName
        binding.locationName.text = getLocation.locationName
        binding.textView5.text = activityName


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

        binding.EndActivity.setOnClickListener {
            if (Constants.isInternetConnected(this@Dashboard)) {
                val intent = Intent(this, EndActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.left, R.anim.left2);
            } else Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()

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

                }

                var tinyDB = TinyDB(this)
                tinyDB.clear();
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.left, R.anim.left2);
                this.finish()
            } else Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()

        }
    }
}