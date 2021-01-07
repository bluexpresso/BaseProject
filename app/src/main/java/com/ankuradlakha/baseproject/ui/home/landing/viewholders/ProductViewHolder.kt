package com.ankuradlakha.baseproject.ui.home.landing.viewholders

import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.data.models.BaseModel
import com.ankuradlakha.baseproject.data.models.Content
import com.ankuradlakha.baseproject.data.models.Product
import com.ankuradlakha.baseproject.databinding.ItemLandingProductViewBinding
import com.ankuradlakha.baseproject.ui.home.landing.LandingProductPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProductViewHolder(private val binding: ItemLandingProductViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        activity: FragmentActivity,
        content: Content,
        onProductSelected: ((BaseModel.Hit<Product>, AppCompatImageView) -> Unit)?
    ) {
        binding.contentSubtitle.text = content.subTitle ?: ""
        binding.contentTitle.text = content.title ?: ""
        if (!content.productsList.isNullOrEmpty()) {
            for (i in 0..content.productsList!!.size) {
                binding.productTabs.addTab(binding.productTabs.newTab())
            }
        }
        binding.productsPager.apply {
            clipToPadding = false
            offscreenPageLimit = 1
            val offsetPx = resources.getDimensionPixelOffset(R.dimen.spacing_12)
            val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.spacing_32)
            setPageTransformer { page, position ->
                val viewPager = page.parent.parent as ViewPager2
                val offset = position * -(2 * offsetPx + pageMarginPx)
                if (viewPager.orientation == ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.translationX = -offset
                    } else {
                        page.translationX = offset
                    }
                }
            }
        }
        val pagerAdapter = LandingProductPagerAdapter(onProductSelected)
        binding.productsPager.adapter = pagerAdapter
        pagerAdapter.setItems(content.productsList)
        TabLayoutMediator(
            binding.productTabs,
            binding.productsPager
        ) { _: TabLayout.Tab, _: Int -> }.attach()
    }

    fun getNextButton() = binding.arrowNext
}