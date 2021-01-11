package com.idslogic.levelshoes.utils

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.idslogic.levelshoes.R
import kotlinx.android.synthetic.main.view_expandable_header_body.view.*

class ExpandableHeaderBody @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.view_expandable_header_body, this)
        initExpandCollapse()
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutTransition = LayoutTransition()
    }

    private fun initExpandCollapse() {
        heading.setOnClickListener {
            if (body.visibility == View.VISIBLE) {
                body.visibility = View.GONE
                icon.setImageResource(
                    R.drawable.ic_baseline_add_24
                )
            } else {
                body.visibility = View.VISIBLE
                icon.setImageResource(
                    R.drawable.ic_baseline_remove_24
                )
            }
        }
    }

    fun setHeader(text: String) {
        heading.text = text
    }

    fun setBody(text: String) {
        body.text = text
    }
}