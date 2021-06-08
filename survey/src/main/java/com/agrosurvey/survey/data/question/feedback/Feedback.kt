package com.agrosurvey.survey.data.question.feedback

import androidx.room.Entity
import com.agrosurvey.survey.data.LatLng
import com.agrosurvey.survey.data.Syncable
import java.io.Serializable
import java.time.OffsetDateTime

@Entity(primaryKeys = ["ID"])
class Feedback (var surveyId: String,
                var createdOn: OffsetDateTime? = OffsetDateTime.now(),
                var lastUpdateOn: OffsetDateTime? = OffsetDateTime.now(),
                var startTime: OffsetDateTime? = OffsetDateTime.now(),
                var endTime: OffsetDateTime? = null,
                var startCoordinate: LatLng? = null,
                var endCoordinate: LatLng? = null,
                var deviceID: String? = null
                ) : Syncable(), Serializable