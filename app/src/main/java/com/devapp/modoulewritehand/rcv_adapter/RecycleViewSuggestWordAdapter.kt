package com.devapp.modoulewritehand.rcv_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devapp.modoulewritehand.R
import com.devapp.modoulewritehand.call_back.TextListener
import com.devapp.modoulewritehand.diff_util.DiffUtilSuggestWord

class RecycleViewSuggestWordAdapter(val listener:TextListener):
    RecyclerView.Adapter<RecycleViewSuggestWordAdapter.MyViewHolder>() {
    private var listWords:List<String> = emptyList()
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var mSuggestWord:TextView = itemView.findViewById(R.id.tvSuggestWords)
        fun setData(string:String){
            mSuggestWord.text = string
        }
        companion object{
            fun from(parent: ViewGroup): MyViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.suggest_word_card,parent,false)
                return MyViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(listWords[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(holder.mSuggestWord.text.toString())
        }
    }
    fun setData(newList:List<String>){
        val diffUtil = DiffUtilSuggestWord(listWords,newList)
        val diffUtlCallbackResult = DiffUtil.calculateDiff(diffUtil)
        listWords = newList
        diffUtlCallbackResult.dispatchUpdatesTo(this)
    }
    override fun getItemCount(): Int {
        return listWords.size
    }
}