package com.mbs.mbsapp.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_masters")
data class ActivityMaster(
    @PrimaryKey
    @ColumnInfo(name = "mid")
    val mid :Int? = 0,
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "activity_name")
    val activityName: String,
    @ColumnInfo(name = "activity_code")
    val activityCode: String,
    @ColumnInfo(name = "brand_id")
    val brandId: Int,
    @ColumnInfo(name = "campaign_id")
    val campaignId: Int,
    @ColumnInfo(name = "campaign_channel_id")
    val campaignChannelId: Int,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "created_by")
    val createdBy: Int,
    @ColumnInfo(name = "updated_by")
    val updatedBy: Int?,
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Int?,
    @ColumnInfo(name = "deleted_by")
    val deletedBy: Int?,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: String,
    @ColumnInfo(name = "created_at")
    val createdAt: String,
    @ColumnInfo(name = "updated_at")
    val updatedAt: String
)