package com.ankuradlakha.baseproject.ui.home.landing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ankuradlakha.baseproject.data.models.Content
import com.ankuradlakha.baseproject.databinding.*
import com.ankuradlakha.baseproject.ui.home.landing.viewholders.*
import com.ankuradlakha.baseproject.utils.*

class LandingListAdapter(private val activity: FragmentActivity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var landingItems = arrayListOf<Content>()
    var mapItems = HashMap<String, ArrayList<Content>>()
    var currentSelectedTab = GENDER_WOMEN

    companion object {
        const val viewTypeSlider = 0
        const val viewTypeRegisterSignIn = 1
        const val viewTypeBoxView = 2
        const val viewTypeNoContent = 3
        const val viewTypeProductView = 4
    }

    override fun getItemViewType(position: Int): Int {
        if (landingItems[position].boxType == BOX_TYPE_SLIDER)
            return viewTypeSlider
        if (landingItems[position].boxType == BOX_TYPE_REGISTER_SIGN_IN)
            return viewTypeRegisterSignIn
        if (landingItems[position].boxType == BOX_TYPE_BOX_VIEW)
            return viewTypeBoxView
        if (landingItems[position].boxType == BOX_TYPE_PRODUCT_VIEW)
            return viewTypeProductView
        return viewTypeNoContent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == viewTypeSlider) {
            return SliderViewHolder(
                ItemLandingSliderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        if (viewType == viewTypeRegisterSignIn) {
            return RegisterSignInViewHolder(
                ViewDismissibleCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        if (viewType == viewTypeBoxView) {
            return BoxViewHolder(
                ItemBoxViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        if (viewType == viewTypeProductView) {
            return ProductViewHolder(
                ItemLandingProductViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        return NoContentViewHolder(
            ItemNoContentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SliderViewHolder) {
            holder.bind(activity, mapItems = mapItems)
        }
        if (holder is RegisterSignInViewHolder) {
            holder.bind(landingItems[position])
        }
        if (holder is BoxViewHolder) {
            holder.bind(landingItems[position])
        }
        if (holder is ProductViewHolder) {
            holder.bind(activity,landingItems[position])
        }
        if (holder is NoContentViewHolder) {
            holder.bind()
        }
    }

    override fun getItemCount() = landingItems.size

    fun setItems(mapItems: HashMap<String, ArrayList<Content>>?) {
        if (mapItems == null) return
        this.mapItems = mapItems
        landingItems.clear()
        landingItems.addAll(mapItems[GENDER_MEN] ?: arrayListOf())
        notifyDataSetChanged()
    }
}