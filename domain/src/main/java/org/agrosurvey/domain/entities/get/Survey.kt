package org.agrosurvey.domain.entities.get

import com.google.gson.annotations.SerializedName

data class Survey(

    @field:SerializedName("default_survey_localization")
    val defaultSurveyLocalization: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("deployment_channels")
    val deploymentChannels: List<DeploymentChannelsItem?>? = null,

    @field:SerializedName("survey_localization")
    val surveyLocalization: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: Any? = null,

    @field:SerializedName("created_by_user")
    val createdByUser: CreatedByUser? = null,

    @field:SerializedName("organisation_id")
    val organisationId: String? = null,

    @field:SerializedName("sequence")
    val sequence: Int? = null,

    @field:SerializedName("gist")
    val gist: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("survey_localizations")
    val surveyLocalizations: List<String?>? = null,

    @field:SerializedName("id")
    val id: String? = null
)

data class DeploymentChannelsItem(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("slug")
    val slug: String? = null
)

data class CreatedByUser(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)
