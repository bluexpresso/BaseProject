package com.idslogic.levelshoes.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.idslogic.levelshoes.utils.VerticalParallaxTransformer
import com.idslogic.levelshoes.utils.ViewTransformer
import timber.log.Timber

class ParallaxImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), ViewTreeObserver.OnScrollChangedListener {
    private val viewLocation: IntArray = IntArray(2)
    var viewTransformer = object : ViewTransformer() {
        override fun apply(view: ParallaxImageView, canvas: Canvas, viewX: Int, viewY: Int) {
            if (view.scaleType == ImageView.ScaleType.CENTER_CROP) {
                view.drawable ?: return
                val imageWidth = view.drawable.intrinsicWidth
                val imageHeight = view.drawable.intrinsicHeight

                val viewWidth = view.width - view.paddingLeft - view.paddingRight
                val viewHeight = view.height - view.paddingTop - view.paddingBottom

                val deviceHeight = view.resources.displayMetrics.heightPixels

                // If this view is off screen we wont do anything
                if (viewY < -viewHeight || viewY > deviceHeight) return

//                if (imageWidth * viewHeight < viewWidth * imageHeight) {
                    val scale = viewWidth.toFloat() / imageWidth.toFloat()
                    val invisibleVerticalArea = imageHeight * scale - viewHeight

                    val y = centeredY(viewY, viewHeight, deviceHeight)
                    val translationScale = invisibleVerticalArea / (deviceHeight + viewHeight)
                    canvas.translate(0f, y * translationScale)
//                }
            }
        }

        override fun onAttached(view: ParallaxImageView) {
            view.scaleType = ImageView.ScaleType.CENTER_CROP
        }

    }
    var enableTransformer = true
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewTreeObserver.addOnScrollChangedListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnScrollChangedListener(this)
    }

    override fun onDraw(canvas: Canvas) {
        if (enableTransformer) {
            getLocationInWindow(viewLocation)
            viewTransformer.apply(this, canvas, viewLocation[0], viewLocation[1])
        }
        super.onDraw(canvas)
    }

    override fun onScrollChanged() {
        if (enableTransformer) {
            Timber.tag("onScrollChanged")
            invalidate()
        }
    }
}