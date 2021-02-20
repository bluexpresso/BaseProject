package com.idslogic.levelshoes.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.idslogic.levelshoes.R
import kotlinx.android.synthetic.main.view_toolbar_compact.view.*

class CustomToolbarCompact @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppBarLayout(context, attrs, defStyleAttr) {
    private val title: String
    private val leftIcon: Drawable?
    private val rightIcon: Drawable?
    private val background: Int
    private val contentColor: Int
    private val actionButtonEnabled: Boolean
    private val showOnlyTitle: Boolean
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
                showOnlyTitle = getBoolean(R.styleable.CustomToolbar_showOnlyTitle, false)
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
        inflate(context, R.layout.view_toolbar_compact, this)
        setBackgroundColor(background)
        left_icon.setImageDrawable(leftIcon)
        if (showOnlyTitle) {
            left_icon.visibility = View.INVISIBLE
            right_icon.visibility = View.INVISIBLE
        }
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