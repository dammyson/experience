package org.agrosurvey.features.survey.ui.feedback



import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.agrosurvey.survey.Survey
import kotlinx.coroutines.CoroutineScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.agrosurvey.R
import com.agrosurvey.survey.Listing
import com.agrosurvey.survey.data.LatLng
import com.agrosurvey.survey.data.question.feedback.Feedback
import com.agrosurvey.survey.data.question.feedback.FeedbackAndSurvey
import com.agrosurvey.survey.data.remote.NetworkResponse
import kotlinx.coroutines.*
import org.agrosurvey.SingleLiveData
import kotlin.coroutines.CoroutineContext

class FeedbacksViewModel (val single_survey : Survey,
                          val deviceID: String,
                          val startLocation: LatLng
) : ViewModel(), CoroutineScope {


    var pagedFeedbackList = MutableLiveData<Listing<FeedbackAndSurvey>>()
    lateinit var feedbackCollection : LiveData<PagedList<FeedbackAndSurvey>>


    val noFeedbackFound = MutableLiveData<Boolean>()
    val surveyTitle = MutableLiveData<String>(single_survey.title())
    var messages = SingleLiveData<Pair<String, Int>>()
    var newlyCreatedFeedback = MutableLiveData<FeedbackAndSurvey? >(null)

    val config = PagedList.Config.Builder()
        .setPageSize(PAGE_SIZE)
        .setInitialLoadSizeHint(20)
        .setEnablePlaceholders(true)
        .setMaxSize(100)
        .build()

    private var job =  SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    init {
        configureSurveyListPaging()
        loadFeedBack()

    }

    fun configureSurveyListPaging(){
         val livePaged = single_survey.getFeedBacks().toLiveData(config,
                boundaryCallback = object : PagedList.BoundaryCallback<FeedbackAndSurvey>() {

                    override fun onZeroItemsLoaded() {
                        super.onZeroItemsLoaded()
                        noFeedbackFound.value = true
                    }

                    override fun onItemAtEndLoaded(itemAtEnd: FeedbackAndSurvey) {
                        super.onItemAtEndLoaded(itemAtEnd)
                        noFeedbackFound.value = false
                    }

                })

            pagedFeedbackList.value = Listing(
                pagedList = livePaged,
                retry = {},
                refresh = {}
            )

            feedbackCollection = pagedFeedbackList.switchMap { it.pagedList }

    }



    fun loadFeedBack(){
        launch(getJobErrorHandler() + job) {
            val apiResponse = single_survey.fetchFeedbacksForSurvey()

            when(apiResponse) {
                is NetworkResponse.Success -> {
                    val feedBacks = apiResponse.body
                    single_survey.insertOrUpdateFeedBacks(single_survey.surveyId(), feedBacks)
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
    }


    companion object {
        val PAGE_SIZE = 20
        val ERROR_CREATING_FEEDBACK = "ERROR_CREATING_FEEDBACK"
        val FEEDBACK_CREATED = "FEEDBACK_CREATED"
    }

    fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.e("ERROR", e.stackTraceToString())
        messages.value = ERROR_CREATING_FEEDBACK to R.string.msg
    }
    fun createFeedback(){
        launch(getJobErrorHandler() + job) {
            newlyCreatedFeedback.postValue(
                single_survey.createFeedBackForSurvey(
                    deviceID = deviceID,
                    startLocation = startLocation
                ))
            messages.value = FEEDBACK_CREATED to R.string.feedback_create_msg
        }
    }

}