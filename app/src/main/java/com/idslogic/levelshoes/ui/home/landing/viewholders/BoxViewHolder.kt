package com.idslogic.levelshoes.ui.home.landing.viewholders

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.button.MaterialButton
import com.idslogic.levelshoes.custom.MediaButton
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
            val buttonContent =
                it.elements.find { element -> element.type.equals(CONTENT_TYPE_BUTTON, true) }
            val textContent =
                it.elements.find { element -> element.type.equals(CONTENT_TYPE_TEXT, true) }
            val headingContent = it.elements.find { element ->
                element.type.equals(
                    CONTENT_TYPE_HEADING, true
                )
            }
            var activeView: Any = binding.boxActionButton
            buttonContent?.let { btnContent ->
                if (!buttonContent.backgroundColor.isNullOrEmpty()) {
                    activeView = binding.boxActionButton
                    binding.boxActionButton.visibility = View.VISIBLE
                    binding.boxActionImageButton.visibility = View.GONE
                    try {
                        binding.boxActionButton.setBackgroundColor(
                            Color.parseColor(
                                btnContent.backgroundColor
                            )
                        )
                    } catch (e: Exception) {
                    }
                } else if (!buttonContent.backgroundImage.isNullOrEmpty()) {
                    activeView = binding.boxActionImageButton
                    binding.boxActionButton.visibility = View.GONE
                    binding.boxActionImageButton.visibility = View.VISIBLE
                    binding.boxActionImageButton.loadBackgroundImage(buttonContent.backgroundImage)
                }
                val textColor =
                    btnContent.foregroundColor ?: textContent?.foregroundColor ?: "#000000"
                if (textContent?.content.isNullOrEmpty() && btnContent.content.isNullOrEmpty()) {
                    binding.boxActionButton.visibility = View.GONE
                    binding.boxActionImageButton.visibility = View.GONE
                } else {
                    if (activeView is MaterialButton) {
                        binding.boxActionButton.visibility = View.VISIBLE
                        binding.boxActionButton.text =
                            btnContent.content ?: textContent?.content ?: ""
                        binding.boxActionButton.setTextColor(Color.parseColor(textColor))
                    } else if (activeView is MediaButton) {
                        binding.boxActionImageButton.visibility = View.VISIBLE
                        binding.boxActionImageButton.setText(
                            btnContent.content ?: textContent?.content ?: ""
                        )
                        binding.boxActionImageButton.setTextColor(textColor)
                    }
                }
            }
            headingContent?.let { heading ->
                binding.boxTitle.visibility = View.VISIBLE
                try {
                    binding.boxTitle.setTextColor(
                        Color.parseColor(
                            heading.foregroundColor ?: "#FFFFFF"
                        )
                    )
                } catch (e: Exception) {
                }
                binding.boxTitle.text = heading.content
            }
        }

    }
}