package com.inksy.Remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.mbs.mbsapp.EndActivity
import com.mbs.mbsapp.Model.ActivityModel
import com.mbs.mbsapp.Model.ActivitySubmitModel
import com.mbs.mbsapp.Model.AnswerModel
import com.mbs.mbsapp.Model.BrandAmbassadorModel
import com.mbs.mbsapp.Model.BrandsModel
import com.mbs.mbsapp.Model.CampaignChannelModel
import com.mbs.mbsapp.Model.CampaignModel
import com.mbs.mbsapp.Model.CitiesModel
import com.mbs.mbsapp.Model.LocationModel
import com.mbs.mbsapp.Model.ProductModel
import com.mbs.mbsapp.Model.QuestionSectionModel
import com.mbs.mbsapp.Model.QuestionnaireModel
import com.mbs.mbsapp.Model.StoreModel
import com.mbs.mbsapp.Model.UserModel
import com.mbs.mbsapp.Model.myvalidation
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


public interface APIInterface {


//    ----------------------------------------------------------------------------------------
    // Login


    @FormUrlEncoded
    @POST("push/activity/products")
    @Headers("Accept: application/json")
    fun SubmitProducts(
        @Header("Authorization") token: String?,
        @Field("activity_log_id") activity_log_id: Int?,
        @Field("campaign_id") campaign_id: Int?,
        @Field("products[id][]") answers: List<String>,
        @Field("products[count][]") comment: List<String>,
    ): Call<ApiResponse<ActivitySubmitModel>>


    @Multipart
    @Headers("Accept: application/json")
    @POST("push/activity/media")
    fun SubmitMediaData(
        @Header("Authorization") token: String?,
        @Part("activity_log_id") activity_log_id: RequestBody,
        @Part("form_id") form_id: RequestBody,
        @Part("form_name") form_name: RequestBody,
        @Part("data_id") data_id: RequestBody,
        @Part("data_name") data_name: RequestBody,
        @Part("activity_media\"; filename=\"myfile.jpg") activity_media: RequestBody,
    ): Call<ApiResponse<ActivitySubmitModel>>

    @FormUrlEncoded
    @POST("push/activity/answere")
    @Headers("Accept: application/json")
    fun SubmitAnswers(
        @Header("Authorization") token: String?,
        @Field("activity_log_id") activity_log_id: Int?,
        @Field("questionnaire_id") questionnaire_id: Int?,
        @Field("campaign_id") campaign_id: Int?,
        @Field("activity_id") activity_id: Int?,
        @Field("activity_detail_id") activity_detail_id: Int?,
        @Field("current_status") current_status: Int?,
        @Field("submitted_at") submitted_ats: String?,
        @Field("answeres[ans][]") answers: List<String>,
        @Field("answeres[question_id][]") question_id: List<String>,
        @Field("answeres[comment][]") comment: List<String>,
        @Field("answeres[media_attached][]") media_attached: List<String>,
        @Field("answeres[media_count][]") media_count: List<String>,
        @Field("answeres[submitted_at][]") submitted_at: List<String>,
    ): Call<ApiResponse<ActivitySubmitModel>>

//    @FormUrlEncoded
//    @POST("push/activity/answere")
//    @Headers("Accept: application/json")
//    fun SubmitAnswers(
//        @Header("Authorization") token: String?,
//        @Body request: RequestBody
//    ): Call<ApiResponse<ActivitySubmitModel>>


