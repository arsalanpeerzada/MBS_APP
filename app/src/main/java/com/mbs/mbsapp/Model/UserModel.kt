package com.mbs.mbsapp.Model

import com.google.gson.annotations.SerializedName

class UserModel(
    @SerializedName("id"                      ) var id                   : Int?    = null,
    @SerializedName("status"                  ) var status               : Int?    = null,
    @SerializedName("group_id"                ) var groupId              : Int?    = null,
    @SerializedName("username"                ) var username             : String? = null,
    @SerializedName("email"                   ) var email                : String? = null,
    @SerializedName("full_name"               ) var fullName             : String? = null,
    @SerializedName("first_name"              ) var firstName            : String? = null,
    @SerializedName("last_name"               ) var lastName             : String? = null,
    @SerializedName("cnic_no"                 ) var cnicNo               : String? = null,
    @SerializedName("phone_no"                ) var phoneNo              : String? = null,
    @SerializedName("email_verified_at"       ) var emailVerifiedAt      : String? = null,
    @SerializedName("two_factor_confirmed_at" ) var twoFactorConfirmedAt : String? = null,
    @SerializedName("can_delete"              ) var canDelete            : Int?    = null,
    @SerializedName("is_deleted"              ) var isDeleted            : Int?    = null,
    @SerializedName("current_team_id"         ) var currentTeamId        : String? = null,
    @SerializedName("profile_photo_path"      ) var profilePhotoPath     : String? = null,
    @SerializedName("created_at"              ) var createdAt            : String? = null,
    @SerializedName("updated_at"              ) var updatedAt            : String? = null,
    @SerializedName("profile_photo_url"       ) var profilePhotoUrl      : String? = null
)