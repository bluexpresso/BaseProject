package com.idslogic.levelshoes.ui.home.product.filters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.data.models.FilterOption
import com.idslogic.levelshoes.databinding.ItemFiltersSelectionBinding

class FiltersSelectionAdapter(
    private val isColorFilter: Boolean,
    val selectedItems: HashMap<String, FilterOption>
) :
    RecyclerView.Adapter<FiltersSelectionAdapter.ViewHolder>() {
    var filters = arrayListOf<FilterOption>()

    inner class ViewHolder(val binding: ItemFiltersSelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.title.text = filters[bindingAdapterPosition].name
            if (isColorFilter) {
                binding.image.visibility = View.VISIBLE
            } else {
                binding.image.visibility = View.GONE
            }
            if (selectedItems.containsKey(filters[bindingAdapterPosition].value)) {
                binding.check.visibility = View.VISIBLE
            } else {
                binding.check.visibility = View.GONE
            }
            binding.root.setOnClickListener {
                if (selectedItems.containsKey(filters[bindingAdapterPosition].value)) {
                    binding.check.visibility = View.GONE
                    selectedItems.remove(filters[bindingAdapterPosition].value)
                } else {
                    binding.check.visibility = View.VISIBLE
                    selectedItems[filters[bindingAdapterPosition].value] =
                        filters[bindingAdapterPosition]
                }
                notifyItemChanged(bindingAdapterPosition)
            }
        }
    }

    fun clear() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFiltersSelectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = filters.size

    fun setItems(filters: ArrayList<FilterOption>) {
        this.filters = filters
        notifyDataSetChanged()
    }
}