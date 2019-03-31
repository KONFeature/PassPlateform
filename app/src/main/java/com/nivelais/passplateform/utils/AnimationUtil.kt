package com.nivelais.passplateform.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.Gravity
import android.view.View
import android.view.ViewAnimationUtils
import androidx.transition.Slide
import androidx.transition.Transition
import kotlinx.android.synthetic.main.activity_open_db.*

object AnimationUtil {

    /**
     * Get a slide up transition
     */
    fun slideUpTransition(): Transition {
        val slide = Slide(Gravity.TOP)

        return slide
    }

    /**
     * Get a slide down transition
     */
    fun slideDownTransition(): Transition {
        val slide = Slide(Gravity.BOTTOM)

        return slide
    }

}