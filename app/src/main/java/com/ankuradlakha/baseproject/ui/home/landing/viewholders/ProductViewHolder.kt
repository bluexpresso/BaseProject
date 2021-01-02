package com.ankuradlakha.baseproject.ui.home.landing.viewholders

import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.data.models.Content
import com.ankuradlakha.baseproject.databinding.ItemLandingProductViewBinding
import com.ankuradlakha.baseproject.ui.home.landing.LandingProductPagerAdapter
import dagger.BindsInstance

class ProductViewHolder(private val binding: ItemLandingProductViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(activity: FragmentActivity, content: Content) {
        binding.contentSubtitle.text = content.subTitle ?: ""
        binding.contentTitle.text = content.title ?: ""
//        binding.productsPager.apply {
//            offscreenPageLimit = 1
//            val recyclerView = getChildAt(0) as RecyclerView
//            recyclerView.apply {
//                val padding = resources.getDimensionPixelOffset(R.dimen.spacing_12) +
//                        resources.getDimensionPixelOffset(R.dimen.spacing_24)
//                // setting padding on inner RecyclerView puts overscroll effect in the right place
//                // TODO: expose in later versions not to rely on getChildAt(0) which might break
//                setPadding(padding, 0, padding, 0)
//                clipToPadding = false
//            }
//        }
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
        val pagerAdapter = LandingProductPagerAdapter(activity)
        binding.productsPager.adapter = pagerAdapter
        pagerAdapter.setItems(content.productsList)
    }
}