package com.ankuradlakha.baseproject.ui.home.landing.viewholders

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ankuradlakha.baseproject.data.models.Content
import com.ankuradlakha.baseproject.databinding.ItemLandingSliderBinding
import com.ankuradlakha.baseproject.ui.home.landing.SliderAdapter
import com.ankuradlakha.baseproject.utils.GENDER_KIDS
import com.ankuradlakha.baseproject.utils.GENDER_MEN
import com.ankuradlakha.baseproject.utils.GENDER_WOMEN

class SliderViewHolder(private val binding: ItemLandingSliderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(activity: FragmentActivity, mapItems: HashMap<String, ArrayList<Content>>) {
        val adapter = SliderAdapter(activity)
        adapter.setItems(
            arrayListOf(
                mapItems[GENDER_WOMEN]?.get(0),
                mapItems[GENDER_MEN]?.get(0),
                mapItems[GENDER_KIDS]?.get(0)
            )
        )
        binding.sliderPager.adapter = adapter
    }

}