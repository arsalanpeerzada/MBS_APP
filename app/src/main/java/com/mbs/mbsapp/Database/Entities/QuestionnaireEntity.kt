package com.mbs.mbsapp.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questionnaire")
data class QuestionnaireEntity(
    @PrimaryKey
    @ColumnInfo(name = "mid")
    val mid: Int?,
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "questionnair_name")
    val questionnaireName: String?,
    @ColumnInfo(name = "campaign_id")
    val campaignId: Int?,
    @ColumnInfo(name = "out_of")
    val outOf: Int?,
    @ColumnInfo(name = "questions_count")
    val questionsCount: Int?,
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