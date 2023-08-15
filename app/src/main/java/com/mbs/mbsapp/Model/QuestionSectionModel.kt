package com.mbs.mbsapp.Model

import com.google.gson.annotations.SerializedName

data class QuestionSectionModel(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("section_name") var sectionName: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null
)