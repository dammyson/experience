package org.agrosurvey.features.survey.ui.surveys

import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.agrosurvey.survey.Listing
import com.agrosurvey.survey.MySurvey
import com.agrosurvey.survey.data.survey.Survey
import kotlinx.coroutines.*
import org.agrosurvey.SingleLiveData
import kotlin.coroutines.CoroutineContext


class SurveysViewModel(val surveyLib : MySurvey) : ViewModel(), CoroutineScope {


    var pagedSurveyList = MutableLiveData<Listing<Survey>>()
    lateinit var surveyCollection : LiveData<PagedList<Survey>>


    val noSurveyFound = MutableLiveData<Boolean>(false)
    val loadingSurvey = MutableLiveData<Boolean>(true)
    val messages = SingleLiveData<Pair<String,Int>>()

    val queryText = MutableLiveData<String?>("")


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
    }


    fun configureSurveyListPaging(){

        val livePaged = Transformations.switchMap(queryText){ query ->
            surveyLib.getSurveys(query!!).toLiveData(config,
                boundaryCallback = object : PagedList.BoundaryCallback<Survey>(){

                    override fun onZeroItemsLoaded() {
                        super.onZeroItemsLoaded()
                        noSurveyFound.value = true
                        loadingSurvey.value = false

                    }

                    override fun onItemAtEndLoaded(itemAtEnd: Survey) {
                        super.onItemAtEndLoaded(itemAtEnd)
                        noSurveyFound.value = false
                        loadingSurvey.value = false

                    }

                })
        }

        pagedSurveyList.value = Listing(
                pagedList = livePaged,
                retry = {},
                refresh = {}
        )

        surveyCollection = pagedSurveyList.switchMap { it.pagedList }

    }

    fun setQueryText(query: String?) {
        //TODO add delay
        queryText.value = query?:""
    }

    fun reload() {


        launch {
            //TODO
            messages.value = START_RELOADING to 0//R.string.msg_start_reloading
            surveyLib.reloadSurveys()
            messages.value = END_RELOADING to 0//R.string.msg_start_reloading

        }

    }

    companion object {
        val PAGE_SIZE = 20
        val END_RELOADING = "END_RELOADING"
        val START_RELOADING = "START_RELOADING"
    }

}


/*
@HiltViewModel
class SurveysViewModel @Inject constructor(
    val repository: SurveyRepository,
    val preferences: PreferenceHelper
) : ViewModel() {
    private val headers by lazy {
        RequestHeaders(authorization = preferences.getAccessToken())
    }

    private val isLoadingSurveyList = MutableLiveData(false)
    private val onFailedSurveyList = MutableLiveData<Pair<String, ApiErrorType>>()
    private val onSucceededSurveyList = MutableLiveData<List<SurveyViewItem>>()

    fun surveyListIsLoading() = isLoadingSurveyList as LiveData<Boolean>
    fun surveyListSucceeded() = onSucceededSurveyList as LiveData<List<SurveyViewItem>>
    fun surveyListFailed() = onFailedSurveyList as LiveData<Pair<String, ApiErrorType>>

    private val isLoadingSurveyQuestions = MutableLiveData(false)
    private val onFailedSurveyQuestions = MutableLiveData<Pair<String, ApiErrorType>>()
    private val onSucceededSurveyQuestions = MutableLiveData<List<Question>>()

    fun surveyQuestionsIsLoading() = isLoadingSurveyQuestions as LiveData<Boolean>
    fun surveyQuestionsSucceeded() = onSucceededSurveyQuestions as LiveData<List<Question>>
    fun surveyQuestionsFailed() =
        onFailedSurveyQuestions as LiveData<Pair<String, ApiErrorType>>


    fun getSurveys() {
        isLoadingSurveyList.postValue(true)
        repository.getSurveys(headers) {
            isLoadingSurveyList.postValue(false)
            when (it) {
                is ApiEmptyResponse -> {
                    val msg = Pair("No content", ApiErrorType.UNKNOWN_ERROR)
                    onFailedSurveyList.postValue(msg)
                    Logs.logCat("getSurveys: $msg", true)
                }
                is ApiErrorResponse -> {
                    val msg = Pair("Error content", it.errorType)
                    onFailedSurveyList.postValue(msg)
                    Logs.logCat("getSurveys: $msg")
                }
                is ApiSuccessResponse -> {
                    val data = it.body.asViewItems()
                    onSucceededSurveyList.postValue(data)
                    Logs.logCat("getSurveys: $data")
                }
            }
        }
    }

    fun getSurveyQuestions(id: String, onSuccess: (List<Question>) -> Unit) {
        repository.getSurveyQuestions(headers, surveyId = id) {
            isLoadingSurveyQuestions.postValue(false)
            when (it) {
                is ApiEmptyResponse -> {
                    val msg = Pair("No content", ApiErrorType.UNKNOWN_ERROR)
                    onFailedSurveyQuestions.postValue(msg)
                    Logs.logCat("getSurveyQuestions: $msg", true)
                }
                is ApiErrorResponse -> {
                    val msg = Pair("Error content", it.errorType)
                    onFailedSurveyQuestions.postValue(msg)
                    Logs.logCat("getSurveyQuestions: $msg")
                }
                is ApiSuccessResponse -> {
                    val data = it.body
                    onSuccess(data)
                    onSucceededSurveyQuestions.postValue(data)
                    Logs.logCat("getSurveyQuestions: $data")
                }
            }
        }
    }

    fun getQuestionTypes(onSuccess: (List<QuestionType>) -> Unit) {
        repository.getSupportedQuestionTypes(headers) {
            when (it) {
                is ApiEmptyResponse -> {
                    val msg = Pair("No content", ApiErrorType.UNKNOWN_ERROR)
                    onFailedSurveyQuestions.postValue(msg)
                    Logs.logCat("getQuestionTypes: $msg", true)
                }
                is ApiErrorResponse -> {
                    val msg = Pair("Error content", it.errorType)
                    onFailedSurveyQuestions.postValue(msg)
                    Logs.logCat("getQuestionTypes: $msg")
                }
                is ApiSuccessResponse -> {
                    val data = it.body
                    onSuccess(data)
                    Logs.logCat("getQuestionTypes: $data")
                }
            }
        }
    }
}
*/