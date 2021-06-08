package com.agrosurvey.survey.listeners

import com.agrosurvey.survey.data.Section

interface FieldListener {
    fun onHideField(section : Section, Field: Int)
}