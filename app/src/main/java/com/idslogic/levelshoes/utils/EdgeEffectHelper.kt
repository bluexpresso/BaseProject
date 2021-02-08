package com.idslogic.levelshoes.utils

import android.content.Context
import android.graphics.*
import android.os.Build
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.EdgeEffect
import androidx.annotation.ColorInt
import kotlin.math.*


class EdgeEffectHelper(context: Context?) : EdgeEffect(context) {
    private var mGlowAlpha = 0f
    private var mGlowScaleY = 0f
    private var mGlowAlphaStart = 0f
    private var mGlowAlphaFinish = 0f
    private var mGlowScaleYStart = 0f
    private var mGlowScaleYFinish = 0f
    private var mStartTime: Long = 0
    private var mDuration = 0f
    private val mInterpolator: Interpolator
    private var mState = STATE_IDLE
    private var mPullDistance = 0f
    private val mBounds: Rect = Rect()
    private val mPaint: Paint = Paint()
    private var mRadius = 0f
    private var mBaseGlowScale = 0f
    private var mDisplacement = 0.5f
    private var mTargetDisplacement = 0.5f
    override fun setSize(width: Int, height: Int) {
        val r = width * RADIUS_FACTOR / SIN
        val y = COS * r
        val h = r - y
        val or = height * RADIUS_FACTOR / SIN
        val oy = COS * or
        val oh = or - oy
        mRadius = r
        mBaseGlowScale = if (h > 0) min(oh / h, 1f) else 1f
        mBounds.set(
            mBounds.left, mBounds.top, width,
            min(height.toFloat(), h).toInt()
        )
        mPaint.setMaskFilter(
            BlurMaskFilter(
                mBounds.bottom * BlUR_RADIUS_FACTOR,
                BlurMaskFilter.Blur.NORMAL
            )
        )
    }

    override fun isFinished(): Boolean {
        return mState == STATE_IDLE
    }

    override fun finish() {
        mState = STATE_IDLE
    }

    override fun onPull(deltaDistance: Float) {
        onPull(deltaDistance, 0.5f)
    }

    override fun onPull(deltaDistance: Float, displacement: Float) {
        val now: Long = AnimationUtils.currentAnimationTimeMillis()
        mTargetDisplacement = displacement
        if (mState == STATE_PULL_DECAY && now - mStartTime < mDuration) {
            return
        }
        if (mState != STATE_PULL) {
            mGlowScaleY = max(PULL_GLOW_BEGIN, mGlowScaleY)
        }
        mState = STATE_PULL
        mStartTime = now
        mDuration = PULL_TIME.toFloat()
        mPullDistance += deltaDistance
        val absdd = abs(deltaDistance)
        mGlowAlphaStart = min(
            MAX_ALPHA,
            mGlowAlpha + absdd * PULL_DISTANCE_ALPHA_GLOW_FACTOR
        )
        mGlowAlpha = mGlowAlphaStart
        if (mPullDistance == 0f) {
            mGlowScaleYStart = 0f
            mGlowScaleY = mGlowScaleYStart
        } else {
            val scale = (max(
                0.0, 1 - 1 /
                        sqrt(abs(mPullDistance) * mBounds.height()) - 0.3
            ) / 0.7).toFloat()
            mGlowScaleYStart = scale
            mGlowScaleY = mGlowScaleYStart
        }
        mGlowAlphaFinish = mGlowAlpha
        mGlowScaleYFinish = mGlowScaleY
    }

    override fun onRelease() {
        mPullDistance = 0f
        if (mState != STATE_PULL && mState != STATE_PULL_DECAY) {
            return
        }
        mState = STATE_RECEDE
        mGlowAlphaStart = mGlowAlpha
        mGlowScaleYStart = mGlowScaleY
        mGlowAlphaFinish = 0f
        mGlowScaleYFinish = 0f
        mStartTime = AnimationUtils.currentAnimationTimeMillis()
        mDuration = RECEDE_TIME.toFloat()
    }

    override fun onAbsorb(velocity: Int) {
        var velocity = velocity
        mState = STATE_ABSORB
        velocity = min(max(MIN_VELOCITY, abs(velocity)), MAX_VELOCITY)
        mStartTime = AnimationUtils.currentAnimationTimeMillis()
        mDuration = 0.15f + velocity * 0.02f

        // The glow depends more on the velocity, and therefore starts out
        // nearly invisible.
        mGlowAlphaStart = GLOW_ALPHA_START
        mGlowScaleYStart = max(mGlowScaleY, 0f)


        // Growth for the size of the glow should be quadratic to properly
        // respond
        // to a user's scrolling speed. The faster the scrolling speed, the more
        // intense the effect should be for both the size and the saturation.
        mGlowScaleYFinish = min(0.025f + velocity * (velocity / 100) * 0.00015f / 2, 1f)
        // Alpha should change for the glow as well as size.
        mGlowAlphaFinish = max(
            mGlowAlphaStart, min(velocity * VELOCITY_GLOW_FACTOR * .00001f, MAX_ALPHA)
        )
        mTargetDisplacement = 0.5f
    }

    override fun setColor(@ColorInt color: Int) {
        mPaint.setColor(color)
    }

