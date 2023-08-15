package com.mbs.mbsapp.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_sections")
data class QuestionSectionEntity(
    @PrimaryKey
    @ColumnInfo(name = "mid")
    val mid: Int?,
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "section_name")
    val sectionName: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: String?, // Convert to appropriate type if necessary
    @ColumnInfo(name = "updated_at")
    val updatedAt: String? // Convert to appropriate type if necessary
)