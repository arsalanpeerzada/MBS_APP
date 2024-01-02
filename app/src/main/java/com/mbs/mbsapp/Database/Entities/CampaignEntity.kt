package com.mbs.mbsapp.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "campaigns")
data class CampaignEntity(
    @PrimaryKey
    @ColumnInfo(name = "mid")
    val mid :Int? = 0,
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "status_id")
    val statusId: Int?,
    @ColumnInfo(name = "cc_id")
    val ccId: Int?,
    @ColumnInfo(name = "brand_id")
    val brandId: Int?,
    @ColumnInfo(name = "questionair_created")
    val questionairCreated: Int?,
    @ColumnInfo(name = "campaign_name")
    val campaignName: String?,
    @ColumnInfo(name = "campaign_description")
    val campaignDescription: String?,
    @ColumnInfo(name = "campaign_start_date")
    val campaignStartDate: String?,
    @ColumnInfo(name = "campaign_end_date")
    val campaignEndDate: String?,
    @ColumnInfo(name = "logo_id")
    val logo_id: Int?,
    @ColumnInfo(name = "created_by")
    val createdBy: String?,
    @ColumnInfo(name = "updated_by")
    val updatedBy: String?,
    @ColumnInfo(name = "is_deleted")
    val isDeleted: String?,
    @ColumnInfo(name = "deleted_by")
    val deletedBy: String?,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: String?,
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null,
    @ColumnInfo(name = "updated_at")
    var updatedAt: String? = null
)