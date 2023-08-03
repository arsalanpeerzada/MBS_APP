package com.mbs.mbsapp.Model

import com.google.gson.annotations.SerializedName

data class CampaignModel(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("status_id") var statusId: Int? = null,
    @SerializedName("cc_id") var ccId: Int? = null,
    @SerializedName("brand_id") var brandId: Int? = null,
    @SerializedName("questionair_created") var questionairCreated: Int? = null,
    @SerializedName("campaign_name") var campaignName: String? = null,
    @SerializedName("campaign_description") var campaignDescription: String? = null,
    @SerializedName("campaign_start_date") var campaignStartDate: String? = null,
    @SerializedName("campaign_end_date") var campaignEndDate: String? = null,
    @SerializedName("created_by") var createdBy: String? = null,
    @SerializedName("updated_by") var updatedBy: String? = null,
    @SerializedName("is_deleted") var isDeleted: String? = null,
    @SerializedName("deleted_by") var deletedBy: String? = null,
    @SerializedName("deleted_at") var deletedAt: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null
)
