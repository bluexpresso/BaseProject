package com.idslogic.levelshoes.ui.home.landing.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.databinding.ItemNoContentBinding

class NoContentViewHolder(private val binding: ItemNoContentBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        binding.root.visibility = View.GONE
    }
}