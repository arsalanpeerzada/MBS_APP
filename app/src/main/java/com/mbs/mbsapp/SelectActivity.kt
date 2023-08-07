package com.mbs.mbsapp


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.inksy.Database.MBSDatabase
import com.mbs.mbsapp.Dialog.OnSearchItemSelected
import com.mbs.mbsapp.Dialog.SearchListItem
import com.mbs.mbsapp.Dialog.SearchableDialog
import com.mbs.mbsapp.Utils.Constants
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mbsDatabase = MBSDatabase.getInstance(this)!!

        binding.next.setOnClickListener {

            if (SELECTED_STORE_ID == 0 || SELECTED_LOCATION_ID == 0) {
                Toast.makeText(
                    this@SelectActivity,
                    "Please select all the field",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, ClusterStartActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.left, R.anim.left2);
            }


        }

        var brands = mbsDatabase.getMBSData().getAllBrands()
        var cities = mbsDatabase.getMBSData().getAllCities()
        var locations = mbsDatabase.getMBSData().getAllLocations()
        var stores = mbsDatabase.getMBSData().getAllStores()
        var activitydetail = mbsDatabase.getMBSData().getAllActivityDetails()
        var activitymaster = mbsDatabase.getMBSData().getAllActivityMasters()
        var campaings = mbsDatabase.getMBSData().getAllCampaign()
        var campaignChannel = mbsDatabase.getMBSData().getAllCampaignChannel()

        val searchListItems = ArrayList<SearchListItem>()
        for (i in brands.indices) {

            var brandselected =
                SearchListItem(brands[i].id!!, brands[i].brandName!!, Constants.BRANDS)
            searchListItems.add(brandselected);
        }


        searchableDialog = SearchableDialog(this, searchListItems, "Select Activity")

        binding.selectBrand.setOnClickListener {
            searchableDialog?.show()
        }

        binding.selectActivity.setOnClickListener {
            searchableDialog?.show()
        }

        binding.selectCity.setOnClickListener {
            searchableDialog?.show()
        }

        binding.selectLocation.setOnClickListener {
            searchableDialog?.show()
        }

        binding.selectStore.setOnClickListener {
            searchableDialog?.show()
        }

        binding.logout.setOnClickListener {
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
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()

        }

        searchableDialog?.setOnItemSelected(object : OnSearchItemSelected {
            override fun onClick(position: Int, searchListItem: SearchListItem, type: Int) {

                when (type) {
                    Constants.BRANDS -> {
                        binding.selectBrand.setText(searchListItem.title)
                        SELECTED_BRAND_ID = searchListItem.id

                        var getcampaign =
                            mbsDatabase.getMBSData().getCampaignbyBrand(SELECTED_BRAND_ID)
                        searchListItems.clear()
                        for (i in getcampaign.indices) {

                            var campaignSelected = SearchListItem(
                                getcampaign[i].id!!,
                                getcampaign[i].campaignName!!,
                                Constants.CAMPAIGN
                            )
                            searchListItems.add(campaignSelected);
                        }
                    }

                    Constants.CAMPAIGN -> {
                        binding.selectActivity.setText(searchListItem.title)
                        SELECTED_CAMPAIGN_ID = searchListItem.id

                        searchListItems.clear()
                        var userdata = mbsDatabase.getMBSData().getUser()
                        USER_ID = userdata.id!!

                        var getcity =
                            mbsDatabase.getMBSData()
                                .getCitybyCampaign(SELECTED_CAMPAIGN_ID, USER_ID)

                        for (i in getcity.indices) {

                            var campaignSelected = SearchListItem(
                                getcity[i].id!!,
                                getcity[i].cityName!!,
                                Constants.CITY
                            )
                            searchListItems.add(campaignSelected);
                        }

                    }

                    Constants.CITY -> {
                        binding.selectCity.setText(searchListItem.title)
                        SELECTED_CITY_ID = searchListItem.id

                        var getlocation =
                            mbsDatabase.getMBSData()
                                .getLocationbyCity(SELECTED_CAMPAIGN_ID, USER_ID, SELECTED_CITY_ID)
                        searchListItems.clear()

                        for (i in getlocation.indices) {

                            var campaignSelected = SearchListItem(
                                getlocation[i].id!!,
                                getlocation[i].locationName!!,
                                Constants.LOCATION
                            )
                            searchListItems.add(campaignSelected);
                        }

                    }

                    Constants.LOCATION -> {

                        binding.selectLocation.setText(searchListItem.title)
                        SELECTED_LOCATION_ID = searchListItem.id

                        var getStore =
                            mbsDatabase.getMBSData()
                                .getStorebyLocation(
                                    SELECTED_CAMPAIGN_ID,
                                    USER_ID,
                                    SELECTED_LOCATION_ID
                                )

                        if (getStore.size > 0) {
                            binding.selectStore.visibility = View.VISIBLE
                        } else {
                            binding.selectStore.visibility = View.GONE
                        }
                        searchListItems.clear()
                        for (i in getStore.indices) {

                            var campaignSelected = SearchListItem(
                                getStore[i].id!!,
                                getStore[i].storeName!!,
                                Constants.STORE
                            )
                            searchListItems.add(campaignSelected);
                        }
                    }

                    Constants.STORE -> {
                        binding.selectStore.setText(searchListItem.title)
                        SELECTED_STORE_ID = searchListItem.id
                    }
                }


            }
        })

    }
}