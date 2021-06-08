package org.agrosurvey

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import com.agrosurvey.R
import com.fabricethilaw.sonarnet.InternetStatus
import com.fabricethilaw.sonarnet.SonarNet
import com.google.android.material.snackbar.Snackbar
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


fun Context.executeWithInternet(uiFeedbackAnchor: View, task: () -> Unit) {
    SonarNet.ping { networkState ->
        if (networkState == InternetStatus.INTERNET) task() else {
            Handler(Looper.getMainLooper()).post {
                Snackbar.make(
                    uiFeedbackAnchor,
                    getString(R.string.no_internet_connection),
                    Snackbar.LENGTH_LONG
                ).show()
            }

        }
    }
}

fun OffsetDateTime.toOffsetDateTimeString(format : DateTimeFormatter = Constants.SURVEY_RICH_DATETIME_PATTERN): String? {
    return  this.toLocalDateTime()?.format(format)
}