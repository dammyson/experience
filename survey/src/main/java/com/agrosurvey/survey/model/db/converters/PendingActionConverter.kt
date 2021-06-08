package com.agrosurvey.survey.model.db.converters

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.agrosurvey.survey.data.PendingAction

class PendingActionConverter {
    @TypeConverter
    fun toPendingAction(value: String?): PendingAction? {
        return when(value){
            "nothing" -> PendingAction.NOTHING
            "upload" -> PendingAction.UPLOAD
            "delete" -> PendingAction.DELETE
            else -> PendingAction.NOTHING
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromPendingAction(pendingAction: PendingAction?): String? {
        return  pendingAction?.action
    }
}