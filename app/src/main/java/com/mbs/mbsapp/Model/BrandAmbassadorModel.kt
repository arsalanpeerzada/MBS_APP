package com.mbs.mbsapp.Model

import com.google.gson.annotations.SerializedName

data class BrandAmbassadorModel(
    @SerializedName("id"                 ) var id               : Int?    = null,
    @SerializedName("ba_name"            ) var baName           : String? = null,
    @SerializedName("campaign_id"        ) var campaignId       : Int?    = null,
    @SerializedName("activity_id"        ) var activityId       : Int?    = null,
    @SerializedName("activity_detail_id" ) var activityDetailId : Int?    = null,
    @SerializedName("submited_at"        ) var submitedAt       : String? = null,
    @SerializedName("submited_by"        ) var submitedBy       : Int?    = null,
    @SerializedName("created_at"         ) var createdAt        : String? = null,
    @SerializedName("updated_at"         ) var updatedAt        : String? = null
)
