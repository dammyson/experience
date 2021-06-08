package com.agrosurvey.survey.data

import java.io.Serializable

open class Syncable (var ID: Int = if(System.currentTimeMillis().toInt() > 0) -System.currentTimeMillis().toInt() else System.currentTimeMillis().toInt(),
                    var pendingAction: PendingAction =  PendingAction.UPLOAD
) : Serializable