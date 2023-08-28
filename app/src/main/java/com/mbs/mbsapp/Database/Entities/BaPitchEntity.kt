package com.mbs.mbsapp.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ba_pitches")
data class BaPitchEntity(
    @PrimaryKey
    @ColumnInfo(name = "mid")
    val mid: Int?,
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "ba_id")
    val baId: Int?,
    @ColumnInfo(name = "bap_media_original_name")
    val bapMediaOriginalName: String?,
    @ColumnInfo(name = "bap_path")
    val bapPath: String?,
    @ColumnInfo(name = "bap_media_name")
    val bapMediaName: String?, // Convert to appropriate type if necessary
    @ColumnInfo(name = "submited_at")
    val submittedAt: String?, // Convert to appropriate type if necessary
    @ColumnInfo(name = "submited_by")
    val submittedBy: Int?,
    @ColumnInfo(name = "created_at")
    val createdAt: String?, // Convert to appropriate type if necessary
    @ColumnInfo(name = "updated_at")
    val updatedAt: String? // Convert to appropriate type if necessary
)
