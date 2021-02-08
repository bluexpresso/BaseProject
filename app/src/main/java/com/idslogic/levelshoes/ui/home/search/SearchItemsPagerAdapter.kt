package com.idslogic.levelshoes.ui.home.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.idslogic.levelshoes.utils.GENDER_KIDS
import com.idslogic.levelshoes.utils.GENDER_MEN
import com.idslogic.levelshoes.utils.GENDER_WOMEN

class SearchItemsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return SearchItemFragment.newInstance(
            when (position) {
                0 -> GENDER_WOMEN
                1 -> GENDER_MEN
                2 -> GENDER_KIDS
                else -> GENDER_WOMEN
            }
        )
    }
}