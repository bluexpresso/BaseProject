package com.idslogic.levelshoes.ui.home.landing.viewholders

import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.Content
import com.idslogic.levelshoes.data.models.Product
import com.idslogic.levelshoes.databinding.ItemLandingProductViewBinding
import com.idslogic.levelshoes.ui.home.landing.LandingProductPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProductViewHolder(
    private val binding: ItemLandingProductViewBinding,
    private val selectedCurrency: String
) :
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
        val pagerAdapter = LandingProductPagerAdapter(onProductSelected, selectedCurrency)
        binding.productsPager.adapter = pagerAdapter
        pagerAdapter.setItems(content.productsList)
        TabLayoutMediator(
            binding.productTabs,
            binding.productsPager
        ) { _: TabLayout.Tab, _: Int -> }.attach()
    }

    fun getNextButton() = binding.arrowNext
}