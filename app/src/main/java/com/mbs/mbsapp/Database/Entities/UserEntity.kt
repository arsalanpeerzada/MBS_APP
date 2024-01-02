package com.mbs.mbsapp.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mid")
    val mid :Int? = 0,
    @ColumnInfo(name = "id")
    val id: Int? = 0,
    @ColumnInfo(name = "status")
    val status: Int? = 0,
    @ColumnInfo(name = "group_id")
    val groupId: Int? = 0,
    @ColumnInfo(name = "username")
    val username: String? = "",
    @ColumnInfo(name = "email")
    val email: String? = "",
    @ColumnInfo(name = "full_name")
    val fullName: String? = "",
    @ColumnInfo(name = "first_name")
    val firstName: String? = "",
    @ColumnInfo(name = "last_name")
    val lastName: String? = "",
    @ColumnInfo(name = "cnic_no")
    val cnicNo: String? = "",
    @ColumnInfo(name = "phone_no")
    val phoneNo: String? = "",
    @ColumnInfo(name = "email_verified_at")
    val emailVerifiedAt: String? = "",
    @ColumnInfo(name = "password")
    val password: String? = "",
    @ColumnInfo(name = "two_factor_secret")
    val twoFactorSecret: String? = "",
    @ColumnInfo(name = "two_factor_recovery_codes")
    val twoFactorRecoveryCodes: String? = "",
    @ColumnInfo(name = "two_factor_confirmed_at")
    val twoFactorConfirmedAt: String? = "",
    @ColumnInfo(name = "remember_token")
    val rememberToken: String? = "",
    @ColumnInfo(name = "can_delete")
    val canDelete: Int? = 0,
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Int? = 0,
    @ColumnInfo(name = "current_team_id")
    val currentTeamId: String? = "",
    @ColumnInfo(name = "profile_photo_path")
    val profilePhotoPath: String? = "",
    @ColumnInfo(name = "created_at")
    val createdAt: String? = "",
    @ColumnInfo(name = "updated_at")
    val updatedAt: String? = ""
)
