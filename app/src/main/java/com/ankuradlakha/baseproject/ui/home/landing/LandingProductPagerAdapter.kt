package com.ankuradlakha.baseproject.ui.home.landing

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ankuradlakha.baseproject.data.models.BaseModel
import com.ankuradlakha.baseproject.data.models.Product

class LandingProductPagerAdapter(activity: FragmentActivity) :
    FragmentStateAdapter(activity) {
    var products: ArrayList<BaseModel.Hit<Product>>? = null
    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return ProductPagerItemFragment.newInstance(
            Product()
        )
    }

    fun setItems(products: ArrayList<BaseModel.Hit<Product>>?) {
        this.products = products
    }

}