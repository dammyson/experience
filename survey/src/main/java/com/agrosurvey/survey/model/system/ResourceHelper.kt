package com.agrosurvey.survey.model.system

import android.content.Context
import javax.inject.Inject

class ResourceHelper @Inject constructor(private val context: Context) {
    fun getString(resId : Int) : String = context.getString(resId)
    fun getStringArray(resId: Int) : Array<String> = context.resources.getStringArray(resId)
}