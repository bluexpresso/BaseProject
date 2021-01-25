package com.idslogic.levelshoes.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.di.GlideApp
import kotlinx.android.synthetic.main.view_media_button.view.*

class MediaButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.view_media_button, this)
    }

    fun loadBackgroundImage(url: String) {
        GlideApp.with(context)
            .load(url)
            .into(media_image)
    }

    fun setText(text: String) {
        btn.text = text
    }

    fun setTextColor(textColor: String) {
        btn.setTextColor(Color.parseColor(textColor))
    }
}