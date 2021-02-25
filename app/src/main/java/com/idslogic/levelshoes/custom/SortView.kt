package com.idslogic.levelshoes.custom

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textview.MaterialTextView
import com.idslogic.levelshoes.R
import kotlinx.android.synthetic.main.item_filters_sort_by.view.*

class SortView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var selectedValue = SORT_BY_RELEVANCE

    init {
        inflate(context, R.layout.item_filters_sort_by, this)
        initClicks()
        select(selectedValue)
    }

    private fun initClicks() {
        sort_by_relevance.setOnClickListener {
            btn_sort_by_relevance.isChecked = true
        }
        sort_by_newest_first.setOnClickListener {
            btn_sort_by_newest_first.isChecked = true
        }
        sort_by_highest_price.setOnClickListener {
            btn_sort_by_highest_price.isChecked = true
        }
        sort_by_lowest_price.setOnClickListener {
            btn_sort_by_lowest_price.isChecked = true
        }

        btn_sort_by_relevance.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) select(SORT_BY_RELEVANCE)
        }

        btn_sort_by_newest_first.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) select(SORT_BY_NEWEST_FIRST)
        }

        btn_sort_by_highest_price.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) select(SORT_BY_HIGHEST_PRICE)
        }

        btn_sort_by_lowest_price.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) select(SORT_BY_LOWEST_PRICE)
        }
    }

    private fun select(sortBy: String) {
        selectedValue = sortBy
        when (sortBy) {
            SORT_BY_RELEVANCE -> {
                sort_by_relevance.typeface = ResourcesCompat.getFont(context, R.font.medium)
                unselect(sort_by_newest_first, btn_sort_by_newest_first)
                unselect(sort_by_lowest_price, btn_sort_by_lowest_price)
                unselect(sort_by_highest_price, btn_sort_by_highest_price)
            }
            SORT_BY_NEWEST_FIRST -> {
                sort_by_newest_first.typeface = ResourcesCompat.getFont(context, R.font.medium)
                unselect(sort_by_relevance, btn_sort_by_relevance)
                unselect(sort_by_lowest_price, btn_sort_by_lowest_price)
                unselect(sort_by_highest_price, btn_sort_by_highest_price)
            }
            SORT_BY_HIGHEST_PRICE -> {
                sort_by_highest_price.typeface = ResourcesCompat.getFont(context, R.font.medium)
                unselect(sort_by_relevance, btn_sort_by_relevance)
                unselect(sort_by_lowest_price, btn_sort_by_lowest_price)
                unselect(sort_by_newest_first, btn_sort_by_newest_first)
            }
            SORT_BY_LOWEST_PRICE -> {
                sort_by_lowest_price.typeface = ResourcesCompat.getFont(context, R.font.medium)
                unselect(sort_by_relevance, btn_sort_by_relevance)
                unselect(sort_by_highest_price, btn_sort_by_highest_price)
                unselect(sort_by_newest_first, btn_sort_by_newest_first)
            }
        }
    }

    private fun unselect(textView: MaterialTextView, radioButton: MaterialRadioButton) {
        textView.typeface = ResourcesCompat.getFont(context, R.font.light)
        radioButton.isChecked = false
    }

    fun setSortBy(sortBy: String?) {
        when (sortBy) {
            SORT_BY_RELEVANCE -> {
                selectedValue = SORT_BY_RELEVANCE
                btn_sort_by_relevance.isChecked = true
            }
            SORT_BY_NEWEST_FIRST -> {
                selectedValue = SORT_BY_NEWEST_FIRST
                btn_sort_by_newest_first.isChecked = true
            }
            SORT_BY_LOWEST_PRICE -> {
                selectedValue = SORT_BY_LOWEST_PRICE
                btn_sort_by_lowest_price.isChecked = true
            }
            SORT_BY_HIGHEST_PRICE -> {
                selectedValue = SORT_BY_HIGHEST_PRICE
                btn_sort_by_highest_price.isChecked = true
            }
        }
    }

    companion object {
        const val SORT_BY_RELEVANCE = "rel"
        const val SORT_BY_NEWEST_FIRST = "newest_first"
        const val SORT_BY_LOWEST_PRICE = "asc"
        const val SORT_BY_HIGHEST_PRICE = "desc"
    }
}