package com.ankuradlakha.baseproject.ui.home.landing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ankuradlakha.baseproject.data.models.Content
import com.ankuradlakha.baseproject.databinding.*
import com.ankuradlakha.baseproject.ui.home.landing.viewholders.*
import com.ankuradlakha.baseproject.utils.*

class LandingListAdapter(private val activity: FragmentActivity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var selectedCurrency: String
    var landingItems = arrayListOf<Content>()
    var mapItems = HashMap<String, ArrayList<Content>>()
    var currentSelectedTab = GENDER_WOMEN
    var parent: RecyclerView? = null

    companion object {
        const val viewTypeSlider = 0
        const val viewTypeDismissibleCard = 1
        const val viewTypeBoxView = 2
        const val viewTypeNoContent = 3
        const val viewTypeProductView = 4
        const val viewTypeAdditionalProductsView = 5
    }

    override fun getItemViewType(position: Int): Int {
        if (landingItems[position].boxType == BOX_TYPE_SLIDER)
            return viewTypeSlider
        if (landingItems[position].boxType == BOX_TYPE_REGISTER_SIGN_IN)
            return viewTypeDismissibleCard
        if (landingItems[position].boxType == BOX_TYPE_BOX_VIEW)
            return viewTypeBoxView
        if (landingItems[position].boxType == BOX_TYPE_PRODUCT_VIEW)
            return viewTypeProductView
        if (landingItems[position].boxType == BOX_TYPE_ADDITIONAL_PRODUCTS_VIEW)
            return viewTypeAdditionalProductsView
        return viewTypeNoContent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == viewTypeSlider) {
            val binding =
                ItemLandingSliderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            val sliderViewHolder = SliderViewHolder(binding)
            this.parent = parent as RecyclerView
            observePageChanges(sliderViewHolder)
            return SliderViewHolder(binding)
        }
        if (viewType == viewTypeDismissibleCard) {
            return DismissibleCardViewHolder(
                ViewDismissibleCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        if (viewType == viewTypeBoxView) {
            val binding = ItemBoxViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            val layoutParams: ViewGroup.LayoutParams = binding.root.layoutParams
            layoutParams.height = (parent.height * 0.8).toInt()
            binding.root.layoutParams = layoutParams

            return BoxViewHolder(binding)
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
        if (viewType == viewTypeAdditionalProductsView) {
            return AdditionalProductsViewHolder(
                ItemLandingAdditionalProductsViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),selectedCurrency
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

    private fun observePageChanges(
        sliderViewHolder: SliderViewHolder
    ) {
        sliderViewHolder.getSlider().registerOnPageChangeCallback(sliderPageChangedCallback)
    }

    private val sliderPageChangedCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (parent != null && !parent!!.isComputingLayout) {
                super.onPageSelected(position)
                updateItems(
                    when (position) {
                        0 -> {
                            currentSelectedTab = GENDER_WOMEN
                            GENDER_WOMEN
                        }
                        1 -> {
                            currentSelectedTab = GENDER_MEN
                            GENDER_MEN
                        }
                        2 -> {
                            currentSelectedTab = GENDER_KIDS
                            GENDER_KIDS
                        }
                        else -> {
                            currentSelectedTab = GENDER_WOMEN
                            GENDER_WOMEN
                        }
                    }
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SliderViewHolder) {
            holder.bind(activity, mapItems = mapItems)
            holder.getSlider().unregisterOnPageChangeCallback(sliderPageChangedCallback)
            holder.getSlider().setCurrentItem(
                when (currentSelectedTab) {
                    GENDER_WOMEN -> 0
                    GENDER_MEN -> 1
                    GENDER_KIDS -> 2
                    else -> 1
                }, false
            )
            holder.getSlider().registerOnPageChangeCallback(sliderPageChangedCallback)
        }
        if (holder is DismissibleCardViewHolder) {
            holder.bind(landingItems[position])
            holder.getCloseButton().setOnClickListener {
                onDismissibleCardDismissed?.invoke(BOX_TYPE_REGISTER_SIGN_IN)
            }
            holder.getActionButtonOne().setOnClickListener {
                onDismissibleCardActioned?.invoke(BOX_TYPE_REGISTER_SIGN_IN)
            }
        }
        if (holder is BoxViewHolder) {
            holder.bind(landingItems[position])
        }
        if (holder is ProductViewHolder) {
            holder.bind(activity, landingItems[position])
        }
        if (holder is AdditionalProductsViewHolder) {
            holder.bind(landingItems[position])
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
        landingItems.addAll(mapItems[GENDER_WOMEN] ?: arrayListOf())
        notifyDataSetChanged()
    }

    fun updateItems(gender: String) {
        landingItems.clear()
        landingItems.addAll(mapItems[gender] ?: arrayListOf())
        notifyItemRangeChanged(1, landingItems.size - 1)
    }

    fun removeDismissibleCards() {
        mapItems[GENDER_WOMEN]?.removeAt(1)
        mapItems[GENDER_MEN]?.removeAt(1)
        mapItems[GENDER_KIDS]?.removeAt(1)
        landingItems.removeAt(1)
        notifyItemRemoved(1)
    }

    var onDismissibleCardActioned: ((String) -> Unit)? = null
    var onDismissibleCardDismissed: ((String) -> Unit)? = null
}