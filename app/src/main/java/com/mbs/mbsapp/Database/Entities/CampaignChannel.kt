package com.mbs.mbsapp.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "campaign_channels")
data class CampaignChannel(
    @PrimaryKey
    @ColumnInfo(name = "mid")
    val mid: Int?,

    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "status_id")
    val statusId: Int?,

    @ColumnInfo(name = "cc_name")
    val ccName: String?,

    @ColumnInfo(name = "cc_store_level")
    val ccStoreLevel: Int? ,

    @ColumnInfo(name = "created_by")
    val createdBy: String?,

    @ColumnInfo(name = "updated_by")
    val updatedBy: String?,

    @ColumnInfo(name = "is_deleted")
    val isDeleted: String?,

    @ColumnInfo(name = "deleted_by")
    val deletedBy: String?,

    @ColumnInfo(name = "deleted_at")
    val deletedAt: String?, // Change the data type if needed

    @ColumnInfo(name = "created_at")
    val createdAt: String?, // Change the data type if needed

    @ColumnInfo(name = "updated_at")
    val updatedAt: String? // Change the data type if needed
)