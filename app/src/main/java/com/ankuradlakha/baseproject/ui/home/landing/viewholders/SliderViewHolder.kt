package com.ankuradlakha.baseproject.ui.home.landing.viewholders

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.data.models.Content
import com.ankuradlakha.baseproject.databinding.ItemLandingSliderBinding
import com.ankuradlakha.baseproject.ui.home.landing.SliderAdapter
import com.ankuradlakha.baseproject.utils.GENDER_KIDS
import com.ankuradlakha.baseproject.utils.GENDER_MEN
import com.ankuradlakha.baseproject.utils.GENDER_WOMEN
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber
import java.text.FieldPosition

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

    private fun initSliderTransition() {
        binding.sliderPager.setPageTransformer { page, position ->
            Timber.d("POSITION->$position")
            page.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
//                        alpha = 0f
                    }
                    position <= 0 -> { // [-1,0]
                        // Use the default slide transition when moving to the left page
//                        alpha = 1f
//                        translationX = 0f
//                        translationZ = 0f
//                        scaleX = 1f
//                        scaleY = 1f
//                        translationX = pageWidth*position
//                        translationX = pageWidth*position

                    }
                    position <= 1 -> { // (0,1]
                        // Fade the page out.

                        // Counteract the default slide transition
//                        translationX = pageWidth*-position
                        // Move it behind the left page
//                        translationZ = -1f

                        // Scale the page down (between MIN_SCALE and 1)
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
//                        alpha = 0f
                    }
                }
            }

        }
    }

    fun getSlider() = binding.sliderPager
}