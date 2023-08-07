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
    val id: Int?,
    @ColumnInfo(name = "activity_master_id")
    val activityMasterId: Int?,
    @ColumnInfo(name = "city_id")
    val cityId: Int?,
    @ColumnInfo(name = "location_id")
    val locationId: Int?,
    @ColumnInfo(name = "store_id")
    val storeId: Int?,
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null,
    @ColumnInfo(name = "updated_at")
    var updatedAt: String? = null
)