package com.mbs.mbsapp.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_stocks")
data class ProductStock(
    @PrimaryKey
    @ColumnInfo(name = "mid")
    val mid: Int?,
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "campaign_id")
    val campaignId: Int?,
    @ColumnInfo(name = "activity_id")
    val activityId: Int?,
    @ColumnInfo(name = "activity_detail_id")
    val activityDetailId: Int?,
    @ColumnInfo(name = "product_id")
    val productId: Int?,
    @ColumnInfo(name = "count")
    val count: Int?,
    @ColumnInfo(name = "created_by")
    val createdBy: String?,
    @ColumnInfo(name = "updated_by")
    val updatedBy: String?,
    @ColumnInfo(name = "is_deleted")
    val isDeleted: String?,
    @ColumnInfo(name = "deleted_by")
    val deletedBy: String?,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: String?, // Convert to appropriate type if necessary
    @ColumnInfo(name = "created_at")
    val createdAt: String?, // Convert to appropriate type if necessary
    @ColumnInfo(name = "updated_at")
    val updatedAt: String? // Convert to appropriate type if necessary
)
