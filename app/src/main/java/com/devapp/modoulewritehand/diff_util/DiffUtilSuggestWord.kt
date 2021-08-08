package com.devapp.modoulewritehand.diff_util

import androidx.recyclerview.widget.DiffUtil
import com.devapp.modoulewritehand.ItemsWordSound

class DiffUtilSuggestWord(private var oldList:List<String>, private var newList: List<String>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]==newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]===newList[newItemPosition]
    }
}