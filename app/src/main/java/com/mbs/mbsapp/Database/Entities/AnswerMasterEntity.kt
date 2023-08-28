package com.mbs.mbsapp.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answere_masters")
data class AnswerMasterEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "questionnaire_id")
    val questionnaireId: Int?,
    @ColumnInfo(name = "campaign_id")
    val campaignId: Int?,
    @ColumnInfo(name = "activitylogID")
    val activitylogID: Int?,
    @ColumnInfo(name = "activity_id")
    val activityId: Int?,
    @ColumnInfo(name = "submited_by")
    val submittedBy: Int?,
    @ColumnInfo(name = "submited_at")
    val submittedAt: String?, // Convert to appropriate type if necessary
    @ColumnInfo(name = "current_status")
    val currentStatus: String?, // Convert to appropriate type if necessary
    @ColumnInfo(name = "created_at")
    val createdAt: String?, // Convert to appropriate type if necessary
    @ColumnInfo(name = "updated_at")
    val updatedAt: String? // Convert to appropriate type if necessary
)