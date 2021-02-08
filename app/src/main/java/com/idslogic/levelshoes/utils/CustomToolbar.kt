package com.idslogic.levelshoes.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import com.idslogic.levelshoes.R
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.view_toolbar.view.*

class CustomToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppBarLayout(context, attrs, defStyleAttr) {
    private val title: String
    private val leftIcon: Drawable?
    private val rightIcon: Drawable?
    private val background: Int
    private val contentColor: Int
    private val actionButtonEnabled: Boolean
    private val contentInset: Float

    init {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.CustomToolbar, 0, 0
        ).apply {
            try {
                title = getString(R.styleable.CustomToolbar_titleText)
                    ?: ""
                leftIcon =
                    getDrawable(R.styleable.CustomToolbar_leftIcon)
                rightIcon = getDrawable(R.styleable.CustomToolbar_rightIcon)
                actionButtonEnabled =
                    getBoolean(R.styleable.CustomToolbar_actionButtonEnabled, false)
                background = getColor(
                    R.styleable.CustomToolbar_background,
                    ContextCompat.getColor(context, R.color.toolbarColor)
                )
                contentInset = getDimension(R.styleable.CustomToolbar_contentInset, 16f)
                contentColor = getColor(
                    R.styleable.CustomToolbar_contentColor, ContextCompat.getColor(
                        context, R.color.toolbarContentColor
                    )
                )
                initToolbar()
            } finally {
                recycle()
            }
        }
    }

    private fun initToolbar() {
        inflate(context, R.layout.view_toolbar, this)
        setBackgroundColor(background)
        left_icon.setImageDrawable(leftIcon)
        left_icon.backgroundTintList = ColorStateList.valueOf(background)
        right_icon.backgroundTintList = ColorStateList.valueOf(background)
        left_icon.imageTintList = ColorStateList.valueOf(contentColor)
        right_icon.imageTintList = ColorStateList.valueOf(contentColor)
        title_text.setTextColor(contentColor)
        right_icon.setImageDrawable(rightIcon)
        title_text.text = title
        left_icon.setOnClickListener {
            onLeftIconClick?.invoke()
        }
        right_icon.setOnClickListener {
            onRightIconClick?.invoke()
        }
    }

    fun setTitle(@StringRes title: Int) {
        title_text.setText(title)
    }

    fun setTitle(title: String?) {
        title_text.text = title ?: ""
    }

    var onRightIconClick: (() -> Unit)? = null
    var onActionButtonClick: (() -> Unit)? = null
    var onLeftIconClick: (() -> Unit)? = null
}