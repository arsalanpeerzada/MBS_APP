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
    val username: String? = null,
    @ColumnInfo(name = "email")
    val email: String? = null,
    @ColumnInfo(name = "full_name")
    val fullName: String? = null,
    @ColumnInfo(name = "first_name")
    val firstName: String? = null,
    @ColumnInfo(name = "last_name")
    val lastName: String? = null,
    @ColumnInfo(name = "cnic_no")
    val cnicNo: String? = null,
    @ColumnInfo(name = "phone_no")
    val phoneNo: String? = null,
    @ColumnInfo(name = "email_verified_at")
    val emailVerifiedAt: String? = null,
    @ColumnInfo(name = "password")
    val password: String? = null,
    @ColumnInfo(name = "two_factor_secret")
    val twoFactorSecret: String? = null,
    @ColumnInfo(name = "two_factor_recovery_codes")
    val twoFactorRecoveryCodes: String? = null,
    @ColumnInfo(name = "two_factor_confirmed_at")
    val twoFactorConfirmedAt: String? = null,
    @ColumnInfo(name = "remember_token")
    val rememberToken: String? = null,
    @ColumnInfo(name = "can_delete")
    val canDelete: Int? = 0,
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Int? = 0,
    @ColumnInfo(name = "current_team_id")
    val currentTeamId: String? = null,
    @ColumnInfo(name = "profile_photo_path")
    val profilePhotoPath: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: String? = null,
    @ColumnInfo(name = "updated_at")
    val updatedAt: String? = null
)
