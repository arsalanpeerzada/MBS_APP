package com.mbs.mbsapp.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answere_detials")
data class AnswerDetailEntity(
    @PrimaryKey
    @ColumnInfo(name = "mid")
    val mid: Int?,
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "answere_master_id")
    val answerMasterId: Int?,
    @ColumnInfo(name = "activity_id")
    val activity_id: Int?,
    @ColumnInfo(name = "section_id")
    val section_id: Int?,
    @ColumnInfo(name = "activity_detail_id")
    val activity_detail_id: Int?,
    @ColumnInfo(name = "activity_log_id")
    val activity_log_id: Int?,
    @ColumnInfo(name = "questionnaire_id")
    val questionnaire_id: Int?,
    @ColumnInfo(name = "question_id")
    val question_id: Int?,
    @ColumnInfo(name = "answere")
    val answer: String?,
    @ColumnInfo(name = "answere_comment")
    val answerComment: String?, // Convert to appropriate type if necessary
    @ColumnInfo(name = "is_media_attached")
    val isMediaAttached: Int?,
    @ColumnInfo(name = "attached_media_count")
    val attachedMediaCount: Int,
    @ColumnInfo(name = "submited_by")
    val submittedBy: Int?,
    @ColumnInfo(name = "submited_at")
    val submittedAt: String?, // Convert to appropriate type if necessary
    @ColumnInfo(name = "created_at")
    val createdAt: String?, // Convert to appropriate type if necessary
    @ColumnInfo(name = "updated_at")
    val updatedAt: String?, // Convert to appropriate type if necessary
    @ColumnInfo(name = "media1")
    val media1: String?,
    @ColumnInfo(name = "media2")
    val media2: String?,
    @ColumnInfo(name = "media3")
    val media3: String?,
    @ColumnInfo(name = "media4")
    val media4: String?
)