package com.ankuradlakha.baseproject.ui.home.landing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ankuradlakha.baseproject.data.models.Content
import com.ankuradlakha.baseproject.databinding.ItemLandingSliderBinding
import com.ankuradlakha.baseproject.utils.GENDER_KIDS
import com.ankuradlakha.baseproject.utils.GENDER_MEN
import com.ankuradlakha.baseproject.utils.GENDER_WOMEN

class LandingListAdapter(private val activity: FragmentActivity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var landingItems = arrayListOf<Content>()
    var mapItems = HashMap<String, ArrayList<Content>>()
    var currentSelectedTab = GENDER_WOMEN

    companion object {
        const val viewTypeSlider = 0
    }

    inner class ViewHolderSlider(private val binding: ItemLandingSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(content: Content) {
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

    override fun getItemViewType(position: Int): Int {
        return viewTypeSlider
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == viewTypeSlider) {
            return ViewHolderSlider(
                ItemLandingSliderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        return ViewHolderSlider(
            ItemLandingSliderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolderSlider) {
            holder.bind(landingItems[position])
        }
    }

    override fun getItemCount() = 1

    fun setItems(mapItems: HashMap<String, ArrayList<Content>>?) {
        if (mapItems == null) return
        this.mapItems = mapItems
        landingItems.clear()
        landingItems.addAll(mapItems[GENDER_MEN] ?: arrayListOf())
        notifyDataSetChanged()
    }
}