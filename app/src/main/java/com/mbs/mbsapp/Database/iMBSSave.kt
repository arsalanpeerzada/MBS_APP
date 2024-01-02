package com.mbs.mbsapp.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mbs.mbsapp.Database.Entities.ActivityDetailEntity
import com.mbs.mbsapp.Database.Entities.ActivityLog
import com.mbs.mbsapp.Database.Entities.ActivityMaster
import com.mbs.mbsapp.Database.Entities.AnswerDetailEntity
import com.mbs.mbsapp.Database.Entities.AnswerMasterEntity
import com.mbs.mbsapp.Database.Entities.BaPitchEntity
import com.mbs.mbsapp.Database.Entities.BrandAmbassadorEntity
import com.mbs.mbsapp.Database.Entities.BrandEntity
import com.mbs.mbsapp.Database.Entities.CampaignChannel
import com.mbs.mbsapp.Database.Entities.CampaignEntity
import com.mbs.mbsapp.Database.Entities.CityEntity
import com.mbs.mbsapp.Database.Entities.LocationEntity
import com.mbs.mbsapp.Database.Entities.MediaEntity
import com.mbs.mbsapp.Database.Entities.ProductEntity
import com.mbs.mbsapp.Database.Entities.ProductStock
import com.mbs.mbsapp.Database.Entities.QuestionEntity
import com.mbs.mbsapp.Database.Entities.QuestionSectionEntity
import com.mbs.mbsapp.Database.Entities.QuestionnaireEntity
import com.mbs.mbsapp.Database.Entities.StoreEntity
import com.mbs.mbsapp.Database.Entities.UserEntity
import com.mbs.mbsapp.QuestionnaireActivity

