package com.agrosurvey.survey

import android.content.Context
import android.util.Log
import androidx.paging.DataSource
import com.agrosurvey.survey.data.question.QuestionAndType
import com.agrosurvey.survey.data.remote.NetworkResponse
import com.agrosurvey.survey.data.survey.Survey
import com.agrosurvey.survey.di.Scopes
import com.agrosurvey.survey.di.modules.AppModule
import com.agrosurvey.survey.di.modules.NetworkModule
import com.agrosurvey.survey.domain.FeedbackInteractor
import com.agrosurvey.survey.domain.QuestionInteractor
import com.agrosurvey.survey.domain.SurveyInteractor
import com.agrosurvey.survey.model.system.PreferenceHelper
import kotlinx.coroutines.*
import toothpick.Toothpick
import java.io.Closeable
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MySurvey(val context : Context?, val baseUrl: String? = null, val token: String?) : CoroutineScope, Closeable {

    @Inject
    lateinit var surveyInteractor: SurveyInteractor

    @Inject
    lateinit var feedbackInteractor: FeedbackInteractor

    @Inject
    lateinit var questionInteractor: QuestionInteractor

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private val supervisorJob = SupervisorJob()

    init {
        Toothpick.inject(this, Toothpick.openScopes(Scopes.APP_SCOPE, Scopes.NETWORK_SCOPE))
        preferenceHelper.setAccessToken(token!!)
        preferenceHelper.setAuthStatus(true)
    }


    /**
     * Helps retrieve a survey instance from local database from its ID
     *
     * @param surveyId  The survey ID
     * @return an instance of the Survey
     *
     */
    suspend fun getSurvey(surveyId: String): com.agrosurvey.survey.Survey {
        return Survey(surveyInteractor.getSurvey(surveyId))

    }


    /**
     * Retrieve a pged list of surveys from the database
     *
     * @return a paged list of surveys
     *
     */
    fun getSurveys(query : String = ""): DataSource.Factory<Int, Survey> {
        return surveyInteractor.getSurveysFromDatabase(query)
    }


    /**
     * Get the number of survey available in the database
     *
     * @return the number of cached surveys
     */
    suspend fun surveysCount() : Int =  surveyInteractor.getSurveyCount()


    /**
     * Fetch every surveys provided by the web API and save them
     * to the local database. The surveys are retrieved and for each of them,
     * we fetch and save their questions to. After calling this method, the inner
     * local database is populated with surveys and their related questions.
     *
     * We make use of @see #fetchSurveyQuestions method to fetch and save questions
     * for a survey
     */
    suspend fun fetchAndSaveSurveys() {

        val apiResponse = surveyInteractor.getSurveysFromNetwork()
        Log.e("TAG", apiResponse.toString())
        when(apiResponse) {
            is NetworkResponse.Success -> {
                val surveys = apiResponse.body
                surveyInteractor.insertOrUpdateSurvey(surveys)

                for (survey in surveys){
                    fetchSurveyQuestions(survey.id!!)
                    fetchSurveyFeedbacks(survey.id!!)
                }
            }
            is NetworkResponse.ApiError -> {
                Log.e("TAG", "ApiError ${apiResponse.body}")
            }
            is NetworkResponse.NetworkError -> {
                Log.e("TAG", "NetworkError")
            }
            is NetworkResponse.UnknownError -> {
                Log.e("TAG Error", "UnknownError")
            }
        }

    }

    private suspend fun fetchSurveyFeedbacks(id: String) {

        val apiResponse = surveyInteractor.fetchFeedbacksForSurvey(id)

        when(apiResponse) {
            is NetworkResponse.Success -> {
                feedbackInteractor.insertOrUpdateFeedBacks(surveyId = id, feedbacks = apiResponse.body.feedbacks)
            }
            is NetworkResponse.ApiError -> {
                Log.e("TAG", "ApiError ${apiResponse.body}")
            }
            is NetworkResponse.NetworkError -> {
                Log.e("TAG", "NetworkError")
            }
            is NetworkResponse.UnknownError -> {
                Log.e("TAG", "UnknownError " + apiResponse.error?.stackTraceToString())
            }
        }

    }


    suspend fun reloadSurveys(){
        surveyInteractor.flushSurveys()
        fetchQuestionsType()
        fetchAndSaveSurveys()
    }

    /**
     * Retrieve and save t local cache list of questions for a survey
     *
     * @param surveyId  The survey ID
     */

    suspend fun fetchSurveyQuestions(surveyId : String){

        val apiResponse = surveyInteractor.getSurveyQuestions(surveyId)

        when(apiResponse) {
            is NetworkResponse.Success -> {

                for(fg in apiResponse.body)
                    //if(fg.questionSkips!=null)
                    Log.e("BODY ", "DbbDD = "+fg.question_skips?.size.toString() )

                surveyInteractor.insertOrUpdateQuestions(apiResponse.body)
            }
            is NetworkResponse.ApiError -> {
                Log.e("TAG", "ApiError ${apiResponse.body}")
            }
            is NetworkResponse.NetworkError -> {
                Log.e("TAG", "NetworkError")
            }
            is NetworkResponse.UnknownError -> {
                Log.e("TAG", "UnknownError " + apiResponse.error?.stackTraceToString())
            }
        }

    }

    suspend fun fetchQuestionsType(){

        val apiResponse = questionInteractor.getQuestionTypeFromNetwork()

        when(apiResponse) {
            is NetworkResponse.Success -> {
                val body = apiResponse.body
                questionInteractor.insertOrUpdateQuestionType(apiResponse.body)
                questionInteractor.insertOrUpdateQuestionTypeGroup(apiResponse.body)
            }
            is NetworkResponse.ApiError -> {
                Log.e("TAG", "ApiError ${apiResponse.body}")
            }
            is NetworkResponse.NetworkError -> {
                Log.e("TAG", "NetworkError")
            }
            is NetworkResponse.UnknownError -> {
                Log.e("TAG", "UnknownError")
            }
        }

    }

    /**
     * Retrieve from local cache the list of questions featured with their types
     * for a survey. @see #QuestionAndType
     *
     *
     * @param surveyId  The survey ID
     * @return A list of survey questions with the questions type.
     */
    suspend fun surveyQuestions(surveyId: String): List<QuestionAndType> {
        return surveyInteractor.getAllSurveyQuestionsFromDB(surveyId)
    }

    data class Builder(var context: Context,
                       var baseUrl: String? = Constants.API_URL,
                       var token: String? = null) {

        fun context(context: Context) = apply { this.context = context }
        fun setUserToken(token : String) = apply { this.token = token }
        fun setBaseUrl(baseUrl : String) = apply { this.baseUrl = baseUrl }

        fun build(): MySurvey {

            val appScope = Toothpick.openScope(Scopes.APP_SCOPE)
            appScope.installModules(AppModule(context))
            val serverScope = Toothpick.openScope(Scopes.NETWORK_SCOPE)
            serverScope.installModules(NetworkModule(baseUrl = baseUrl!!))

            Toothpick.inject(context, appScope)
            Toothpick.inject(context, serverScope)

            return MySurvey(context = context, token = token, baseUrl = baseUrl)
        }
    }


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + supervisorJob


    override fun close() {
        supervisorJob.cancelChildren()
    }



}

