package com.devapp.modoulewritehand.ui_support

import android.animation.ObjectAnimator
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.LinearInterpolator

class Animation {
    companion object{
        fun startAnimationFromBottom(view: View,delay:Long,looper: Looper){
            Handler(looper).postDelayed({
                view.animate().translationY(0f).start()
            },delay)
            view.translationY = 150f
        }
    }
}