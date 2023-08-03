package com.mbs.mbsapp.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stores")
data class StoreEntity(
    @PrimaryKey
    @ColumnInfo(name = "mid")
    val mid :Int? = 0,
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "location_id")
    val locationId: Int?,
    @ColumnInfo(name = "store_name")
    val storeName: String?,
    @ColumnInfo(name = "store_code")
    val storeCode: String?,
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