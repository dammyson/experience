package org.agrosurvey.features.survey.ui.surveys

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agrosurvey.R
import com.agrosurvey.databinding.FragmentSurveysBinding
import com.agrosurvey.survey.MySurvey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.agrosurvey.Constants
import org.agrosurvey.PreferenceHelper
import org.agrosurvey.features.survey.ui.surveys.adapter.SurveyAdapter
import javax.inject.Inject

@AndroidEntryPoint
class SurveysFragment  @Inject constructor(): Fragment() {


    private lateinit var viewModel: SurveysViewModel
    lateinit var binding: FragmentSurveysBinding
    lateinit var adapter: SurveyAdapter
    lateinit var progressBar: ProgressBar
    lateinit var empty_list_text: TextView

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_surveys, container, false)
        binding.lifecycleOwner = requireActivity()
        progressBar = binding.progressBar
        empty_list_text = binding.emptyListText

        val surveyLib = MySurvey.Builder(requireActivity())
                .setUserToken(preferenceHelper.getToken())
                .setBaseUrl(Constants.API_URL)
                .build()
        viewModel = ViewModelProvider(requireActivity(),
                SurveysVMFactory(surveyLib)).get(SurveysViewModel::class.java)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setQueryText(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setQueryText(newText)
                return true
            }

        })

        binding.reload.setOnClickListener {
            viewModel.reload()
        }


        initRecyclerView()
        initObservables()

        return binding.root
    }

    fun initRecyclerView() {

        adapter = SurveyAdapter(requireActivity())
        val recyclerView = binding.list
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    fun initObservables(){
        viewModel.surveyCollection.observe(requireActivity()) {
            val recyclerView = binding.list
            adapter.submitList(it) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (position != RecyclerView.NO_POSITION) {
                    recyclerView.scrollToPosition(position)
                }
            }
        }

        viewModel.noSurveyFound.observe(requireActivity()) { noRequestFound ->
            if (noRequestFound) {
                binding.emptyListText.visibility = View.VISIBLE
            } else {
                binding.emptyListText.visibility = View.GONE
            }
        }

        viewModel.loadingSurvey.observe(requireActivity()) { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.messages.observe(requireActivity()) { message ->
            when(message.first){
                SurveysViewModel.START_RELOADING -> Toast.makeText(context, "RELOADING...", Toast.LENGTH_LONG).show()
                SurveysViewModel.END_RELOADING -> Toast.makeText(context, "RELOADED", Toast.LENGTH_LONG).show()
            }
        }
    }

}

/*
class SurveysFragment : Fragment() {

    private val surveysViewModel: SurveysViewModel by activityViewModels()
    private val formsViewModel: FormsViewModel by activityViewModels()

    private lateinit var binding: FragmentSurveysBinding

    private var surveyItems = listOf<SurveyViewItem>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSurveysBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val mAdapter = list.setupAdapter()
            setupSearchBar { searchView, filter ->
                mAdapter.apply {
                    if (filter.isEmpty()) {
                        submitList(surveyItems)
                        emptyListText.isVisible = false
                    } else {
                        val result = surveyItems.filter { it.title.contains(filter, true) }
                        this.submitList(result)
                        emptyListText.apply {
                            if (result.isEmpty()) {
                                text = getString(R.string.no_match, filter)
                                isVisible = true
                            } else isVisible = false
                        }

                    }
                }
            }
            reload.setOnClickListener {
                searchView.setQuery("", true)
                emptyListText.isVisible = false
                requireContext().executeWithInternet(root) { surveysViewModel.getSurveys() }
            }
            bindUIToEvents(surveysViewModel)
        }
    }


    private fun RecyclerView.setupAdapter(): SurveyListAdapter {
        addItemDecoration(
            BottomSpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.grid_2))
        )

        val mAdapter = SurveyListAdapter(object : SurveyViewClick {
            override fun onClick(view: View, item: SurveyViewItem) {
                collectDetailsForFormRendering(survey = item, viewModel = surveysViewModel) {
                    gotoSurveyForm()
                }
            }
        })
        adapter = mAdapter
        return mAdapter
    }


    private fun collectDetailsForFormRendering(
        survey: SurveyViewItem,
        viewModel: SurveysViewModel,
        onReadyForRendering: () -> Unit
    ) {
        viewModel.getSurveyQuestions(survey.id) { questions ->
            if (questions.isEmpty()) {
                warnUserAboutBlankSurvey()
            } else {
                viewModel.getQuestionTypes {
                    formsViewModel.setQuestionsToRender(
                        survey.id, survey.title, questions, it, 4
                    )
                    onReadyForRendering()
                }
            }
        }
    }

    private fun gotoSurveyForm() {
        findNavController().navigate(R.id.navigation_survey_form)
    }

    private fun warnUserAboutBlankSurvey() {
        make(binding.root, R.string.survey_has_no_question, LENGTH_LONG)
            .show()
    }

    private fun FragmentSurveysBinding.setupSearchBar(searchLister: (SearchView, String) -> Unit) {
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Logs.logCat("Survey List query text submitted : $query")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchLister(searchView, it) }
                Logs.logCat(" List query text: $newText")
                return false
            }
        })

    }

    private fun FragmentSurveysBinding.bindUIToEvents(viewModel: SurveysViewModel) {
        observeSurveysLoading(viewModel)
        observeSuccessfulSurveyListRequest(viewModel)
        observeFailedSurveyListRequest(viewModel)
    }


    private fun FragmentSurveysBinding.observeSurveysLoading(viewModel: SurveysViewModel) {
        viewModel.surveyListIsLoading().observe(viewLifecycleOwner) {
            progressBar.isVisible = it
            emptyListText.isVisible = !it
            list.isVisible = !it
        }
    }

    private fun FragmentSurveysBinding.observeSuccessfulSurveyListRequest(
        viewModel: SurveysViewModel
    ) {
        viewModel.surveyListSucceeded().observe(viewLifecycleOwner) { data ->

            surveyItems = data
            progressBar.isVisible = false
            emptyListText.isVisible = data.isEmpty()
            emptyListText.text = getString(R.string.no_survey_found)
            list.isVisible = data.isNotEmpty()
            (list.adapter as? SurveyListAdapter)?.run {
                submitList(data)
            }
        }
    }


    private fun FragmentSurveysBinding.observeFailedSurveyListRequest(viewModel: SurveysViewModel) {
        viewModel.surveyListFailed().observe(viewLifecycleOwner) {

            progressBar.isVisible = false
            emptyListText.isVisible = true
            emptyListText.text = getString(R.string.an_error_occurred)
            list.isVisible = false

        }
    }

}
*/