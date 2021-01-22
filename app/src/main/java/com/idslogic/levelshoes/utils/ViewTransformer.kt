package com.idslogic.levelshoes.utils

import android.graphics.Canvas
import com.idslogic.levelshoes.custom.ParallaxImageView

abstract class ViewTransformer {

    /**
     * This will be called when it's being set into the ScrollTransformImageView
     */
    open fun onAttached(view : ParallaxImageView) {}

    /**
     * This will be called when it's being removed or replaced by other viewTransformer
     * from the ScrollTransformImageView
     */
    open fun onDetached(view : ParallaxImageView) {}

    /**
     * apply will be called every time the view scrolled and before rendered
     */
    abstract fun apply(view: ParallaxImageView, canvas: Canvas, viewX : Int, viewY : Int)

    /**
     * Convert top,left (0,0) coordinate
     * to the middle of the screen
     */
    protected fun centeredX(x : Int, viewWidth : Int, screenWidth : Int) = x + (viewWidth / 2) - (screenWidth / 2)
    protected fun centeredY(y : Int, viewHeight : Int, screenHeight : Int) = (screenHeight / 2) - (y + (viewHeight / 2))
}