    @FormUrlEncoded
    @POST("push/activity")
    @Headers("Accept: application/json")
    fun SubmitActivity(
        @Header("Authorization") token: String?,
        @Field("activity_id") activity_id: Int?,
        @Field("activity_detail_id") activity_detail_id: Int?,
        @Field("brand_id") brand_id: Int?,
        @Field("campaign_id") campaign_id: Int?,
        @Field("city_id") city_id: Int?,
        @Field("location_id") location_id: Int?,
        @Field("store_id") store_id: Int?,
        @Field("activity_start_date") activity_start_date: String?,
        @Field("activity_start_time") activity_start_time: String?,
        @Field("device_location_lat") device_location_lat: String?,
        @Field("device_location_long") device_location_long: String?,
        @Field("start_activity_tasks_completed") start_activity_tasks_completed: Int?,
        @Field("is_questionnaire_completed") is_questionnaire_completed: Int?,
        @Field("stock_entry_completed") stock_entry_completed: Int?,
        @Field("store_picture_completed") store_picture_completed: Int?,
        @Field("ba_checklist_completed") ba_checklist_completed: Int?,
        @Field("all_task_completed") all_task_completed: Int?,
        @Field("end_activity_tasks_completed") end_activity_tasks_completed: Int?,
        @Field("activity_end_date") activity_end_date: String?,
        @Field("activity_end_time") activity_end_time: String?,
    ): Call<ApiResponse<ActivitySubmitModel>>


    @FormUrlEncoded
    @POST("login")
    @Headers("Accept: application/json")
    fun login(
        @Field("email") email: String?,
        @Field("password") password: String?,
    ): Call<ApiResponse<UserModel>>


    @POST("logout")
    @Headers("Accept: application/json")
    fun LogOut(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<String>>

    @GET("initialsync/brands")
    @Headers("Accept: application/json")
    fun getBrands(@Header("Authorization") token: String?): Call<ApiResponse<List<BrandsModel>>>

    @GET("initialsync/question_section")
    @Headers("Accept: application/json")
    fun getQuestionSection(@Header("Authorization") token: String?): Call<ApiResponse<List<QuestionSectionModel>>>


    @GET("initialsync/campaigns")
    @Headers("Accept: application/json")
    fun getCampaign(@Header("Authorization") token: String?): Call<ApiResponse<List<CampaignModel>>>

    @GET("initialsync/channels")
    @Headers("Accept: application/json")
    fun getCampaignChannel(@Header("Authorization") token: String?): Call<ApiResponse<List<CampaignChannelModel>>>

    @GET("initialsync/cities")
    @Headers("Accept: application/json")
    fun getCities(@Header("Authorization") token: String?): Call<ApiResponse<List<CitiesModel>>>

    @GET("initialsync/locations")
    @Headers("Accept: application/json")
    fun getLocation(@Header("Authorization") token: String?): Call<ApiResponse<List<LocationModel>>>

    @GET("initialsync/stores")
    @Headers("Accept: application/json")
    fun getStores(@Header("Authorization") token: String?): Call<ApiResponse<List<StoreModel>>>

    @GET("initialsync/activities")
    @Headers("Accept: application/json")
    fun getactivities(@Header("Authorization") token: String?): Call<ApiResponse<ActivityModel>>

    @GET("initialsync/questionnair")
    @Headers("Accept: application/json")
    fun getQuestionnaire(@Header("Authorization") token: String?): Call<ApiResponse<QuestionnaireModel>>

    @GET("initialsync/products")
    @Headers("Accept: application/json")
    fun getProducts(@Header("Authorization") token: String?): Call<ApiResponse<List<ProductModel>>>


    @GET("initialsync/brand_ambassadors")
    @Headers("Accept: application/json")
    fun getBrandAmbassador(@Header("Authorization") token: String?): Call<ApiResponse<List<BrandAmbassadorModel>>>


    class ApiResponse<T> {
        @SerializedName("status")
        @Expose
        var status: Boolean? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("token")
        @Expose
        var token: String? = null

        @SerializedName("total_rows")
        var totalRows: Int? = null

        @SerializedName("validation")
        var validation: Boolean? = null

        @SerializedName("validationErorrs")
        var validation_erorrs: myvalidation? = null

        @SerializedName("data")
        @Expose
        var data: T? = null
            private set

        @SerializedName("user")
        @Expose
        var user: T? = null
            private set

        fun setData(user: T) {
            this.user = user
        }
    }
}