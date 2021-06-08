package org.agrosurvey.forms.rendering

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.agrosurvey.Logs
import org.agrosurvey.domain.entities.get.Question
import org.agrosurvey.forms.DummyQuestions.addDummyCheckBoxQuestion
import org.agrosurvey.forms.DummyQuestions.addDummyRadioBoxQuestion
import org.agrosurvey.forms.DummyQuestions.addDummySelectBoxQuestion
import org.agrosurvey.forms.FormsViewModel
import org.agrosurvey.forms.R
import org.agrosurvey.forms.field.FieldsGenerator
import org.agrosurvey.forms.widget.OnNextClickListener
import org.agrosurvey.forms.widget.Page

private const val ARG_QUESTIONS = "questions"
private const val ARG_QUESTION_TYPES = "question_types"
private const val ARG_SEQUENCE = "page_sequence"
private const val ARG_IS_LAST_PAGE = "is_last_page"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentFormPage.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class FragmentFormPage : Fragment(), OnNextClickListener {

    private val viewModel by activityViewModels<FormsViewModel>()

    private val fieldGenerator by lazy { FieldsGenerator(viewModel.questionTypes()) }

    private var pageSequence: Int = -1
    private var isLastPage: Boolean = false

    private lateinit var pageView: Page

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pageSequence = it.getInt(ARG_SEQUENCE)
            isLastPage = it.getBoolean(ARG_IS_LAST_PAGE, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_survey_page, container, false)
        pageView = root.findViewById(R.id.page)
        return root
    }

    fun getPageIndex(): Int = pageSequence

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getQuestions().forEach { q ->

            fieldGenerator.createField(pageView.context, q) { fieldView, enumFieldType ->
                pageView.addField(fieldView as View)
                pageView.invalidate()

            }
        }

        pageView.setOnNextClickListener(this)
        val textRes: Int = if (isLastPage) R.string.submit else R.string.next
        pageView.setButtonText(textRes)

        Logs.logCat(
            "Page $pageSequence is  ${if (isLastPage) "" else "not "}the last page"
        )
    }

    private fun handleOnNextClick() {
        viewModel.goToNextPage(current = pageSequence, isLastPage = isLastPage)
    }


    override fun onResume() {
        super.onResume()
        Logs.logCat("Page $pageSequence has ${getQuestions().size} questions")
    }

    private fun getQuestions(): List<Question> {
        val questions = mutableListOf<Question>()
        questions.addAll(viewModel.getQuestionsForPage(this))

        questions.add(addDummySelectBoxQuestion())
        questions.add(addDummyCheckBoxQuestion())
        questions.add(addDummyRadioBoxQuestion())

        return questions.sortedBy { it.sequence }
    }

    override fun onNext() {
        handleOnNextClick()
    }

    companion object {
        private const val FORM_PAGE = "Form page :"

        @JvmStatic
        fun newInstance(pageSequence: Int, isLastPage: Boolean) =
            FragmentFormPage().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SEQUENCE, pageSequence)
                    putBoolean(ARG_IS_LAST_PAGE, isLastPage)
                }
            }
    }


}