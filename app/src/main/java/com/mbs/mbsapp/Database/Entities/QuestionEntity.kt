package com.mbs.mbsapp.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey
    @ColumnInfo(name = "mid")
    val mid: Int?,
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "questionnaire_id")
    val questionnaireId: Int?,
    @ColumnInfo(name = "question_section_id")
    val questionSectionId: Int?,
    @ColumnInfo(name = "question_section_name")
    val questionSectionName: String?, // Change this type to appropriate type (e.g., String, Int, etc.)
    @ColumnInfo(name = "question")
    val question: String?,
    @ColumnInfo(name = "is_media_allowed")
    val isMediaAllowed: Int?,
    @ColumnInfo(name = "media_count")
    val mediaCount: Int? = 0,
    @ColumnInfo(name = "marks")
    var marks: Int?, // Change this type to appropriate type (e.g., Int, Long, etc.)
    @ColumnInfo(name = "marksRecieved")
    var marksRecieved: Int?, // Change this type to appropriate type (e.g., Int, Long, etc.)
    @ColumnInfo(name = "answere_comment")
    var answerComment: String?, // Convert to appropriate type if necessary
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
    var updatedAt: String? = null,
    @ColumnInfo(name = "media1")
    var media1: String?,
    @ColumnInfo(name = "media2")
    var media2: String?,
    @ColumnInfo(name = "media3")
    var media3: String?,
    @ColumnInfo(name = "media4")
    var media4: String?
)