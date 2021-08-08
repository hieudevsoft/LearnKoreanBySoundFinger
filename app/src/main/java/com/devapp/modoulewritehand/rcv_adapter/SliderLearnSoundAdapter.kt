package com.devapp.modoulewritehand.rcv_adapter

import android.app.Activity
import android.media.MediaPlayer
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devapp.modoulewritehand.ItemsWordSound
import com.devapp.modoulewritehand.R
import com.devapp.modoulewritehand.call_back.HandFingerCallback
import com.devapp.modoulewritehand.call_back.TextListener
import com.devapp.modoulewritehand.canvas_element.CanvasHandFinger
import com.devapp.modoulewritehand.diff_util.DiffUltilLearningSound
import com.devapp.modoulewritehand.retrofit.RetrofitInstance
import com.eyalbira.loadingdots.LoadingDots
import com.wajahatkarim3.easyflipview.EasyFlipView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SliderLearnSoundAdapter(private val activity: Activity,val listener:TextListener):RecyclerView.Adapter<SliderLearnSoundAdapter.MyViewHolder>() {
    val listItem = mutableListOf<ItemsWordSound>()
    class MyViewHolder(itemView:View, val activity: Activity,val listener: TextListener): RecyclerView.ViewHolder(itemView) {
        var canvasView: CanvasHandFinger
        private var recycleViewSuggestWord:RecyclerView
        private var adapter: RecycleViewSuggestWordAdapter
        var cvClear : CardView
        var cvUndo : CardView
        var cvDelete : CardView
        var cvUndoText : CardView
        var tvTextFront : TextView
        var tvTextBack:TextView
        var tvPhoneticFront:TextView
        var tvMeanFront:TextView
        var tvPhoneticBack:TextView
        var tvMeanBack:TextView
        var loadingDotPhonetic:LoadingDots
        var loadingDotMean:LoadingDots
        var flipView:EasyFlipView
        var canvasViewCard:CardView
        init {
            val displayMetrics = DisplayMetrics()
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
                activity.display?.getRealMetrics(displayMetrics)
            }else activity.windowManager.defaultDisplay.getRealMetrics(displayMetrics)
            canvasView = itemView.findViewById(R.id.canvasFinger)
            canvasView.init(displayMetrics)
            if(activity is HandFingerCallback)
            canvasView.setCallback(activity as HandFingerCallback)
            recycleViewSuggestWord = itemView.findViewById(R.id.recycleView)
            cvClear = itemView.findViewById(R.id.cvClear)
            cvUndo = itemView.findViewById(R.id.cvUndo)
            tvTextFront = itemView.findViewById(R.id.tvWordSelected)
            cvDelete = itemView.findViewById(R.id.cvDelete)
            cvUndoText = itemView.findViewById(R.id.cvUndoResult)
            tvTextBack = itemView.findViewById(R.id.tvWordBack)
            tvMeanFront = itemView.findViewById(R.id.tvMeanFront)
            tvPhoneticBack = itemView.findViewById(R.id.tvPhoneticBack)
            tvPhoneticFront = itemView.findViewById(R.id.tvPhoneticFront)
            tvMeanBack = itemView.findViewById(R.id.tvMeanBack)
            loadingDotPhonetic = itemView.findViewById(R.id.loadingPhonetic)
            loadingDotMean = itemView.findViewById(R.id.loadingMean)
            flipView = itemView.findViewById(R.id.flipView)
            adapter = RecycleViewSuggestWordAdapter(listener)
            canvasViewCard = itemView.findViewById(R.id.cardCanvasFinger)
            recycleViewSuggestWord.adapter = adapter
        }


        fun setData(itemsWordSound: ItemsWordSound){
            canvasView.setBackground(itemsWordSound.colorCanvas)
            canvasView.setFingerProperty(itemsWordSound.colorBrush,itemsWordSound.sizeBursh)
            canvasViewCard.isVisible = itemsWordSound.isEnable
            adapter.setData(itemsWordSound.listWordSuggest)
            if(itemsWordSound.textUserChoose.isNotEmpty())
            tvTextFront.text = itemsWordSound.textUserChoose.reduce { acc, s -> acc+s  }
            else tvTextFront.text=""
            tvTextBack.text = itemsWordSound.textCorrect
            tvPhoneticFront.text =  "「 ${itemsWordSound.phoneticTextFront} 」"
            tvPhoneticBack.text =  "「 ${itemsWordSound.phoneticTextCorrect} 」"
            tvMeanBack.text = itemsWordSound.meanBack
            tvMeanFront.text = itemsWordSound.meanFront
            loadingDotMean.visibility = if(itemsWordSound.isLoadingWord) View.VISIBLE else View.GONE
            loadingDotPhonetic.visibility = if(itemsWordSound.isLoadingWord) View.VISIBLE else View.GONE
            tvPhoneticFront.visibility = if(itemsWordSound.isLoadingWord) View.GONE else View.VISIBLE
            tvMeanFront.visibility = if(itemsWordSound.isLoadingWord) View.GONE else View.VISIBLE
            flipView.isFlipEnabled = itemsWordSound.enabledFlipView
            if(itemsWordSound.isCorrect!=null && itemsWordSound.enabledFlipView){
                if(itemsWordSound.isCorrect!!){
                    tvMeanFront.setTextColor(ContextCompat.getColor(activity,R.color.teal_700))
                    tvPhoneticFront.setTextColor(ContextCompat.getColor(activity,R.color.teal_700))
                    tvTextFront.setTextColor(ContextCompat.getColor(activity,R.color.teal_700))
                }else{
                    tvMeanFront.setTextColor(ContextCompat.getColor(activity,R.color.design_default_color_error))
                    tvPhoneticFront.setTextColor(ContextCompat.getColor(activity,R.color.design_default_color_error))
                    tvTextFront.setTextColor(ContextCompat.getColor(activity,R.color.design_default_color_error))
                }
            }
        }
        companion object{
            fun from(parent: ViewGroup,activity: Activity,listener: TextListener): MyViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_card_learn_sound,parent,false)
                return MyViewHolder(view,activity,listener = listener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent, activity,listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(listItem[position])
        holder.cvClear.setOnClickListener {
            holder.canvasView.clear()
        }
        holder.cvUndo.setOnClickListener {
            holder.canvasView.undo()
        }
        holder.cvDelete.setOnClickListener {
            listItem[position].textUserChoose.clear()
            listItem[position].phoneticTextFront =""
            listItem[position].meanFront=""
            notifyItemChanged(position)
        }
        holder.cvUndoText.setOnClickListener {
            listItem[position].textUserChoose.removeAt(listItem[position].textUserChoose.size-1)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }
    fun setCanvasBackground(color: Int,position: Int){
        listItem[position].colorCanvas = color
        notifyItemChanged(position)

    }
    fun setColorBrush(color: Int,position: Int){
        listItem[position].colorBrush = color
        notifyItemChanged(position)
    }
    fun setBrushSize(size: Float,position: Int){
        listItem[position].sizeBursh = size
        notifyItemChanged(position)
    }
    fun setMeanAndPhoneticFront(position: Int,mean:String,phonetic:String){
        listItem[position].meanFront = mean
        listItem[position].phoneticTextFront = phonetic
        notifyItemChanged(position)
    }
    fun setData(list:MutableList<ItemsWordSound>){
        val diffUtilLearningSound = DiffUltilLearningSound(listItem,list)
        val diffUtlCallbackResult = DiffUtil.calculateDiff(diffUtilLearningSound)
        this.listItem.clear()
        this.listItem.addAll(list)
        diffUtlCallbackResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
    fun disableExcept(position: Int){
        listItem.forEach { it.isEnable = false }
        listItem[position].isEnable = true
        notifyItemChanged(position)
    }
    fun setRecycleViewForCard(position: Int,list:List<String>){
        listItem[position].listWordSuggest = list
        notifyItemChanged(position)
    }
    fun setLoading(position: Int,isLoading:Boolean){
        listItem[position].isLoadingWord = isLoading
        notifyItemChanged(position)
    }
    fun setTextChooseUser(position: Int,text:String){
        listItem[position].textUserChoose.add(text)
        if(listItem[position].isCorrect==null) listItem[position].isCorrect = false
        else
        listItem[position].isCorrect = listItem[position].textUserChoose.reduce { acc, s -> acc+s  }.toString()==listItem[position].textCorrect
        Log.d("check", "onItemClick: ${listItem[position].isCorrect}")
        notifyItemChanged(position,text)
    }
    fun setEnabledFlipView(enable:Boolean){
        listItem.forEach {
            it.enabledFlipView = enable
            if(it.isCorrect==null) it.isCorrect=false
        }
        notifyDataSetChanged()
    }


}