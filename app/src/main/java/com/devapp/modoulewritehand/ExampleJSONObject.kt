package com.devapp.modoulewritehand

import android.text.Html
import java.util.*

/**
 * Created by Dinh Sam Vu on 1/24/2021.
 */
class ExampleJSONObject {
    var status = 0
    var results: ArrayList<Result>? = null
    var total = 0

    inner class Result {
        private var content: String? = null
        fun getContent(): String? {
            return if (content != null && mean != null) {
                Html.fromHtml(mean).toString()
            } else content
        }

        fun setContent(content: String?) {
            this.content = content
        }

        private var mean: String? = null

        fun setMean(mean: String?) {
            this.mean = Html.fromHtml(mean).toString()
        }

        var transcription: String? = null
    }
}