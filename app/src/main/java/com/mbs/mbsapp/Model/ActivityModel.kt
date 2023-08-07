package com.mbs.mbsapp.Model

import com.google.gson.annotations.SerializedName

data class ActivityModel(
    @SerializedName("activity_master"  ) var activityMaster  : ArrayList<ActivityMaster>  = arrayListOf(),
    @SerializedName("activity_detials" ) var activityDetials : ArrayList<ActivityDetials> = arrayListOf()
){
    data class ActivityMaster (

        @SerializedName("id"                  ) var id                : Int?    = null,
        @SerializedName("activity_name"       ) var activityName      : String? = null,
        @SerializedName("activity_code"       ) var activityCode      : String? = null,
        @SerializedName("brand_id"            ) var brandId           : Int?    = null,
        @SerializedName("campaign_id"         ) var campaignId        : Int?    = null,
        @SerializedName("campaign_channel_id" ) var campaignChannelId : Int?    = null,
        @SerializedName("user_id"             ) var userId            : Int?    = null,
        @SerializedName("created_by"          ) var createdBy         : String?    = null,
        @SerializedName("updated_by"          ) var updatedBy         : String? = null,
        @SerializedName("is_deleted"          ) var isDeleted         : String? = null,
        @SerializedName("deleted_by"          ) var deletedBy         : String? = null,
        @SerializedName("deleted_at"          ) var deletedAt         : String? = null,
        @SerializedName("created_at"          ) var createdAt         : String? = null,
        @SerializedName("updated_at"          ) var updatedAt         : String? = null

    )
    data class ActivityDetials (

        @SerializedName("id"                 ) var id               : Int?    = null,
        @SerializedName("activity_master_id" ) var activityMasterId : Int?    = null,
        @SerializedName("city_id"            ) var cityId           : Int?    = null,
        @SerializedName("location_id"        ) var locationId       : Int?    = null,
        @SerializedName("store_id"           ) var storeId          : Int? = null,
        @SerializedName("created_at"         ) var createdAt        : String? = null,
        @SerializedName("updated_at"         ) var updatedAt        : String? = null

    )
}
