package com.idslogic.levelshoes.custom

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.FilterOption
import kotlinx.android.synthetic.main.view_filter_by.view.*
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.HashMap

class FilterByView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_filter_by, this)
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.FilterByView, 0, 0
        ).apply {
            title.text = try {
                getString(R.styleable.FilterByView_title) ?: ""
            } catch (e: Exception) {
                ""
            }
        }
    }

    fun setFilter(mapFilters: HashMap<String, FilterOption>?) {
        if (!mapFilters.isNullOrEmpty()) {
            val sbFilter = StringBuilder()
            var filterSize = 0
            mapFilters.values.forEach {
                sbFilter.append("${it.name},")
                filterSize = filterSize.plus(1)
            }
            sbFilter.deleteCharAt(sbFilter.length - 1)
            selected_filter.text = String.format(Locale.ENGLISH, "(%d) %s", filterSize, sbFilter)
        }
    }

    fun setFilter(filter: String?) {
        selected_filter.text = filter
    }

    var onFilterClicked: (() -> Unit)? = null

}