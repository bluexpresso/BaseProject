package com.idslogic.levelshoes.ui.home.landing.viewholders

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.data.models.Content
import com.idslogic.levelshoes.databinding.ItemBoxViewBinding
import com.idslogic.levelshoes.di.GlideApp
import com.idslogic.levelshoes.utils.CONTENT_TYPE_BUTTON
import com.idslogic.levelshoes.utils.CONTENT_TYPE_HEADING
import com.idslogic.levelshoes.utils.CONTENT_TYPE_TEXT

class BoxViewHolder(private val binding: ItemBoxViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(content: Content) {
        content.data?.forEach {
            GlideApp.with(binding.boxImage.context).load(it.url).into(binding.boxImage)
            it.elements.forEach { element ->
                if (element.type.equals(CONTENT_TYPE_BUTTON, true)) {
                    binding.boxActionButton.visibility = View.VISIBLE
                    binding.boxActionButton.setBackgroundColor(
                        Color.parseColor(
                            element.backgroundColor ?: "#FFFFFF"
                        )
                    )
                    binding.boxActionButton.setTextColor(
                        Color.parseColor(
                            element.foregroundColor ?: "#000000"
                        )
                    )
                    binding.boxActionButton.text = element.content ?: ""
                } else {
                    binding.boxActionButton.visibility = View.GONE
                }
                if (element.type.equals(CONTENT_TYPE_HEADING, true)
                    || element.type.equals(CONTENT_TYPE_TEXT, true)
                ) {
                    binding.boxTitle.visibility = View.VISIBLE
                    binding.boxTitle.setTextColor(
                        Color.parseColor(
                            element.foregroundColor ?: "#FFFFFF"
                        )
                    )
                    binding.boxTitle.text = element.content
                } else {
                    binding.boxTitle.visibility = View.GONE
                }
            }
        }

    }
}