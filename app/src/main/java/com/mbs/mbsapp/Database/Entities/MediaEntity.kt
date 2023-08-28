package com.mbs.mbsapp.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Media")
data class MediaEntity(
    @PrimaryKey
    @ColumnInfo(name = "mid")
    val mid: Int? = 0,
    @ColumnInfo(name = "form_id")
    val form_id: Int?,
    @ColumnInfo(name = "form_name")
    val form_name: String?,
    @ColumnInfo(name = "activity_log_id")
    val activity_log_id: Int?,
    @ColumnInfo(name = "data_id")
    val data_id: Int?,
    @ColumnInfo(name = "data_name")
    val data_name: String?,
    @ColumnInfo(name = "media_type")
    val media_type: String?,
    @ColumnInfo(name = "media_path")
    val media_path: String?,
    @ColumnInfo(name = "media_name")
    val media_name: String?,
    @ColumnInfo(name = "is_sync")
    val is_sync: String?,
    @ColumnInfo(name = "sync_message")
    val sync_message: String?,
    @ColumnInfo(name = "create_date")
    val create_date: String?,
    @ColumnInfo(name = "sync_id")
    val sync_id: String?,


)