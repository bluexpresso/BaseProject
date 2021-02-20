package com.idslogic.levelshoes.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.idslogic.levelshoes.R
import kotlinx.android.synthetic.main.view_country_dropdown.view.*
import kotlinx.android.synthetic.main.view_gender_selection.view.*

class CustomGenderSelection @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        initView()
    }

    private fun initView() {
        inflate(context, R.layout.view_gender_selection, this)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        women.setOnClickListener {
            women.isSelected = true
            men.isSelected = false
            kids.isSelected = false

        }
        men.setOnClickListener {
            women.isSelected = false
            men.isSelected = true
            kids.isSelected = false

        }
        kids.setOnClickListener {
            women.isSelected = false
            men.isSelected = false
            kids.isSelected = true

        }

    }
}