package com.agrosurvey.survey.domain


import androidx.paging.DataSource
import com.agrosurvey.survey.data.LatLng
import com.agrosurvey.survey.data.question.feedback.Feedback
import com.agrosurvey.survey.data.question.FieldType
import com.agrosurvey.survey.data.Response
import com.agrosurvey.survey.data.ResponseValue
import com.agrosurvey.survey.data.mappers.*
import com.agrosurvey.survey.data.question.Question
import com.agrosurvey.survey.data.question.QuestionAndType
import com.agrosurvey.survey.data.question.QuestionError
import com.agrosurvey.survey.data.question.QuestionIn
import com.agrosurvey.survey.data.question.feedback.*
import com.agrosurvey.survey.data.question.option.Option
import com.agrosurvey.survey.data.remote.NetworkResponse
import com.agrosurvey.survey.data.survey.Survey
import com.agrosurvey.survey.data.survey.SurveyError
import com.agrosurvey.survey.data.survey.SurveyIn
import com.agrosurvey.survey.di.Scopes
import com.agrosurvey.survey.exception.NotConnectedException
import com.agrosurvey.survey.model.db.FeedBackDao
import com.agrosurvey.survey.model.db.QuestionDao
import com.agrosurvey.survey.model.db.SurveyDao
import com.agrosurvey.survey.model.repository.SurveyRepository
import com.agrosurvey.survey.model.system.NetworkHelper
import toothpick.Toothpick
import java.util.*
import javax.inject.Inject

class SurveyInteractor {


    //Surveys
    @Inject
    lateinit var repository: SurveyRepository

    @Inject
    lateinit var surveyDao: SurveyDao

    @Inject
    lateinit var networkHelper: NetworkHelper

    //QUESTIONS
    @Inject
    lateinit var questionDao: QuestionDao

    @Inject
    lateinit var feedBackDao: FeedBackDao

    init {
        Toothpick.inject(this, Toothpick.openScopes(Scopes.APP_SCOPE, Scopes.NETWORK_SCOPE))
    }


    suspend fun getSurveysFromNetwork(page: Int = 1, pageSize: Int = 20): NetworkResponse<List<SurveyIn>, SurveyError> {
        if(networkHelper.isConnected().not()){
            throw NotConnectedException()
        }
        return repository.getSurveys()
    }

    suspend fun getSurveyQuestions(surveyId : String, page: Int = 1, pageSize: Int = 20): NetworkResponse<List<QuestionIn>, QuestionError> {
        if(networkHelper.isConnected().not()){
            throw NotConnectedException()
        }
        return repository.getSurveyQuestions(surveyId)
    }

    fun getSurveysFromDatabase(query : String = ""): DataSource.Factory<Int, Survey> {
        return  surveyDao.getSurveys("%$query%")
    }

    fun getFeedBacksForSurvey(surveyId : String): DataSource.Factory<Int, FeedbackAndSurvey> {
        return surveyDao.getFeedBacksForSurvey(surveyId)
    }


    suspend fun insertOrUpdateSurvey(items: List<SurveyIn>): List<Long> {
        return surveyDao.insert(items.map {it.toSurveyDb()})
    }


    fun getSurveyQuestionsFromDB(surveyId : String): DataSource.Factory<Int, Question> {
        return surveyDao.getQuestionsForSurvey(surveyId)
    }

    suspend fun getAllSurveyQuestionsFromDB(surveyId : String): List<QuestionAndType> {
        return surveyDao.getAllQuestionsForSurvey(surveyId)
    }

    suspend fun getSurveyCount() : Int {
        return surveyDao.getSurveysCount()
    }

    suspend fun insertOrUpdateQuestions(items: List<QuestionIn>): List<Long> {
        return questionDao.insert(items.map {it.toQuestionDb()})
    }

    suspend fun getSurvey(surveyId: String): Survey {
        return surveyDao.getSurvey(surveyId)
    }

    suspend fun createFeedBackForSurvey(
        surveyId: String,
        startLocation: LatLng,
        deviceId: String
    ): FeedbackAndSurvey {
        val feedbackId = feedBackDao.insert(listOf(Feedback(
            surveyId = surveyId,
            deviceID = deviceId,
            startCoordinate = startLocation
        )))[0]
        return feedBackDao.getFeedBackById(feedbackId)
    }

    suspend fun saveResponse(question: QuestionAndType, feedbackId: Int, value: Any): List<Long> {

        val ResponseValue = when(question.type.slug){
            is FieldType.ShortText -> ResponseValue.ShortText(value.toString())
            is FieldType.LongText -> ResponseValue.LongText(value.toString())
            is FieldType.SelectBox -> ResponseValue.SelectBox(value as Option)
            is FieldType.RadioButton -> ResponseValue.RadioButton(value as Option)
            is FieldType.CheckedBox -> ResponseValue.CheckedBox(value.toString().toBoolean())
            is FieldType.ReferenceDataResponses -> ResponseValue.ReferenceDataResponses(value.toString().toInt())
            is FieldType.AutoGeneratedId -> ResponseValue.AutoGeneratedId(value.toString())
            is FieldType.Integer -> ResponseValue.Integer(value.toString().toInt())
            is FieldType.Decimal -> ResponseValue.Decimal(value.toString().toDouble())
            is FieldType.PhoneNumber -> ResponseValue.PhoneNumber(value.toString())
            is FieldType.DateTimePicker -> ResponseValue.DateTimePicker(value.toString().toOffsetDateTime())
            is FieldType.DatePicker -> ResponseValue.DatePicker(value.toString().toDate())
            //is FieldType.TimePicker -> ResponseValue.TimePicker(value.toString())
            is FieldType.MonthPicker -> ResponseValue.MonthPicker(value.toString())
            is FieldType.DayOfWeekPicker -> ResponseValue.DayOfWeekPicker(value.toString())
            //is FieldType.Signature -> ResponseValue.Signature(value.toString())
            is FieldType.Image -> ResponseValue.Image(value.toString())
            is FieldType.Audio -> ResponseValue.Audio(value.toString())
            //is FieldType.PolyGonPlot -> ResponseValue.PolyGonPlot(value.toString())
            else -> ResponseValue.None
        }

        return feedBackDao.insertResponse(listOf(
            Response(question.question.id!!, feedbackId, ResponseValue))
        )
    }

    suspend fun getAllFeedBacksForSurvey(id: String): List<Feedback> {
        return surveyDao.getAllFeedBacksForSurvey(id)
    }

    suspend fun flushSurveys() {
        surveyDao.flushSurveys()
    }

    suspend fun fetchFeedbacksForSurvey(id: String): NetworkResponse<FeedbackPayload, FeedbackPayloadError> {
        return repository.getFeedBackForSurvey(id)
    }

}