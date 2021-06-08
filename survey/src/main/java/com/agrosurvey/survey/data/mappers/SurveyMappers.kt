package com.agrosurvey.survey.data.mappers

import com.agrosurvey.survey.data.survey.Survey
import com.agrosurvey.survey.data.survey.SurveyIn


fun SurveyIn.toSurveyDb(): Survey {
    return Survey(
            id = this.id!!,
            organisationId = this.organisationId,
            sequence = this.sequence,
            survey_status_id = this.survey_status_id,
            title = this.title,
            gist = this.gist,
            created_at = this.created_at,
            updated_at = this.updated_at,
            deleted_at = this.deleted_at
    )
}



