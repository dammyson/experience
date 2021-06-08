package org.agrosurvey.forms

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.agrosurvey.domain.entities.get.Question
import org.agrosurvey.domain.entities.get.QuestionType
import org.agrosurvey.forms.rendering.FragmentFormPage
import org.agrosurvey.forms.rendering.pager.FragmentSurveyPager
import javax.inject.Inject

@HiltViewModel
class FormsViewModel @Inject constructor() : ViewModel() {
    private var maxQuestionsPerPage: Int = 5
    private var surveyTitle = ""
    private var surveyId = ""


    private val questionTypes = MutableLiveData<List<QuestionType>>()
    private val questionMap = mutableMapOf<Int, List<Question>>()

    private var onNextPage = MutableLiveData<Int>()

    fun onNextPageRequested() = onNextPage as LiveData<Int>

    fun questionTypes(): List<QuestionType> {
        return questionTypes.value.orEmpty()
    }

    fun getSurveyPageCount(): Int {
        return questionMap.size
    }


    fun getQuestionsForPage(page: FragmentFormPage): List<Question> {
        return questionMap[page.getPageIndex()].orEmpty()
    }

    fun setQuestionsToRender(
        surveyId: String, surveyTitle: String,
        questions: List<Question>,
        allowedQuestionTypes: List<QuestionType>,
        maxQuestionsPerPage: Int
    ) {
        this.surveyId = surveyId
        this.surveyTitle = surveyTitle
        this.maxQuestionsPerPage = if (maxQuestionsPerPage > 0) maxQuestionsPerPage else 1

        questionTypes.postValue(allowedQuestionTypes)
        createQuestionsMap(maxQuestionsPerPage, questions)
    }

    private fun createQuestionsMap(maxQuestionsPerPage: Int, questions: List<Question>) {
        val max = if (maxQuestionsPerPage > 0) maxQuestionsPerPage else 1
        questions.chunked(max).run {
            forEach {
                questionMap[this.indexOf(it)] = it
            }
        }
    }

    fun goToNextPage(current: Int, isLastPage: Boolean) {
        onNextPage.postValue(current)
    }

    fun reset(requestMaker: Fragment) {
        if (requestMaker is FragmentSurveyPager) {
            onNextPage = MutableLiveData<Int>()
        }
    }

    fun completeSurvey(onFeedbackSaved: () -> Unit, onError: () -> Unit) {
        // todo save the survey's feedback
        onFeedbackSaved()
    }

}

private fun Question.asFieldView() {

}