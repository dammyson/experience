package com.agrosurvey.survey


import androidx.paging.DataSource
import com.agrosurvey.survey.data.LatLng
import com.agrosurvey.survey.data.mappers.toDbFeedBack
import com.agrosurvey.survey.data.question.feedback.Feedback
import com.agrosurvey.survey.data.question.feedback.FeedbackAndSurvey
import com.agrosurvey.survey.data.question.feedback.FeedbackPayload
import com.agrosurvey.survey.data.question.feedback.FeedbackPayloadError
import com.agrosurvey.survey.data.remote.NetworkResponse
import com.agrosurvey.survey.data.survey.Survey
import com.agrosurvey.survey.di.Scopes
import com.agrosurvey.survey.domain.FeedbackInteractor
import com.agrosurvey.survey.domain.SurveyInteractor
import toothpick.Toothpick
import javax.inject.Inject





class Survey(private val survey: Survey) {


    @Inject
    lateinit var surveyInteractor: SurveyInteractor

    @Inject
    lateinit var feedbackInteractor: FeedbackInteractor


    init {
        Toothpick.inject(this, Toothpick.openScopes(Scopes.APP_SCOPE, Scopes.NETWORK_SCOPE))
    }

    fun title() = survey.title
    fun surveyId() = survey.id


    fun getFeedBacks(): DataSource.Factory<Int, FeedbackAndSurvey> {
        return surveyInteractor.getFeedBacksForSurvey(survey.id)
    }

    suspend fun getAllFeedbacks(): List<Feedback> {
        return surveyInteractor.getAllFeedBacksForSurvey(survey.id)
    }

    /**
     * Create a feedback for a survey
     *
     *
     * @param surveyId  The survey ID
     * @return the created feedback
     */
    suspend fun createFeedBackForSurvey(startLocation: LatLng, deviceID: String): FeedbackAndSurvey {
        return surveyInteractor.createFeedBackForSurvey(
            startLocation = startLocation,
            surveyId = survey.id,
            deviceId = deviceID
        )
    }

    suspend fun fetchFeedbacksForSurvey(): NetworkResponse<FeedbackPayload, FeedbackPayloadError> {
        return surveyInteractor.fetchFeedbacksForSurvey(survey.id)
    }

    suspend fun insertOrUpdateFeedBacks(surveyId: String, feedBacks: FeedbackPayload) {
        feedbackInteractor.insertOrUpdateFeedBacks(surveyId, feedBacks.feedbacks)
    }


}