package com.mbs.mbsapp.Model

import com.google.gson.annotations.SerializedName

data class AnswerModel(
    @SerializedName("answeres[ans][]") var ans: String? = null,
    @SerializedName("answeres[comment][]") var comment: String? = null,
    @SerializedName("answeres[media_attached][]") var media_attached: Int? = null,
    @SerializedName("answeres[media_count][]") var media_count: Int? = null,
    @SerializedName("answeres[submitted_at][]") var submitted_at: String? = null,
)
