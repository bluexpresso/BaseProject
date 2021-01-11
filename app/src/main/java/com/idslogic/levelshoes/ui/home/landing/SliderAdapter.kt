package com.idslogic.levelshoes.ui.home.landing

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.idslogic.levelshoes.data.models.Content
import kotlin.collections.ArrayList

class SliderAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private lateinit var items: ArrayList<Content?>

    companion object {
        const val ITEM_COUNT = 3
    }

    override fun getItemCount() = ITEM_COUNT

    override fun createFragment(position: Int): Fragment {
        return SliderItemFragment.newInstance(items[position])
    }

    fun setItems(items: ArrayList<Content?>) {
        this.items = items
    }
}