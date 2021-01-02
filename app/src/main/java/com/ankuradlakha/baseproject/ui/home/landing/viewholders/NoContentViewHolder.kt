package com.ankuradlakha.baseproject.ui.home.landing.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ankuradlakha.baseproject.databinding.ItemNoContentBinding

class NoContentViewHolder(private val binding: ItemNoContentBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        binding.root.visibility = View.GONE
    }
}