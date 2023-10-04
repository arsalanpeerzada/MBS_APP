package com.mbs.mbsapp.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_logs")
data class ActivityLog(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mid")
    val mid: Int?,

    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "serverid")
    val serverid: Int?,

    @ColumnInfo(name = "activity_id")
    val activityId: Int?,

    @ColumnInfo(name = "campaign_id")
    val campaignId: Int?,

    @ColumnInfo(name = "brand_id")
    val brandId: Int?,

    @ColumnInfo(name = "user_id")
    val userId: Int?,

    @ColumnInfo(name = "isSync")
    val isSync: Int?,

    @ColumnInfo(name = "activity_detail_code")
    val activity_detail_code: String?,

    @ColumnInfo(name = "activity_start_date")
    val activityStartDate: String?,

    @ColumnInfo(name = "activity_start_time")
    val activityStartTime: String?,

    @ColumnInfo(name = "device_location_lat")
    val deviceLocationLat: String?,

    @ColumnInfo(name = "device_location_long")
    val deviceLocationLong: String?,

    @ColumnInfo(name = "start_activity_tasks_completed")
    val startActivityTasksCompleted: Int? = 0,

    @ColumnInfo(name = "is_questionnaire_completed")
    val isQuestionnaireCompleted: Int? = 0,

    @ColumnInfo(name = "stock_entry_completed")
    val stockEntryCompleted: Int? = 0,

    @ColumnInfo(name = "store_picture_completed")
    val storePictureCompleted: Int? = 0,

    @ColumnInfo(name = "ba_checklist_completed")
    val baChecklistCompleted: Int? = 0,

    @ColumnInfo(name = "all_task_completed")
    val allTaskCompleted: Int? = 0,

    @ColumnInfo(name = "end_activity_tasks_completed")
    val endActivityTasksCompleted: Int? = 0,

    @ColumnInfo(name = "activity_end_date")
    val activityEndDate: String?,

    @ColumnInfo(name = "activity_end_time")
    val activityEndTime: String?,

    @ColumnInfo(name = "created_at")
    val createdAt: String?, // Change the data type if needed

    @ColumnInfo(name = "updated_at")
    val updatedAt: String? // Change the data type if needed
)

