package com.devapp.modoulewritehand

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.devapp.modoulewritehand.call_back.HandFingerCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xw.repo.BubbleSeekBar

class BSDLSeekBar private constructor(): BottomSheetDialogFragment()  {
    private lateinit var handFingerCallback: HandFingerCallback
    fun setHandFingerCallbacl(callback: HandFingerCallback){
        this.handFingerCallback = callback
    }
    companion object{
        private val TAG = "BSDLSeekBar"
        val ratioBrushSize = 5
        var progress = 1f
        fun newInstance(brush_size:Float):BSDLSeekBar {
            val args = Bundle()
            args.putFloat("brush_size",brush_size)
            val fragment = BSDLSeekBar()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var brushSize = arguments?.getFloat("brush_size")
        if(brushSize!=null) progress=brushSize/ ratioBrushSize
        Log.d(TAG, "onCreate: $progress")
        setStyle(STYLE_NORMAL,R.style.SheetDialog)
        super.onCreate(savedInstanceState)
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.layout_seek_bar,null,false)
        dialog.setContentView(view)
        controlSeekbar(view.findViewById(R.id.seekBarBrushSize))

        return dialog
    }
    fun controlSeekbar(seekBar: BubbleSeekBar){
        seekBar.setProgress(progress)
        seekBar.onProgressChangedListener = object:BubbleSeekBar.OnProgressChangedListener{
            override fun onProgressChanged(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Int,
                progressFloat: Float,
                fromUser: Boolean
            ) {
                Log.d(TAG, "onProgressChanged: $progress $progressFloat")
            }

            override fun getProgressOnActionUp(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Int,
                progressFloat: Float
            ) {
                Log.d(TAG, "getProgressOnActionUp: $progress $progressFloat")
                if(progress==0||progressFloat.toInt()==0) seekBar.setProgress(1f)
                else seekBar.setProgress(progress.toFloat())
                handFingerCallback.onBrushSizeChange(progressFloat)
            }

            override fun getProgressOnFinally(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Int,
                progressFloat: Float,
                fromUser: Boolean
            ) {
                Log.d(TAG, "getProgressOnFinally: $progress $progressFloat")
            }

        }

    }
}