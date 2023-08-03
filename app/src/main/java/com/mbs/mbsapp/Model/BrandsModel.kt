package com.mbs.mbsapp.Model

import com.google.gson.annotations.SerializedName

data class BrandsModel(
    @SerializedName("id"                    ) var id                  : Int?    = null,
    @SerializedName("status_id"             ) var statusId            : Int?    = null,
    @SerializedName("brand_name"            ) var brandName           : String? = null,
    @SerializedName("brand_desciption"      ) var brandDesciption     : String? = null,
    @SerializedName("brand_logo_id"         ) var brandLogoId         : Int?    = null,
    @SerializedName("brand_primary_color"   ) var brandPrimaryColor   : String? = null,
    @SerializedName("brand_secondary_color" ) var brandSecondaryColor : String? = null,
    @SerializedName("created_by"            ) var createdBy           : String?    = null,
    @SerializedName("updated_by"            ) var updatedBy           : String?    = null,
    @SerializedName("is_deleted"            ) var isDeleted           : Int? = null,
    @SerializedName("deleted_by"            ) var deletedBy           : String? = null,
    @SerializedName("deleted_at"            ) var deletedAt           : String? = null,
    @SerializedName("created_at"            ) var createdAt           : String? = null,
    @SerializedName("updated_at"            ) var updatedAt           : String? = null,
    @SerializedName("brand_logo_path"       ) var brandLogoPath       : String? = null,
    @SerializedName("brand_logo_name"       ) var brandLogoName       : String? = null
)
