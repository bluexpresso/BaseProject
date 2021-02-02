package com.idslogic.levelshoes.ui.home.landing.viewholders

import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.Content
import com.idslogic.levelshoes.databinding.ItemLandingSliderBinding
import com.idslogic.levelshoes.ui.home.landing.SliderAdapter
import com.idslogic.levelshoes.utils.GENDER_KIDS
import com.idslogic.levelshoes.utils.GENDER_MEN
import com.idslogic.levelshoes.utils.GENDER_WOMEN
import timber.log.Timber

class SliderViewHolder(private val binding: ItemLandingSliderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        activity: FragmentActivity,
        mapItems: HashMap<String, ArrayList<Content>>,
        onViewAllProducts: ((Int, String?) -> Unit)?
    ) {
        val adapter = SliderAdapter(activity,onViewAllProducts)
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
            when (pos) {
                0 -> {
                    tab.text = binding.root.context.getString(R.string.women)
                }
                1 -> {
                    tab.text = binding.root.context.getString(R.string.men)
                }
                2 -> {
                    tab.text = binding.root.context.getString(R.string.kids)
                }
                else -> {
                    tab.text = binding.root.context.getString(R.string.women)
                }
            }
        }.attach()
        binding.sliderPager.offscreenPageLimit = 3
        initSliderTransition()
    }

    private fun selectTab(i: Int) {
        for (j in 0..3) {
            if (j == i) {
                binding.sliderTabLayout.getTabAt(j)?.view?.children?.find { it is TextView }
                    ?.let { tv ->
                        (tv as TextView).post {
                            tv.typeface = ResourcesCompat.getFont(binding.root.context, R.font.bold)
                        }
                    }
            } else {
                binding.sliderTabLayout.getTabAt(j)?.view?.children?.find { it is TextView }
                    ?.let { tv ->
                        (tv as TextView).post {
                            tv.typeface =
                                ResourcesCompat.getFont(binding.root.context, R.font.regular)
                        }
                    }
            }
        }
    }

    private fun initSliderTransition() {
        binding.sliderPager.setPageTransformer { page, position ->
            Timber.d("POSITION->$position")
            page.apply {
                translationX = when {
                    position <= 0f -> {
                        -width * position
                    }
                    position <= 1 -> {
                        1f
                    }
                    else -> {
                        1f
                    }
                }
            }

        }
    }

    fun getSlider() = binding.sliderPager
    var onSliderPageChanged: ((Int) -> Unit) = {
        selectTab(it)
    }
}