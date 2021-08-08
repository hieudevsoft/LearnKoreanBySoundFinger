package com.devapp.modoulewritehand.single_ton

import android.app.Activity
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import com.example.awesomedialog.*

class AwesomeDialogObject private constructor(){
    companion object{
        fun showDialog(
            activity: Activity,
            onTouchOutside:Boolean,
            title:String,
            icon:Int,
            body:String="",
            textPositive:String,
            callBackPositive: (() -> Unit)? =null,
            textNegative:String,
            callBackNegative: (() -> Unit)? =null,
            ){
            val dialog = AwesomeDialog
                .build(activity)
                .position(AwesomeDialog.POSITIONS.CENTER)
                .icon(icon,true)
                .title(
                    title,
                    titleColor = ContextCompat.getColor(activity, android.R.color.black)
                )
                .body(
                    body,
                    Typeface.MONOSPACE,
                    color = ContextCompat.getColor(activity, android.R.color.holo_blue_light)
                )
                .background(R.drawable.layout_rounded_white)
            dialog.setCanceledOnTouchOutside(onTouchOutside)
            if(callBackNegative!=null && textNegative.isNotEmpty()) dialog.onNegative(textNegative,action =  callBackNegative )
            if(callBackPositive!=null && textPositive.isNotEmpty()) dialog.onPositive(textPositive,action =  callBackPositive )
        }
    }
}