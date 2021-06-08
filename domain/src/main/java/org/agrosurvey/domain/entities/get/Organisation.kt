package org.agrosurvey.domain.entities.get

import com.google.gson.annotations.SerializedName

data class Organisation(

    @field:SerializedName("ext_organisation_id")
    val extOrganisationId: Any? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("phone_number")
    val phoneNumber: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: Any? = null,

    @field:SerializedName("email")
    val email: String? = null
)
