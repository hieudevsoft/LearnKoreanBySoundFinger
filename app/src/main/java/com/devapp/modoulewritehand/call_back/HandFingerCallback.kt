package com.devapp.modoulewritehand.call_back

import com.devapp.modoulewritehand.FingerPath

interface HandFingerCallback {
    fun onBrushSizeChange(size: Float)
    fun onDrawFinger(listFingerPath:List<FingerPath>)
}