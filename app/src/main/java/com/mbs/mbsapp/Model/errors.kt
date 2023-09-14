package com.mbs.mbsapp.Model

import com.google.gson.annotations.SerializedName

data class errors(
    @SerializedName("activity_log_id") var activityLogId: ArrayList<String> = arrayListOf(),
    @SerializedName("questionnaire_id") var questionnaire_id: ArrayList<String> = arrayListOf(),
    @SerializedName("campaign_id") var campaign_id: ArrayList<String> = arrayListOf(),
    @SerializedName("activity_id") var activity_id: ArrayList<String> = arrayListOf(),
    @SerializedName("activity_detail_id") var activity_detail_id: ArrayList<String> = arrayListOf(),
    @SerializedName("submitted_at") var submitted_at: ArrayList<String> = arrayListOf(),
    @SerializedName("current_status") var current_status: ArrayList<String> = arrayListOf(),


    @SerializedName("form_id") var form_id: ArrayList<String> = arrayListOf(),
    @SerializedName("form_name") var form_name: ArrayList<String> = arrayListOf(),
    @SerializedName("data_id") var data_id: ArrayList<String> = arrayListOf(),
    @SerializedName("data_name") var data_name: ArrayList<String> = arrayListOf(),
    @SerializedName("activity_media") var activity_media: ArrayList<String> = arrayListOf(),
    @SerializedName("mobile_media_id") var mobile_media_id: ArrayList<String> = arrayListOf(),
)