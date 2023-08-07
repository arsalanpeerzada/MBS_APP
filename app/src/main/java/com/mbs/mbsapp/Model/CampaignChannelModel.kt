package com.mbs.mbsapp.Model

import com.google.gson.annotations.SerializedName

data class CampaignChannelModel(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("status_id") var statusId: Int? = null,
    @SerializedName("cc_name") var ccName: String? = null,
    @SerializedName("cc_store_level") var ccStoreLevel: Int? = null,
    @SerializedName("created_by") var createdBy: String? = null,
    @SerializedName("updated_by") var updatedBy: String? = null,
    @SerializedName("is_deleted") var isDeleted: String? = null,
    @SerializedName("deleted_by") var deletedBy: String? = null,
    @SerializedName("deleted_at") var deletedAt: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null
)
