package com.mbs.mbsapp.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "brand_ambassadors")
data class BrandAmbassadorEntity(
    @PrimaryKey
    @ColumnInfo(name = "mid")
    val mid: Int?,
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "ba_name")
    val baName: String?,
    @ColumnInfo(name = "campaign_id")
    val campaignId: Int?,
    @ColumnInfo(name = "activity_id")
    val activityId: Int?,
    @ColumnInfo(name = "activity_detail_id")
    val activityDetailId: Int?,
    @ColumnInfo(name = "pitchCompleted")
    val pitchCompleted: Int?,
    @ColumnInfo(name = "submited_at")
    val submittedAt: String?, // Convert to appropriate type if necessary
    @ColumnInfo(name = "submited_by")
    val submittedBy: Int?,
    @ColumnInfo(name = "created_at")
    val createdAt: String?, // Convert to appropriate type if necessary
    @ColumnInfo(name = "updated_at")
    val updatedAt: String? // Convert to appropriate type if necessary
)
