package org.agrosurvey.features.survey.ui.survey

import afu.org.checkerframework.checker.igj.qual.Mutable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agrosurvey.survey.FeedBack
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.agrosurvey.survey.data.question.QuestionAndType
import kotlinx.coroutines.*
import org.agrosurvey.SingleLiveData
import kotlin.coroutines.CoroutineContext


class FeedbackDetailViewModel(val feedBack: FeedBack) : ViewModel(), CoroutineScope {


    var qaDatabaseList = MutableLiveData<List<QuestionAndResponse>>()
    var qaCurrentList = MutableLiveData<ArrayList<QuestionAndResponse>>()
    var title =feedBack.title()

    var isEndOfList = MutableLiveData<Boolean>(false)
    var numberOfQuestion = MutableLiveData<Int>()
    val newQuestion = SingleLiveData<QuestionAndResponse?>()
    val message = SingleLiveData<String?>()

    val lastQuestionAnswered =  MutableLiveData<Pair<QuestionAndType, Any?>>()


    private var job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.e("COROU", e.stackTraceToString())
    }

    fun saveResponse(
        question: QuestionAndType? = null,
        response: Any? = null
    ) {
        launch(getJobErrorHandler() + job) {
            Log.e("SSS", "VM SEND SAVING REQUEST")
            feedBack.saveResponseForQuestion(lastQuestionAnswered.value?.first!!, lastQuestionAnswered.value?.second)

        }
    }

    fun keepNewResponse(
        question: QuestionAndType,
        response: Any?
    ) {
        //if(response != null)
            lastQuestionAnswered.value = question to response
        //else
           // message.value = ""

    }

    init {

        launch(getJobErrorHandler() + job) {
            qaDatabaseList.postValue(feedBack.getQuestions())
            numberOfQuestion.postValue(feedBack.questionsCount())

            if (feedBack.getFirstQuestion() != null){
                newQuestion.postValue(feedBack.getFirstQuestion()!!)
                qaCurrentList.postValue(arrayListOf(feedBack.getFirstQuestion()!!))
            }

            feedBack.questionsListener = object : FeedBack.QuestionListener {
                override fun onNextQuestion(question: QuestionAndResponse?) {
                    if (question != null) {
                        newQuestion.postValue(question)
                        message.postValue("")
                    }else{
                        isEndOfList.postValue(true)
                    }
                }
            }
        }
    }


}

