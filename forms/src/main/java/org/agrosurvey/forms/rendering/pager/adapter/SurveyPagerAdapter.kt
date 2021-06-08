package org.agrosurvey.forms.rendering.pager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.agrosurvey.forms.rendering.FragmentFormPage

class SurveyPagerAdapter(
    private val numberOfPages: Int,
    private val fm: FragmentManager,
    private val lifecycle: Lifecycle
) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return numberOfPages
    }

    override fun createFragment(position: Int): Fragment {
        return FragmentFormPage.newInstance(position, isLastPage = position == numberOfPages - 1)
    }

    /* override fun getPageTitle(position: Int): CharSequence {
         return "Fragment ${(position + 1)}"
     }*/
}


