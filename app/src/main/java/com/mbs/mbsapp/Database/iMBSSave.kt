package com.mbs.mbsapp.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mbs.mbsapp.Database.Entities.ActivityDetailEntity
import com.mbs.mbsapp.Database.Entities.ActivityLog
import com.mbs.mbsapp.Database.Entities.ActivityMaster
import com.mbs.mbsapp.Database.Entities.BrandEntity
import com.mbs.mbsapp.Database.Entities.CampaignChannel
import com.mbs.mbsapp.Database.Entities.CampaignEntity
import com.mbs.mbsapp.Database.Entities.CityEntity
import com.mbs.mbsapp.Database.Entities.LocationEntity
import com.mbs.mbsapp.Database.Entities.ProductEntity
import com.mbs.mbsapp.Database.Entities.QuestionEntity
import com.mbs.mbsapp.Database.Entities.QuestionSectionEntity
import com.mbs.mbsapp.Database.Entities.QuestionnaireEntity
import com.mbs.mbsapp.Database.Entities.StoreEntity
import com.mbs.mbsapp.Database.Entities.UserEntity

@Dao
interface iMBSSave {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrands(brand: BrandEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaign(campaignEntity: CampaignEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(productEntity: ProductEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaignChannel(campaignChannel: CampaignChannel?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cityEntity: CityEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(locationEntity: LocationEntity?)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStores(storeEntity: StoreEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityDetails(activityDetailEntity: ActivityDetailEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityMaster(activityMaster: ActivityMaster?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionnaire(questionnaireEntity: QuestionnaireEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(questionEntity: QuestionEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityLog(activityLog: ActivityLog?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionSection(questionSectionEntity: QuestionSectionEntity?)


    @Query("Select * from users order by mid ASC")
    fun getUser(): UserEntity

    @Query("Select * from brands order by mid ASC")
    fun getAllBrands(): List<BrandEntity>

    @Query("Select * from brands where id = :brandId")
    fun getBrandByID(brandId: Int): BrandEntity


    @Query("Select * from campaigns order by mid ASC")
    fun getAllCampaign(): List<CampaignEntity>

    @Query("Select * from campaign_channels order by mid ASC")
    fun getAllCampaignChannel(): List<CampaignChannel>

    @Query("Select * from cities order by mid ASC")
    fun getAllCities(): List<CityEntity>

    @Query("Select * from cities where id = :cityId")
    fun getCityById(cityId: Int): CityEntity

    @Query("Select * from locations order by mid ASC")
    fun getAllLocations(): List<LocationEntity>

    @Query("Select * from locations where id = :locationId")
    fun getLocationByID(locationId: Int): LocationEntity

    @Query("Select * from stores order by mid ASC")
    fun getAllStores(): List<StoreEntity>

    @Query("Select * from stores where id = :storeId")
    fun getStoresByID(storeId: Int): StoreEntity

    @Query("Select * from activity_detials order by mid ASC")
    fun getAllActivityDetails(): List<ActivityDetailEntity>

    @Query("Select * from activity_masters order by mid ASC")
    fun getAllActivityMasters(): List<ActivityMaster>

    @Query("Select * from question_sections order by mid ASC")
    fun getQuestionSection(): List<QuestionSectionEntity>

    @Query("Select * from questionnaire where campaign_id = :campaignId")
    fun getQuestionnaire(campaignId: Int): List<QuestionnaireEntity>

    @Query("Select * from questions where questionnaire_id = :questionnaireid")
    fun getQuestion(questionnaireid: Int): List<QuestionEntity>

    @Query("Select * from products where campaign_id = :campaignId")
    fun getProducts(campaignId: Int): List<ProductEntity>

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Query("DELETE FROM activity_masters")
    suspend fun deleteAllActivityMasters()

    @Query("DELETE FROM activity_detials")
    suspend fun deleteAllActivityDetails()

    @Query("DELETE FROM brands")
    suspend fun deleteAllBrands()

    @Query("DELETE FROM campaigns")
    suspend fun deleteAllCampaigns()

    @Query("DELETE FROM cities")
    suspend fun deleteAllCities()

    @Query("DELETE FROM locations")
    suspend fun deleteAllLocations()

    @Query("DELETE FROM stores")
    suspend fun deleteAllStores()

    @Query("DELETE FROM questionnaire")
    suspend fun deleteAllQuestionnaires()

    @Query("DELETE FROM questions")
    suspend fun deleteAllQuestions()

    @Query("DELETE FROM question_sections")
    suspend fun deleteQuestionSection()

    @Query("DELETE FROM activity_logs")
    suspend fun deleteActivityLogs()

    @Query("DELETE FROM products")
    suspend fun deleteProducts()


    @Query(
        "select campaigns.*, cc.cc_store_level, cc.cc_name from campaigns\n" +
                "inner join campaign_channels cc ON cc.id = campaigns.cc_id\n" +
                "where brand_id = :brandid"
    )
    fun getCampaignbyBrand(brandid: Int): List<CampaignEntity>


    @Query("select c.city_name, c.id from activity_masters am inner join activity_detials ad ON ad.activity_master_id = am.id inner join cities c ON c.id = ad.city_id where am.campaign_id = :campaignId and am.user_id = :userId group by (c.id);")
    fun getCitybyCampaign(campaignId: Int, userId: Int): List<CityEntity>


    @Query(
        "select l.location_name, l.id from activity_masters am\n" +
                "    inner join activity_detials ad ON ad.activity_master_id = am.id\n" +
                "    inner join locations l ON l.id = ad.location_id\n" +
                "    where am.campaign_id = :campaignId and am.user_id = :userId and ad.city_id = :cityId\n" +
                "    group by (l.id);"
    )
    fun getLocationbyCity(campaignId: Int, userId: Int, cityId: Int): List<LocationEntity>


    @Query(
        "select s.store_name, s.id from activity_masters am\n" +
                "inner join activity_detials ad ON ad.activity_master_id = am.id\n" +
                "inner join stores s ON s.id = ad.store_id\n" +
                "where am.campaign_id = :campaignId and am.user_id = :userId and ad.location_id = :locationId\n" +
                "group by (s.id)"
    )
    fun getStorebyLocation(campaignId: Int, userId: Int, locationId: Int): List<StoreEntity>


}