package org.agrosurvey.domain.entities.get

import com.google.gson.annotations.SerializedName

data class ErrorLogin(

	@field:SerializedName("errors")
	val errors: Errors? = null
)

data class Errors(

	@field:SerializedName("password")
	val password: List<String?>? = null,

	@field:SerializedName("email")
	val email: List<String?>? = null
)