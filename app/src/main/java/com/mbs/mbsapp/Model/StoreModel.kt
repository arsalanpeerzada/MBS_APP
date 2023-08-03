package com.mbs.mbsapp.Model

import com.google.gson.annotations.SerializedName

data class StoreModel(
    @SerializedName("id"          ) var id         : Int?    = null,
    @SerializedName("location_id" ) var locationId : Int?    = null,
    @SerializedName("store_name"  ) var storeName  : String? = null,
    @SerializedName("store_code"  ) var storeCode  : String? = null,
    @SerializedName("created_by"  ) var createdBy  : String?    = null,
    @SerializedName("updated_by"  ) var updatedBy  : String? = null,
    @SerializedName("is_deleted"  ) var isDeleted  : String? = null,
    @SerializedName("deleted_by"  ) var deletedBy  : String? = null,
    @SerializedName("deleted_at"  ) var deletedAt  : String? = null,
    @SerializedName("created_at"  ) var createdAt  : String? = null,
    @SerializedName("updated_at"  ) var updatedAt  : String? = null
)
