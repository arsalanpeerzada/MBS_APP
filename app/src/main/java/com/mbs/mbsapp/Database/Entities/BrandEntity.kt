package com.mbs.mbsapp.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "brands")
data class BrandEntity(
    @PrimaryKey
    @ColumnInfo(name = "mid")
    val mid: Int? = 0,
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "status_id")
    val statusId: Int?,
    @ColumnInfo(name = "brand_name")
    val brandName: String?,
    @ColumnInfo(name = "brand_description")
    val brandDescription: String?,
    @ColumnInfo(name = "brand_logo_id")
    val brandLogoId: Int?,
    @ColumnInfo(name = "brand_primary_color")
    val brandPrimaryColor: String?,
    @ColumnInfo(name = "brand_secondary_color")
    val brandSecondaryColor: String?,
    @ColumnInfo(name = "created_by")
    val createdBy: String?,
    @ColumnInfo(name = "updated_by")
    val updatedBy: String?,
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Int?,
    @ColumnInfo(name = "deleted_by")
    val deletedBy: String?,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: String?,
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null,
    @ColumnInfo(name = "updated_at")
    var updatedAt: String? = null,
    @ColumnInfo(name = "brand_logo_path")
    var picturepath: String? = null,
    @ColumnInfo(name = "brand_logo_name")
    var pictureName: String? = null
)