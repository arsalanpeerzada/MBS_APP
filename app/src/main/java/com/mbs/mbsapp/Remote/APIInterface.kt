package com.inksy.Remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.mbs.mbsapp.Model.ActivityModel
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
import retrofit2.Call
import retrofit2.http.*


public interface APIInterface {


//    ----------------------------------------------------------------------------------------
    // Login

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


    class ApiResponse<T> {
        @SerializedName("success")
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