@Dao
interface iMBSSave {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrands(brand: BrandEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaign(campaignEntity: CampaignEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBA(brandAmbassadorEntity: BrandAmbassadorEntity?)

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
    fun insertActivityDetails(activityDetailEntity: ActivityDetailEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActivityMaster(activityMaster: ActivityMaster?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionnaire(questionnaireEntity: QuestionnaireEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(questionEntity: QuestionEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionSection(questionSectionEntity: QuestionSectionEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActivityLogs(activityLog: ActivityLog?)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnswerMaster(answerMasterEntity: AnswerMasterEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnswerDetail(answerDetailEntity: AnswerDetailEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedia(mediaEntity: MediaEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductStocks(productStock: ProductStock?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBAPitch(baPitchEntity: BaPitchEntity?)

    @Query("Select * from product_stocks where campaign_id = :campaignId AND activity_detail_id = :activitydetailid")
    fun getProductStocks(campaignId: Int, activitydetailid: Int): List<ProductStock>

    @Query("Select * from product_stocks where activity_log_id = :activitylogID")
    fun getProductStocksbyID(activitylogID: Int,): List<ProductStock>

    @Query("Select * from media order by mid ASC")
    fun getmedia(): List<MediaEntity>

    @Query("Select * from activity_logs order by mid ASC")
    fun getactivitylogs(): List<ActivityLog>

    @Query("Select * from media where activity_log_id = :activityLogid AND form_id = :formId")
    fun getmedia(activityLogid: Int, formId: Int): List<MediaEntity>

    @Query("Select * from media where activity_log_id = :activityLogid ")
    fun getmediabyID(activityLogid: Int): List<MediaEntity>

    @Query("Select * from activity_logs where mid = :mid")
    fun getactivitylogsById(mid:Int): ActivityLog

    @Query("Select * from media where is_sync = :isSync ")
    fun getAllMediaForSync(isSync: Int): List<MediaEntity>

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

//    @Query("Select * from question_sections order by mid ASC")
//    fun getQuestionSection(): List<QuestionSectionEntity>

    @Query("SELECT question_section_id, question_section_name FROM questions where questionnaire_id = :questionnareId group by question_section_id")
    fun getQuestionSection(questionnareId: Int): List<QuestionEntity>

    @Query("Select * from questionnaire where campaign_id = :campaignId")
    fun getQuestionnaire(campaignId: Int): List<QuestionnaireEntity>

    @Query("Select * from questions where questionnaire_id = :questionnaireid")
    fun getQuestion(questionnaireid: Int): List<QuestionEntity>

    @Query("Select * from questions where id = :questionid")
    fun getsingleQuestion(questionid: Int): List<QuestionEntity>

    @Query("Select * from products where campaign_id = :campaignId")
    fun getProducts(campaignId: Int): List<ProductEntity>

    @Query("Select * from activity_logs where campaign_id = :campaignId")
    fun getactivityLogs(campaignId: Int): List<ActivityLog>

    @Query("Select * from activity_logs where isSync = 0 AND serverid = 0 AND start_activity_tasks_completed = 1 AND end_activity_tasks_completed = 1")
    fun getUnSyncActivityLogID(): List<ActivityLog>


    @Query("Select * from activity_logs where activity_detail_code = :activityDetailCode AND activity_start_date = :activityStartDate")
    fun checkActivityLog(activityDetailCode: String, activityStartDate: String): List<ActivityLog>

    @Query("Select * from activity_logs where user_id = :user AND all_task_completed = 0")
    fun checkActivityASUSER(user: String): List<ActivityLog>

    @Query("Select * from activity_detials where activity_detail_code = :activity_detail_Id")
    fun getMasterId(activity_detail_Id: String): List<ActivityDetailEntity>

    @Query("Select * from answere_detials where activity_detail_id = :activity_detail_Id AND questionnaire_id = :questionnaireId AND activity_log_id = :activityLogid")
    fun getanswersbyID(
        activity_detail_Id: Int, questionnaireId: Int, activityLogid: Int
    ): List<AnswerDetailEntity>

    @Query("Select * from answere_detials")
    fun getanswersbyIDss(
    ): List<AnswerDetailEntity>


    @Query("Select * from ba_pitches bp inner join brand_ambassadors ba on bp.ba_id = ba.id where ba.activity_detail_id = :activity_detail_Id")
    fun getBApitches(
        activity_detail_Id: Int,
    ): List<BaPitchEntity>


    @Query("Select * from ba_pitches where ba_id = :activity_log_id")
    fun getBApitchesNew(
        activity_log_id: Int,
    ): List<BaPitchEntity>

    @Query("Select * from brand_ambassadors where activity_detail_id = :activity_detail_Id AND campaign_id = :campaignId AND activity_id = :activityID AND pitchCompleted = 0")
    fun getBA(
        activity_detail_Id: Int, campaignId: Int, activityID: Int
    ): List<BrandAmbassadorEntity>

    @Query("Select * from brand_ambassadors  order by mid ASC ")
    fun getBAaa(
    ): List<BrandAmbassadorEntity>


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


    @Query("DELETE FROM answere_detials")
    suspend fun deleteAdetail()

    @Query("DELETE FROM answere_masters")
    suspend fun deleteAMaster()

    @Query("DELETE FROM media")
    suspend fun deletemedia()

    @Query("DELETE FROM product_stocks")
    suspend fun deleteProductStocks()

    @Query("DELETE FROM campaign_channels")
    suspend fun deleteCampaignChannel()

    @Query("DELETE FROM ba_pitches")
    suspend fun deletebapitches()

    @Query("DELETE FROM brand_ambassadors")
    suspend fun deletebrandambbassadors()

    @Query("UPDATE brand_ambassadors SET pitchCompleted = :pitchCompleted WHERE id = :id")
    fun updatePitchCompleted(id: Int, pitchCompleted: Int)

    @Query("UPDATE activity_logs SET serverid = :serverid, isSync = 1 WHERE mid = :mid ")
    fun updateServerId(mid: Int, serverid: Int)

    @Query("UPDATE Media SET new_activity_log_id = :new_activity_log_id WHERE activity_log_id = :oldactivityLog")
    fun updateMedia(new_activity_log_id: Int, oldactivityLog: Int)

    @Query("UPDATE media SET is_sync = :isSync WHERE mid = :mid")
    fun updateMediaSync(mid: Int, isSync: Int)

    @Query("UPDATE activity_logs SET is_questionnaire_completed = :is_questionnaire_completed WHERE id = :id")
    fun updateQuestionnaire(is_questionnaire_completed: Int, id: Int)

    @Query("UPDATE activity_logs SET stock_entry_completed = :stock_entry_completed WHERE id = :id")
    fun updateStockPicture(stock_entry_completed: Int, id: Int)

    @Query("UPDATE activity_logs SET store_picture_completed = :store_picture_completed WHERE id = :id")
    fun updateStorePicture(store_picture_completed: Int, id: Int)

    @Query("UPDATE activity_logs SET ba_checklist_completed = :ba_checklist_completed WHERE id = :id")
    fun updateBA(ba_checklist_completed: Int, id: Int)

    @Query("UPDATE activity_logs SET start_activity_tasks_completed = :start_activity_tasks_completed WHERE id = :id")
    fun updateStartActivity(start_activity_tasks_completed: Int, id: Int)

    @Query("UPDATE activity_logs SET device_location_lat = :latitude,device_location_long = :longitude WHERE id = :id")
    fun updateLocation(latitude: String, longitude : String , id: Int)

    @Query("UPDATE activity_logs SET end_activity_tasks_completed = :end_activity_tasks_completed WHERE id = :id")
    fun updateEndActivity(end_activity_tasks_completed: Int, id: Int)

    @Query("UPDATE activity_logs SET all_task_completed = :all_task_completed, activity_end_date = :endDate, activity_end_time = :endTime  WHERE id = :id")
    fun updateFinal(all_task_completed: Int, id: Int, endDate: String, endTime: String)


    @Query(
        "select campaigns.*, cc.cc_store_level, cc.cc_name from campaigns\n" + "inner join campaign_channels cc ON cc.id = campaigns.cc_id\n" + "where brand_id = :brandid"
    )
    fun getCampaignbyBrand(brandid: Int): List<CampaignEntity>


    @Query("select c.city_name, c.id from activity_masters am inner join activity_detials ad ON ad.activity_master_id = am.id inner join cities c ON c.id = ad.city_id where am.campaign_id = :campaignId and am.user_id = :userId group by (c.id);")
    fun getCitybyCampaign(campaignId: Int, userId: Int): List<CityEntity>


    @Query(
        "select l.location_name, l.id from activity_masters am\n" + "    inner join activity_detials ad ON ad.activity_master_id = am.id\n" + "    inner join locations l ON l.id = ad.location_id\n" + "    where am.campaign_id = :campaignId and am.user_id = :userId and ad.city_id = :cityId\n" + "    group by (l.id);"
    )
    fun getLocationbyCity(campaignId: Int, userId: Int, cityId: Int): List<LocationEntity>


    @Query(
        "select s.store_name, s.id from activity_masters am\n" + "inner join activity_detials ad ON ad.activity_master_id = am.id\n" + "inner join stores s ON s.id = ad.store_id\n" + "where am.campaign_id = :campaignId and am.user_id = :userId and ad.location_id = :locationId\n" + "group by (s.id)"
    )
    fun getStorebyLocation(campaignId: Int, userId: Int, locationId: Int): List<StoreEntity>


}