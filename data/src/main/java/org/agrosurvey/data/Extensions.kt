package org.agrosurvey.data

import com.google.gson.GsonBuilder
import timber.log.Timber

object Logs {


    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun logCat(content: Any, pretty: Boolean = false) {
        val message = if (pretty) {
            gson.toJson(content)
        } else content.toString()
        Timber.i(message)
    }


    enum class DebugLevel {
        Info, Error
    }
}

