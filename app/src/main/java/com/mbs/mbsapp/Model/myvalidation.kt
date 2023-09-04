package com.mbs.mbsapp.Model

import com.google.gson.annotations.SerializedName

data class myvalidation(
    @SerializedName("activity_log_id") var activityLogId: ArrayList<String> = arrayListOf()
)