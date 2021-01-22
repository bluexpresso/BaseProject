package com.idslogic.levelshoes.utils

import android.graphics.Canvas
import android.widget.ImageView
import com.idslogic.levelshoes.custom.ParallaxImageView

class HorizontalParallaxTransformer : ViewTransformer() {

    override fun onAttached(view: ParallaxImageView) {
        view.scaleType = ImageView.ScaleType.CENTER_CROP
    }

    override fun apply(view: ParallaxImageView, canvas: Canvas, viewX : Int, viewY : Int) {
        if (view.scaleType == ImageView.ScaleType.CENTER_CROP) {
            val imageWidth = view.drawable.intrinsicWidth
            val imageHeight = view.drawable.intrinsicHeight

            val viewWidth = view.width - view.paddingLeft - view.paddingRight
            val viewHeight = view.height - view.paddingTop - view.paddingBottom

            val deviceWidth = view.resources.displayMetrics.widthPixels

            if (viewX < -viewWidth || viewX > deviceWidth) return

            if (imageWidth * viewHeight > viewWidth * imageHeight) {
                val scale = viewHeight.toFloat() / imageHeight.toFloat()
                val invisibleHorizontalArea = imageWidth * scale - viewWidth

                val x = centeredX(viewX, viewWidth, deviceWidth)
                val translationScale = invisibleHorizontalArea / (deviceWidth + viewWidth)
                canvas.translate(-x * translationScale, 0f)
            }
        }
    }

}