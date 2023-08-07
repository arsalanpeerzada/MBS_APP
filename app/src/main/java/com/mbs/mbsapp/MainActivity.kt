package com.mbs.mbsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.inksy.Database.MBSDatabase
import com.inksy.Remote.APIClient
import com.inksy.Remote.APIInterface
import com.mbs.mbsapp.Database.Entities.ActivityDetailEntity
import com.mbs.mbsapp.Database.Entities.ActivityMaster
import com.mbs.mbsapp.Database.Entities.BrandEntity
import com.mbs.mbsapp.Database.Entities.CampaignChannel
import com.mbs.mbsapp.Database.Entities.CampaignEntity
import com.mbs.mbsapp.Database.Entities.CityEntity
import com.mbs.mbsapp.Database.Entities.LocationEntity
import com.mbs.mbsapp.Database.Entities.QuestionEntity
import com.mbs.mbsapp.Database.Entities.QuestionnaireEntity
import com.mbs.mbsapp.Database.Entities.StoreEntity
import com.mbs.mbsapp.Database.Entities.UserEntity
import com.mbs.mbsapp.Model.ActivityModel
import com.mbs.mbsapp.Model.BrandsModel
import com.mbs.mbsapp.Model.CampaignChannelModel
import com.mbs.mbsapp.Model.CampaignModel
import com.mbs.mbsapp.Model.CitiesModel
import com.mbs.mbsapp.Model.LocationModel
import com.mbs.mbsapp.Model.QuestionnaireModel
import com.mbs.mbsapp.Model.StoreModel
import com.mbs.mbsapp.Model.UserModel
import com.mbs.mbsapp.Utils.Constants
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var tinyDB: TinyDB
    var apiInterface: APIInterface = APIClient.createService(APIInterface::class.java)
    lateinit var token: String
    var loadingPercentageNo = 0
    lateinit var mbsDatabase: MBSDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tinyDB = TinyDB(this@MainActivity)
        mbsDatabase = MBSDatabase.getInstance(this@MainActivity)!!
        binding.login.setOnClickListener {
//
            if (Constants.isInternetConnected(this@MainActivity))
                if (binding.email.text.isNullOrBlank() || binding.password.text.isNullOrBlank()) {
                    Toast.makeText(this@MainActivity, "Field is Empty", Toast.LENGTH_SHORT).show()
                } else
                    getLogin(binding.email.text.toString(), binding.password.text.toString())
            else Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    fun getLogin(
        email: String?,
        password: String?
    ) {
        binding.login.isEnabled = false
        apiInterface.login(email, password)
            .enqueue(object : Callback<APIInterface.ApiResponse<UserModel>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<UserModel>>,
                    response: Response<APIInterface.ApiResponse<UserModel>>
                ) {
                    if (response.isSuccessful) {
                        binding.loading.visibility = View.VISIBLE
                        token = response.body()?.token.toString()
                        tinyDB.putString("User", response.body()?.user?.id.toString())
                        tinyDB.putString("token", token)
                        insertIntoUserTable(response)
                        var finaltoken = "Bearer $token"


                        getBrandsAPI(finaltoken)
                        getCampaignAPI(finaltoken)
                        getLocationAPI(finaltoken)
                        getStoresAPI(finaltoken)
                        getCitiesAPI(finaltoken)
                        getActivitiesAPI(finaltoken)
                        getQuestionnaire(finaltoken)
                        getCampaignChannelAPI(finaltoken)

                    }else {
                        binding.login.isEnabled = true
                        Toast.makeText(this@MainActivity, "Login Failed, Try Again", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<UserModel>>,
                    t: Throwable
                ) {
                    binding.login.isEnabled = true
                    Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_SHORT).show()
                }
            })

    }

    private fun getCampaignAPI(token: String) {
        apiInterface.getCampaign(token)
            .enqueue(object : Callback<APIInterface.ApiResponse<List<CampaignModel>>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<List<CampaignModel>>>,
                    response: Response<APIInterface.ApiResponse<List<CampaignModel>>>
                ) {
                    if (response.isSuccessful) {
//                        Toast.makeText(
//                            this@MainActivity,
//                            "Campaign Loaded Successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        checkAPI(true)

                        GlobalScope.launch {
                            var id = 0
                            for (item in response.body()?.data!!) {
                                var campaignEntity = CampaignEntity(
                                    id,
                                    item.id,
                                    item.statusId,
                                    item.ccId,
                                    item.brandId,
                                    item.questionairCreated,
                                    item.campaignName,
                                    item.campaignDescription,
                                    item.campaignStartDate,
                                    item.campaignEndDate,
                                    item.createdBy,
                                    item.updatedBy,
                                    item.isDeleted,
                                    item.deletedBy,
                                    item.deletedAt,
                                    item.createdAt,
                                    item.updatedAt
                                )
                                mbsDatabase.getMBSData().insertCampaign(campaignEntity)
                                id++
                            }
                        }


                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<List<CampaignModel>>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        "Campaign Data Loading Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    checkAPI(false)
                }

            })
    }

    private fun getCampaignChannelAPI(token: String) {
        apiInterface.getCampaignChannel(token)
            .enqueue(object : Callback<APIInterface.ApiResponse<List<CampaignChannelModel>>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<List<CampaignChannelModel>>>,
                    response: Response<APIInterface.ApiResponse<List<CampaignChannelModel>>>
                ) {
                    if (response.isSuccessful) {
//                        Toast.makeText(
//                            this@MainActivity,
//                            "Campaign Loaded Successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        checkAPI(true)

                        GlobalScope.launch {
                            var id = 0
                            for (item in response.body()?.data!!) {
                                var campaignChannel = CampaignChannel(
                                    id,
                                    item.id,
                                    item.statusId,
                                    item.ccName,
                                    item.ccStoreLevel,
                                    item.createdBy,
                                    item.updatedBy,
                                    item.isDeleted,
                                    item.deletedBy,
                                    item.deletedAt,
                                    item.createdAt,
                                    item.updatedAt
                                )
                                mbsDatabase.getMBSData().insertCampaignChannel(campaignChannel)
                                id++
                            }
                        }


                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<List<CampaignChannelModel>>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        "Campaign Data Loading Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    checkAPI(false)
                }

            })
    }

    private fun getCitiesAPI(token: String) {
        apiInterface.getCities(token)
            .enqueue(object : Callback<APIInterface.ApiResponse<List<CitiesModel>>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<List<CitiesModel>>>,
                    response: Response<APIInterface.ApiResponse<List<CitiesModel>>>
                ) {
                    if (response.isSuccessful) {
//                        Toast.makeText(
//                            this@MainActivity,
//                            "Cities Loaded Successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        checkAPI(true)
                        GlobalScope.launch {
                            var id = 0
                            for (item in response.body()?.data!!) {
                                var cityEntity = CityEntity(
                                    id,
                                    item.id,
                                    item.cityName,
                                    item.cityCode,
                                    item.createdBy,
                                    item.updatedBy,
                                    item.isDeleted,
                                    item.deletedBy,
                                    item.deletedAt,
                                    item.createdAt,
                                    item.updatedAt
                                )
                                mbsDatabase.getMBSData().insertCities(cityEntity)
                                id++
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<List<CitiesModel>>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        "Cities Data Loading Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    checkAPI(false)
                }

            })
    }

    private fun getLocationAPI(token: String) {
        apiInterface.getLocation(token)
            .enqueue(object : Callback<APIInterface.ApiResponse<List<LocationModel>>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<List<LocationModel>>>,
                    response: Response<APIInterface.ApiResponse<List<LocationModel>>>
                ) {
                    if (response.isSuccessful) {
//                        Toast.makeText(
//                            this@MainActivity,
//                            "Location Loaded Successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        checkAPI(true)
                        GlobalScope.launch {
                            var id = 0
                            for (item in response.body()?.data!!) {
                                var locationEntity = LocationEntity(
                                    id,
                                    item.id,
                                    item.cityId,
                                    item.locationName,
                                    item.locationCode,
                                    item.createdBy,
                                    item.updatedBy,
                                    item.isDeleted,
                                    item.deletedBy,
                                    item.deletedAt,
                                    item.createdAt,
                                    item.updatedAt
                                )
                                mbsDatabase.getMBSData().insertLocation(locationEntity)
                                id++
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<List<LocationModel>>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        "Location Data Loading Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    checkAPI(false)
                }

            })
    }

    private fun getStoresAPI(token: String) {
        apiInterface.getStores(token)
            .enqueue(object : Callback<APIInterface.ApiResponse<List<StoreModel>>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<List<StoreModel>>>,
                    response: Response<APIInterface.ApiResponse<List<StoreModel>>>
                ) {
                    if (response.isSuccessful) {
//                        Toast.makeText(
//                            this@MainActivity,
//                            "Stores Loaded Successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        checkAPI(true)
                        GlobalScope.launch {
                            var id = 0
                            for (item in response.body()?.data!!) {
                                var storeEntity = StoreEntity(
                                    id,
                                    item.id,
                                    item.locationId,
                                    item.storeName,
                                    item.storeCode,
                                    item.createdBy,
                                    item.updatedBy,
                                    item.isDeleted,
                                    item.deletedBy,
                                    item.deletedAt,
                                    item.createdAt,
                                    item.updatedAt
                                )
                                mbsDatabase.getMBSData().insertStores(storeEntity)
                                id++
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<List<StoreModel>>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        "Stores Data Loading Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    checkAPI(false)
                }

            })
    }

    private fun getBrandsAPI(token: String) {
        apiInterface.getBrands(token)
            .enqueue(object : Callback<APIInterface.ApiResponse<List<BrandsModel>>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<List<BrandsModel>>>,
                    response: Response<APIInterface.ApiResponse<List<BrandsModel>>>
                ) {
                    if (response.isSuccessful) {
//                        Toast.makeText(
//                            this@MainActivity,
//                            "Brands Loaded Successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        checkAPI(true)

                        GlobalScope.launch {

                            var id = 0
                            for (item in response.body()?.data!!) {
                                var brandEntity = BrandEntity(
                                    id,
                                    item.id,
                                    item.statusId,
                                    item.brandName,
                                    item.brandDesciption,
                                    item.brandLogoId,
                                    item.brandPrimaryColor,
                                    item.brandSecondaryColor,
                                    item.createdBy,
                                    item.updatedBy,
                                    item.isDeleted,
                                    item.deletedBy,
                                    item.deletedAt,
                                    item.createdAt,
                                    item.updatedAt
                                )
                                mbsDatabase.getMBSData().insertBrands(brandEntity)
                                id++
                            }
                        }

                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            response.message().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<List<BrandsModel>>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        "Brands Data Loading Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    checkAPI(false)
                }

            })
    }


    private fun getActivitiesAPI(token: String) {
        apiInterface.getactivities(token)
            .enqueue(object : Callback<APIInterface.ApiResponse<ActivityModel>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<ActivityModel>>,
                    response: Response<APIInterface.ApiResponse<ActivityModel>>
                ) {
                    if (response.isSuccessful) {
//                        Toast.makeText(
//                            this@MainActivity,
//                            "Brands Loaded Successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        checkAPI(true)

                        GlobalScope.launch {
                            var id = 0
                            for (item in response.body()?.data?.activityDetials!!) {
                                var activityDetailEntity = ActivityDetailEntity(
                                    id,
                                    item.id,
                                    item.activityMasterId,
                                    item.cityId,
                                    item.locationId,
                                    item.storeId,
                                    item.createdAt,
                                    item.updatedAt
                                )
                                mbsDatabase.getMBSData().insertActivityDetails(activityDetailEntity)
                                id++
                            }

                            var idd = 0
                            for (item in response.body()?.data?.activityMaster!!) {
                                var activityMaster = ActivityMaster(
                                    idd,
                                    item.id,
                                    item.activityName,
                                    item.activityCode,
                                    item.brandId,
                                    item.campaignId,
                                    item.campaignChannelId,
                                    item.userId,
                                    item.createdBy,
                                    item.updatedBy,
                                    item.isDeleted,
                                    item.deletedBy,
                                    item.deletedAt,
                                    item.createdAt,
                                    item.updatedAt
                                )
                                mbsDatabase.getMBSData().insertActivityMaster(activityMaster)
                                idd++
                            }
                        }

                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            response.message().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<ActivityModel>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        "Brands Data Loading Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    checkAPI(false)
                }

            })
    }

    private fun getQuestionnaire(token: String) {
        apiInterface.getQuestionnaire(token)
            .enqueue(object : Callback<APIInterface.ApiResponse<QuestionnaireModel>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<QuestionnaireModel>>,
                    response: Response<APIInterface.ApiResponse<QuestionnaireModel>>
                ) {
                    if (response.isSuccessful) {
//                        Toast.makeText(
//                            this@MainActivity,
//                            "Brands Loaded Successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        checkAPI(true)

                        GlobalScope.launch {
                            var id = 0
                            for (item in response.body()?.data?.questionnaires!!) {
                                var questionnaireEntity = QuestionnaireEntity(
                                    id,
                                    item.id,
                                    item.questionnairName,
                                    item.campaignId,
                                    item.outOf,
                                    item.questionsCount,
                                    item.createdBy,
                                    item.updatedBy,
                                    item.isDeleted,
                                    item.deletedBy,
                                    item.deletedAt,
                                    item.createdAt,
                                    item.updatedAt
                                )
                                mbsDatabase.getMBSData().insertQuestionnaire(questionnaireEntity)
                                id++
                            }

                            var idd = 0
                            for (item in response.body()?.data?.questions!!) {
                                var questionEntity = QuestionEntity(
                                    idd,
                                    item.id,
                                    item.questionnaireId,
                                    item.questionSectionId,
                                    item.questionSectionName,
                                    item.question,
                                    item.isMediaAllowed,
                                    item.mediaCount,
                                    item.marks,
                                    item.createdBy,
                                    item.updatedBy,
                                    item.isDeleted,
                                    item.deletedBy,
                                    item.deletedAt,
                                    item.createdAt,
                                    item.updatedAt
                                )
                                mbsDatabase.getMBSData().insertQuestion(questionEntity)
                                idd++
                            }
                        }

                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            response.message().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<QuestionnaireModel>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        "Brands Data Loading Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    checkAPI(false)
                }

            })
    }


    fun checkAPI(check: Boolean) {

        if (check) {
            when (loadingPercentageNo) {
                0 -> {
                    binding.loading.text = "Loading .....   15%"
                    loadingPercentageNo++
                }

                1 -> {
                    binding.loading.text = "Loading .....   30%"
                    loadingPercentageNo++
                }

                2 -> {
                    binding.loading.text = "Loading .....   45%"
                    loadingPercentageNo++
                }

                3 -> {
                    binding.loading.text = "Loading .....   60%"
                    loadingPercentageNo++
                }

                4 -> {
                    binding.loading.text = "Loading .....   75%"
                    loadingPercentageNo++
                }

                5 -> {
                    binding.loading.text = "Loading .....   90%"
                    loadingPercentageNo++
                }

                6 -> {
                    binding.loading.text = "Loading .....   100%"
                    loadingPercentageNo++


                    val timer: Thread = object : Thread() {
                        override fun run() {
                            try {
                                try {
                                    sleep(2000)
                                } catch (e: InterruptedException) {
                                    e.printStackTrace()
                                }
                            } finally {

                                val intent = Intent(this@MainActivity, SelectActivity::class.java)
                                startActivity(intent)
                                overridePendingTransition(R.anim.left, R.anim.left2);
                                finish()
                            }
                        }
                    }
                    timer.start()


                }

            }
        } else {
            binding.login.isEnabled = true
            binding.loading.visibility = View.GONE
            binding.loading.text = "Loading .....   0%"
            loadingPercentageNo = 0
        }
    }


    private fun insertIntoUserTable(response: Response<APIInterface.ApiResponse<UserModel>>) {

        val userbody = response.body()?.user
        val userEntity = UserEntity(
            0,
            userbody?.id,
            userbody?.status,
            userbody?.groupId,
            userbody?.username,
            userbody?.email,
            userbody?.fullName,
            userbody?.firstName,
            userbody?.lastName,
            userbody?.cnicNo,
            userbody?.phoneNo,
            userbody?.emailVerifiedAt,
            "",
            "",
            "",
            userbody?.twoFactorConfirmedAt,
            "",
            userbody?.canDelete,
            userbody?.isDeleted,
            userbody?.currentTeamId,
            userbody?.profilePhotoPath,
            userbody?.createdAt,
            userbody?.updatedAt,
        )
        GlobalScope.launch {
            mbsDatabase?.getMBSData()?.insertUser(userEntity)
            Log.d("DB", "Success")
        }
    }

}