    override fun setBlendMode(blendmode: BlendMode?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mPaint.setBlendMode(blendmode)
        }
    }

    @ColorInt
    override fun getColor(): Int {
        return mPaint.getColor()
    }

    override fun getBlendMode(): BlendMode? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mPaint.getBlendMode()
        } else {
            null
        }
    }

    override fun draw(canvas: Canvas): Boolean {
        update()
        val count = canvas.save()
        val centerX: Int = mBounds.centerX()
        val centerY: Float = mBounds.height() - mRadius
        canvas.scale(1f, min(mGlowScaleY, 1f) * mBaseGlowScale, centerX.toFloat(), 0f)
        val displacement = max(0f, min(mDisplacement, 1f)) - 0.5f
        val translateX: Float = mBounds.width() * displacement / 2
        canvas.clipRect(
            mBounds.left.toFloat(),
            mBounds.top.toFloat(),
            mBounds.right.toFloat(),
            mBounds.bottom + mBounds.bottom * BlUR_RADIUS_FACTOR
        )
        canvas.translate(translateX, 0f)
        //        we don't change alpha during overscroll animation
//        mPaint.setAlpha((int) (0xff * mGlowAlpha));
        canvas.drawCircle(centerX.toFloat(), centerY, mRadius, mPaint)
        canvas.restoreToCount(count)
        var oneLastFrame = false
        if (mState == STATE_RECEDE && mGlowScaleY == 0f) {
            mState = STATE_IDLE
            oneLastFrame = true
        }
        return mState != STATE_IDLE || oneLastFrame
    }

    /**
     * Return the maximum height that the edge effect will be drawn at given the original
     * [input size][.setSize].
     *
     * @return The maximum height of the edge effect
     */
    override fun getMaxHeight(): Int {
        return ((mBounds.height() * MAX_GLOW_SCALE + 0.5f).toInt())
    }

    private fun update() {
        val time: Long = AnimationUtils.currentAnimationTimeMillis()
        val t = min((time - mStartTime) / mDuration, 1f)
        val interp: Float = mInterpolator.getInterpolation(t)
        mGlowAlpha = mGlowAlphaStart + (mGlowAlphaFinish - mGlowAlphaStart) * interp
        mGlowScaleY = mGlowScaleYStart + (mGlowScaleYFinish - mGlowScaleYStart) * interp
        mDisplacement = (mDisplacement + mTargetDisplacement) / 2
        if (t >= 1f - EPSILON) {
            when (mState) {
                STATE_ABSORB -> {
                    mState = STATE_RECEDE
                    mStartTime = AnimationUtils.currentAnimationTimeMillis()
                    mDuration = RECEDE_TIME.toFloat()
                    mGlowAlphaStart = mGlowAlpha
                    mGlowScaleYStart = mGlowScaleY

                    // After absorb, the glow should fade to nothing.
                    mGlowAlphaFinish = 0f
                    mGlowScaleYFinish = 0f
                }
                STATE_PULL -> {
                    mState = STATE_PULL_DECAY
                    mStartTime = AnimationUtils.currentAnimationTimeMillis()
                    mDuration = PULL_DECAY_TIME.toFloat()
                    mGlowAlphaStart = mGlowAlpha
                    mGlowScaleYStart = mGlowScaleY

                    // After pull, the glow should fade to nothing.
                    mGlowAlphaFinish = 0f
                    mGlowScaleYFinish = 0f
                }
                STATE_PULL_DECAY -> mState = STATE_RECEDE
                STATE_RECEDE -> mState = STATE_IDLE
            }
        }
    }

    companion object {
        private const val TAG = "GlowingEdgeEffect"

        // Time it will take the effect to fully recede in ms
        private const val RECEDE_TIME = 600

        // Time it will take before a pulled glow begins receding in ms
        private const val PULL_TIME = 167

        // Time it will take in ms for a pulled glow to decay to partial strength before release
        private const val PULL_DECAY_TIME = 2000
        private const val MAX_ALPHA = 0.15f
        private const val GLOW_ALPHA_START = .09f
        private const val MAX_GLOW_SCALE = 2f
        private const val PULL_GLOW_BEGIN = 0f

        // Minimum velocity that will be absorbed
        private const val MIN_VELOCITY = 100

        // Maximum velocity, clamps at this value
        private const val MAX_VELOCITY = 10000
        private const val EPSILON = 0.001f
        private const val ANGLE = Math.PI / 8
        private val SIN = sin(ANGLE).toFloat()
        private val COS = cos(ANGLE).toFloat()
        private const val RADIUS_FACTOR = 0.6f
        private const val DEFAULT_COLOR = -0x34ff0100
        private const val STATE_IDLE = 0
        private const val STATE_PULL = 1
        private const val STATE_ABSORB = 2
        private const val STATE_RECEDE = 3
        private const val STATE_PULL_DECAY = 4
        private const val PULL_DISTANCE_ALPHA_GLOW_FACTOR = 0.8f
        private const val VELOCITY_GLOW_FACTOR = 6
        private const val BlUR_RADIUS_FACTOR = 0.5f
    }

    init {
        mPaint.setAntiAlias(true)
        mPaint.setColor(DEFAULT_COLOR)
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mPaint.setBlendMode(DEFAULT_BLEND_MODE)
        }
        mInterpolator = DecelerateInterpolator()
    }
}