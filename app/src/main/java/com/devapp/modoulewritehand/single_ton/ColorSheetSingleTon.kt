package com.devapp.modoulewritehand.single_ton

import android.content.Context
import androidx.fragment.app.FragmentManager
import dev.sasikanth.colorsheet.ColorSheet

class ColorSheetSingleTon private constructor(){
    lateinit var colorSheetListener: ColorSheetListener
    interface ColorSheetListener{
        fun onColorSelected(color:Int)
    }
    companion object{
        @JvmStatic
        private lateinit var colorSheet: ColorSheet
        @JvmStatic
        private var instance: ColorSheetSingleTon?=null
        fun getInstance(context: Context): ColorSheetSingleTon {
            if(instance !=null) return instance as ColorSheetSingleTon
            else synchronized(this){
                return ColorSheetSingleTon()
            }
        }

        fun getColorSheet() = colorSheet
    }

    fun setListener(colorSheetListenerParam: ColorSheetListener){
        this.colorSheetListener = colorSheetListenerParam
    }

    fun createColorSheet(colors:IntArray){
        colorSheet = ColorSheet().colorPicker(
            colors = colors,
            noColorOption = true,
            listener = { color ->
                colorSheetListener.onColorSelected(color)
            })
    }
    fun show(supportFragmentManager: FragmentManager){ colorSheet.show(supportFragmentManager)}

}