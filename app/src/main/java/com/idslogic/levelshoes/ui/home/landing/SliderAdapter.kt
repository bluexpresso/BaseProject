package com.idslogic.levelshoes.ui.home.landing

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.idslogic.levelshoes.data.models.Content

class SliderAdapter(activity: FragmentActivity, private val onViewAllProducts: ((Int) -> Unit)?) :
    FragmentStateAdapter(activity) {
    private lateinit var items: ArrayList<Content?>

    companion object {
        const val ITEM_COUNT = 3
    }

    override fun getItemCount() = ITEM_COUNT

    override fun createFragment(position: Int): Fragment {
        val fragment = SliderItemFragment.newInstance(items[position])
        fragment.onViewAllProducts = onViewAllProducts
        return fragment
    }

    fun setItems(items: ArrayList<Content?>) {
        this.items = items
    }
}