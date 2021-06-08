package org.agrosurvey.forms.rendering.pager

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TAB_LABEL_VISIBILITY_UNLABELED
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.agrosurvey.Logs
import org.agrosurvey.forms.FormsViewModel
import org.agrosurvey.forms.R
import org.agrosurvey.forms.databinding.FragmentSurveyPagerBinding
import org.agrosurvey.forms.rendering.pager.adapter.SurveyPagerAdapter
import org.agrosurvey.forms.widget.FormPagerTab.FormTabMode
import org.agrosurvey.forms.widget.TextInputUtils


@AndroidEntryPoint
class FragmentSurveyPager : Fragment() {

    private lateinit var binding: FragmentSurveyPagerBinding
    private val viewModel by activityViewModels<FormsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSurveyPagerBinding.inflate(inflater, container, false)
        binding.apply {
            close.setOnClickListener {
                findNavController().navigateUp()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapAdapter =
            SurveyPagerAdapter(viewModel.getSurveyPageCount(), childFragmentManager, lifecycle)

        with(binding) {
            pager.adapter = mapAdapter
            pager.isUserInputEnabled = false

            TabLayoutMediator(sections, pager) { tab, position ->
                tab.text = null
                val text = "S${position + 1}"

                tab.tabLabelVisibility = TAB_LABEL_VISIBILITY_UNLABELED
                tab.setMode(sections.context, FormTabMode.UNSELECTED, text)

            }.attach()

            viewModel.onNextPageRequested().observe(viewLifecycleOwner) { currentPosition ->
                val next = currentPosition + 1
                if (next in 0 until sections.tabCount) {
                    pager.currentItem = next
                    Logs.logCat("Requested to show page : $next")
                } else {
                    // The last page is requesting exit
                    viewModel.completeSurvey(onFeedbackSaved = {
                        Snackbar.make(pager, "Your feedback is saved !", Snackbar.LENGTH_SHORT)
                            .show()
                        binding.close.performClick()

                    }, onError = {
                        Snackbar.make(
                            pager,
                            "Feedback could not be saved. Try again.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    })
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.reset(this)
    }

    fun TabLayout.Tab.setMode(context: Context, mode: FormTabMode, text: String) {
        val textColor = ContextCompat.getColor(
            context, if (mode == FormTabMode.SELECTED) R.color.white_form
            else R.color.black_form
        )

        val background = ContextCompat.getColor(
            context,
            if (mode == FormTabMode.SELECTED) R.color.black_form
            else R.color.white_form
        )
        val drawable = TextInputUtils.createCircularTextDrawable(
            text = text,
            textColor = textColor,
            background = Color.RED
        )
        icon = drawable
    }

}