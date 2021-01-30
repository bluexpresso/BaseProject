package com.idslogic.levelshoes.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.idslogic.levelshoes.R
import kotlinx.android.synthetic.main.view_shimmer_loading.view.*

class ShimmerLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    companion object {
        const val TYPE_PRODUCT_GRID = 1
        const val TYPE_PRODUCT_LIST = 2
    }

    init {
        inflate(context, R.layout.view_shimmer_loading, this)
        view_shimmer.layoutManager = GridLayoutManager(context, 2)
        view_shimmer.adapter = ShimmerLoadingAdapter(20, TYPE_PRODUCT_GRID)
    }

    class ShimmerLoadingAdapter(private val size: Int, private val type: Int) :
        RecyclerView.Adapter<ShimmerLoadingAdapter.ViewHolder>() {
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_view_shimmer_product_grid,
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindingAdapter
        }

        override fun getItemCount() = size

    }
}