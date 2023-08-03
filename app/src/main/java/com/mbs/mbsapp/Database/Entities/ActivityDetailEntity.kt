package com.mbs.mbsapp.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_detials")
data class ActivityDetailEntity(
    @PrimaryKey
    @ColumnInfo(name = "mid")
    val mid :Int? = 0,
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "activity_master_id")
    val activityMasterId: Long,
    @ColumnInfo(name = "city_id")
    val cityId: Long,
    @ColumnInfo(name = "location_id")
    val locationId: Long,
    @ColumnInfo(name = "store_id")
    val storeId: Long?,
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null,
    @ColumnInfo(name = "updated_at")
    var updatedAt: String? = null
)