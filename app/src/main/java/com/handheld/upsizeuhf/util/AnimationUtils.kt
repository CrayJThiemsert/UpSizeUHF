package com.handheld.upsizeuhf.util

import android.content.Context
import android.view.animation.Animation
import com.handheld.upsizeuhf.R

class AnimationUtils {
    companion object {
        fun getBounceAnimation(context: Context): Animation {
            val bounceAnim =
                android.view.animation.AnimationUtils.loadAnimation(context, R.anim.bounce)
            // Use bounce interpolator with amplitude 0.2 and frequency 20
            val interpolator = BounceInterpolator(Constants.BOUNCE_AMPLITUDE, Constants.BOUNCE_FREQUENCY)
            bounceAnim.interpolator = interpolator
            return bounceAnim
        }
    }
}