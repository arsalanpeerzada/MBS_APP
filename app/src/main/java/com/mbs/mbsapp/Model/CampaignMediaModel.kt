package com.mbs.mbsapp.Model

import com.google.gson.annotations.SerializedName

data class CampaignMediaModel(
    @SerializedName("activity_media_name") var activityMediaName: String? = null,
    @SerializedName("activity_media_path") var activityMediaPath: String? = null,
    @SerializedName("id") var id: Int? = null
)
