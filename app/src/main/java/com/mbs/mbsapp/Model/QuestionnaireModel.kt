package com.mbs.mbsapp.Model

import com.google.gson.annotations.SerializedName

data class QuestionnaireModel(

    @SerializedName("questionnaires") var questionnaires: ArrayList<Questionnaires> = arrayListOf(),
    @SerializedName("questions") var questions: ArrayList<Questions> = arrayListOf()
) {
    data class Questions(

        @SerializedName("id") var id: Int? = null,
        @SerializedName("questionnaire_id") var questionnaireId: Int? = null,
        @SerializedName("question_section_id") var questionSectionId: Int? = null,
        @SerializedName("question_section_name") var questionSectionName: String? = null,
        @SerializedName("question") var question: String? = null,
        @SerializedName("is_media_allowed") var isMediaAllowed: Int? = null,
        @SerializedName("media_count") var mediaCount: Int? = null,
        @SerializedName("marks") var marks: Int? = 0,
        @SerializedName("marksreceived") var marksReceived: Int? = 0,
        @SerializedName("created_by") var createdBy: String? = null,
        @SerializedName("updated_by") var updatedBy: String? = null,
        @SerializedName("is_deleted") var isDeleted: String? = null,
        @SerializedName("deleted_by") var deletedBy: String? = null,
        @SerializedName("deleted_at") var deletedAt: String? = null,
        @SerializedName("created_at") var createdAt: String? = null,
        @SerializedName("updated_at") var updatedAt: String? = null

    )

    data class Questionnaires(

        @SerializedName("id") var id: Int? = null,
        @SerializedName("questionnair_name") var questionnairName: String? = null,
        @SerializedName("campaign_id") var campaignId: Int? = null,
        @SerializedName("out_of") var outOf: Int? = null,
        @SerializedName("questions_count") var questionsCount: Int? = null,
        @SerializedName("created_by") var createdBy: String? = null,
        @SerializedName("updated_by") var updatedBy: String? = null,
        @SerializedName("is_deleted") var isDeleted: String? = null,
        @SerializedName("deleted_by") var deletedBy: String? = null,
        @SerializedName("deleted_at") var deletedAt: String? = null,
        @SerializedName("created_at") var createdAt: String? = null,
        @SerializedName("updated_at") var updatedAt: String? = null

    )
}