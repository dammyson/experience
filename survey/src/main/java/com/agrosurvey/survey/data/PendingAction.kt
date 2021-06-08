package com.agrosurvey.survey.data

enum class PendingAction constructor(val action  : String){
    NOTHING("nothing"),
    UPLOAD("upload"),
    DELETE("delete"),
}
