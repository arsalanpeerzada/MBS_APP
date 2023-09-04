package com.mbs.mbsapp.Model

import com.google.gson.annotations.SerializedName

data class ActivitySubmitModel(
    @SerializedName("activity_log_id") var activityLogId: Int? = null
)
