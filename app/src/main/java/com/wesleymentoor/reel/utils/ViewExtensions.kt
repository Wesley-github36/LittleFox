package com.wesleymentoor.reel.utils

import android.graphics.drawable.AnimationDrawable
import android.widget.ImageView

/**
Animation drawable to animate a loading bar
 */
fun ImageView.animationDrawable(): AnimationDrawable {
    // Get the drawable, which has been compiled to an AnimationDrawable object.
    return drawable as AnimationDrawable
}