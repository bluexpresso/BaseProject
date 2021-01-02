package com.ankuradlakha.baseproject.ui.home.landing.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.data.models.Content
import com.ankuradlakha.baseproject.databinding.ViewDismissibleCardBinding

class RegisterSignInViewHolder(private val binding: ViewDismissibleCardBinding) :
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
}