package com.idslogic.levelshoes.ui.home.landing.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.Content
import com.idslogic.levelshoes.databinding.ViewDismissibleCardBinding

class DismissibleCardViewHolder(private val binding: ViewDismissibleCardBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(content: Content) {
        binding.actionBtnTwo.visibility = View.GONE
        binding.actionBtnOne.visibility = View.VISIBLE
        binding.title.text = content.title
        binding.description.text = content.subTitle
        binding.actionBtnOne.text = binding.root.context.getString(R.string.lets_go)
        binding.iconClose.setOnClickListener {

        }
        binding.actionBtnOne.setOnClickListener {

        }
    }

    fun getCloseButton() = binding.iconClose
    fun getActionButtonOne() = binding.actionBtnOne
    fun getActionButtonTwo() = binding.actionBtnTwo
}