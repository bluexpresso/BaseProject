package com.idslogic.levelshoes.ui.home.landing.viewholders

import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
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
        val spannableString = SpannableString(binding.root.context.getString(R.string.sign_in))
        val signInText = binding.root.context.getString(R.string.sign_in)
        spannableString.setSpan(
            UnderlineSpan(),
            0,
            signInText.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        binding.actionBtnOne.text = spannableString
    }

    fun getCloseButton() = binding.iconClose
    fun getActionButtonOne() = binding.actionBtnOne
    fun getActionButtonTwo() = binding.actionBtnTwo
}