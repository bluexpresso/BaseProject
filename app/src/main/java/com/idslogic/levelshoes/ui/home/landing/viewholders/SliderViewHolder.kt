package com.idslogic.levelshoes.ui.home.landing.viewholders

import android.util.Log
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.Content
import com.idslogic.levelshoes.databinding.ItemLandingSliderBinding
import com.idslogic.levelshoes.ui.home.landing.SliderAdapter
import com.idslogic.levelshoes.utils.GENDER_KIDS
import com.idslogic.levelshoes.utils.GENDER_MEN
import com.idslogic.levelshoes.utils.GENDER_WOMEN
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber
import kotlin.math.abs

class SliderViewHolder(private val binding: ItemLandingSliderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val MIN_SCALE = 0.85f
    private val MIN_ALPHA = 0.5f
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
        TabLayoutMediator(
            binding.sliderTabLayout,
            binding.sliderPager
        ) { tab: TabLayout.Tab, pos: Int ->
            tab.text = when (pos) {
                0 -> binding.root.context.getString(R.string.women)
                1 -> binding.root.context.getString(R.string.men)
                2 -> binding.root.context.getString(R.string.kids)
                else -> binding.root.context.getString(R.string.women)
            }
        }.attach()
        initSliderTransition()
    }
    companion object {

        private const val DEFAULT_TRANSLATION_X = .0f
        private const val DEFAULT_TRANSLATION_FACTOR = 1.2f

        private const val SCALE_FACTOR = .14f
        private const val DEFAULT_SCALE = 1f

        private const val ALPHA_FACTOR = .3f
        private const val DEFAULT_ALPHA = 1f

    }
    private fun initSliderTransition() {
        binding.sliderPager.setPageTransformer { page, position ->
            Timber.d("POSITION->$position")
            page.apply {
                when {
                    position <= 0f -> {
                        translationX = -width*position
                    }
                    position <=1 -> {
                        translationX = 0f
                    }
                    else -> {
                        translationX = 0f
//                        translationX = -width*position
                    }
                }
            }

        }
    }

    fun getSlider() = binding.sliderPager
}