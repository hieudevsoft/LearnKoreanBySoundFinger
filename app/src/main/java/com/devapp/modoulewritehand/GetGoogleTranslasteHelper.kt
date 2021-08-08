package com.devapp.modoulewritehand

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException
import java.net.URLEncoder
import java.util.*
import java.util.regex.Pattern

object GetGoogleTranslateHelper {
    var GOOGLE_URL_TRANSLATE =
        "https://translate.googleapis.com/translate_a/single?client=gtx&dt=t&dt=bd&dj=1&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&sl=%s&tl=%s&q=%s" // from, to, query
    var USER_AGENT =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36"
    private val client = OkHttpClient()
    @Throws(JsonSyntaxException::class)
    fun getGoogleTranslate(from: String, to: String, query: String): GoogleTranslateJSONObject? {
        val google_translate_url =
            String.format(GOOGLE_URL_TRANSLATE, from, to, URLEncoder.encode(query))
        val json: String
        val request = Request.Builder()
            .url(google_translate_url)
            .header("charset", "UTF-8")
            .addHeader("User-Agent", USER_AGENT)
            .build()
        val response: Response
        try {
            response = client.newCall(request).execute()
            if (response.body() != null) {
                json = response.body()!!.string()
                Log.d("TAG", "getGoogleTranslate: $json")
                return Gson().fromJson(json, GoogleTranslateJSONObject::class.java)
            }
        } catch (e: IOException) {
            return newGoogleTranslateApi(query, from, to)
        } catch (e: JsonSyntaxException) {
            return newGoogleTranslateApi(query, from, to)
        }
        return null
    }

    // MARK: new google translate api
    fun newGoogleTranslateApi(query: String, from: String, to: String): GoogleTranslateJSONObject? {
        var json: String
        var jsonObject: GoogleTranslateJSONObject? = null
        var google_translate_url =
            "https://translate.google.com/_/TranslateWebserverUi/data/batchexecute?rpcids=MkEWBc&f.sid=9003407303509348738&bl=boq_translate-webserver_20201215.15_p0&hl=vi&soc-app=1&soc-platform=1&soc-device=1&_reqid=828345&rt=c"
        val freq = String.format(
            "[[[\"MkEWBc\",\"[[\\\"%s\\\",\\\"%s\\\",\\\"%s\\\",true],[null]]\",null,\"generic\"]]]",
            query,
            from,
            to
        )
        var requestBody: RequestBody? = FormBody.Builder()
            .add("f.req", freq)
            .add("at", "AD08yZnPhFKfec27OLdMK7AvFyJw:1608511943250")
            .build()
        var request = Request.Builder()
            .url(google_translate_url)
            .post(requestBody)
            .addHeader("User-Agent", USER_AGENT)
            .addHeader("content-type", "application/x-www-form-urlencoded;charset=utf-8")
            .addHeader(
                "Cookie",
                "NID=205=aol2HVd-_cynCwllqjD_FWXCnm-0m1lkKpdi6l5wGV6vNW1KcOyk7JmProtwBxEp1quflqaa3NgbdCSGVRRonEbehVnZiYo-iX_8OOZj-EcZMCg14CgL2u4gZY9ljpUCAM-92yP390AFgu2nKCODpgYLLbFd9yVJhvNeO8JuMh0; 1P_JAR=2020-12-19-09"
            )
            .build()
        try {
            val response = client.newCall(request).execute()
            if (response.body() != null) {
                json = response.body()!!.string()
                val mean = StringBuilder()
                var phonetic = ""
                val gson = Gson()
                val pattern = Pattern.compile("\\[\"wrb.fr\",\"MkEWBc\"(.|\\n)*?\"generic\"]")
                val matcher = pattern.matcher(json)
                while (matcher.find()) {
                    json = matcher.group(0)
                }
                //Log.d("googletranslte", "newGoogleTranslateApi: " + json);
                val listType = object : TypeToken<List<Any?>?>() {}.type
                val arr = gson.fromJson<List<Any>>(json, listType)
                if (arr != null && arr.size > 2) {
                    val arr1String = arr[2] as String
                    val arr1 = gson.fromJson<List<Any?>>(arr1String, listType)
                    if (arr1 != null && arr1.size > 0 && arr1[0] != null) {
                        val arr2 = arr1[0] as List<Any?>?
                        if (arr2 != null && arr2.size > 0 && arr2[0] != null) {
                            phonetic = (arr2[0] as String?)!!.trim { it <= ' ' }
                        }
                    }
                    if (arr1 != null && arr1.size > 1 && arr1[1] != null) {
                        val arr3 = arr1[1] as List<Any?>?
                        Log.d("googletranslte", "newGoogleTranslateApi: $arr3")
                        if (arr3 != null && arr3.size > 0 && arr3[0] != null) {
                            var arr4 = arr3[0] as List<Any>?
                            if (arr4 != null && arr4.size > 0 && arr4[0] != null) {
                                arr4 = arr4[0] as List<Any>
                                if (arr4 != null && arr4.size > 4 && phonetic.isEmpty()) {
                                    for (i in 0..4) {
                                        if (arr4[i] != null) {
                                            try {
                                                val p = (arr4[i] as String).trim { it <= ' ' }
                                                if (!p.isEmpty()) {
                                                    phonetic = p
                                                    break
                                                }
                                            } catch (ignored: ClassCastException) {
                                            }
                                        }
                                    }
                                }
                                if (arr4 != null && arr4.size > 5 && arr4[5] != null) {
                                    val arr5 = arr4[5] as List<Any>
                                    if (arr5 != null && arr5.size > 0) {
                                        for (item5 in arr5) {
                                            if (item5 != null) {
                                                val arr6 = item5 as List<Any>
                                                if (arr6.size > 0) {
                                                    for (arr7 in arr6) {
                                                        if (arr7 != null) {
                                                            try {
                                                                val temp7 = arr7 as List<Any>
                                                                if (temp7 != null) {
                                                                    for (arr8 in temp7) {
                                                                        val temp8 =
                                                                            arr8 as List<Any>
                                                                        if (temp8.size > 0) {
                                                                            val temp =
                                                                                temp8[0] as String
                                                                            if (temp != null) mean.append(
                                                                                "$temp,"
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                            } catch (e: Exception) {
                                                                continue
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (mean.length > 0 && !phonetic.isEmpty()) {
                    jsonObject = createFakeFromString(
                        query,
                        mean.substring(0, mean.toString().length - 1),
                        phonetic
                    )
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
        if (jsonObject == null) {
            google_translate_url =
                "https://www.google.com.vn/async/translate?vet=12ahUKEwjSgcPShM7eAhUH_GEKHaG2D5kQqDgwAHoECAYQFg..i&ei=EQTpW5K1EYf4hwOh7b7ICQ&yv=3"
            requestBody = FormBody.Builder()
                .add(
                    "async",
                    "translate,sl:" + from + ",tl:" + to + ",st:" + URLEncoder.encode(query) + ",id:1541997726654,qc:true,ac:true,_id:tw-async-translate,_pms:s,_fmt:pc"
                )
                .build()
            request = Request.Builder()
                .url(google_translate_url)
                .post(requestBody)
                .addHeader("User-Agent", USER_AGENT)
                .addHeader("content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .build()
            try {
                val response = client.newCall(request).execute()
                if (response.body() != null) {
                    json = response.body()!!.string()
                    var mean = ""
                    var phonetic = ""
                    var pattern =
                        Pattern.compile("<span id=\"tw-answ-target-text\">((.|\\n)*?)</span>")
                    var matcher = pattern.matcher(json)
                    while (matcher.find()) {
                        mean = matcher.group(1)
                    }
                    pattern =
                        Pattern.compile("<span id=\"tw-answ-romanization\">((.|\\n)*?)</span>")
                    matcher = pattern.matcher(json)
                    while (matcher.find()) {
                        phonetic = matcher.group(1).trim { it <= ' ' }
                    }
                    if (phonetic == null || phonetic.isEmpty()) {
                        pattern =
                            Pattern.compile("<span id=\"tw-answ-source-romanization\">((.|\\n)*?)</span>")
                        matcher = pattern.matcher(json)
                        while (matcher.find()) {
                            phonetic = matcher.group(1).trim { it <= ' ' }
                        }
                    }
                    if (mean != null && !mean.isEmpty() && phonetic != null && !phonetic.isEmpty()) {
                        jsonObject = createFakeFromString(query, mean, phonetic)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
            }
        }
        return jsonObject
    }

    private fun createFakeFromString(
        query: String,
        mean: String?,
        phonetic: String?
    ): GoogleTranslateJSONObject {
        val `object` = GoogleTranslateJSONObject()
        val sentences: MutableList<GoogleTranslateJSONObject.Sentence> =
            ArrayList<GoogleTranslateJSONObject.Sentence>()
        val queries = query.split("\n").toTypedArray()
        val means: Array<String?> = mean!!.split("\n").toTypedArray()
        val phonetics: Array<String?> = phonetic!!.split("\n").toTypedArray()
        if (queries.size == means.size && queries.size > 1) {
            for (i in means.indices) {
                val sen: GoogleTranslateJSONObject.Sentence = GoogleTranslateJSONObject.Sentence()
                sen.setOrig(
                    """
                        ${queries[i]}
                        
                        """.trimIndent()
                )
                if (means[i] != null) {
                    sen.setTrans(
                        """
                            ${means[i].toString()}
                            
                            """.trimIndent()
                    )
                }
                if (phonetics.size > i && phonetics[i] != null &&
                    !phonetics[i]!!.isEmpty()
                ) {
                    sen.setSrcTranslit(phonetics[i].toString() + " ")
                }
                sentences.add(sen)
            }
        } else {
            val sen: GoogleTranslateJSONObject.Sentence = GoogleTranslateJSONObject.Sentence()
            sen.setOrig(query)
            if (mean != null) {
                sen.setTrans(mean)
            }
            if (phonetic != null && !phonetic.isEmpty()) {
                sen.setSrcTranslit(phonetic)
            }
            sentences.add(sen)
        }
        `object`.setSentences(sentences)
        return `object`
    }

    fun convertGoogleTranslate2Word(
        googleTranslateJSONObject: GoogleTranslateJSONObject,
        word: String?
    ): WordJSONObject {
        val wordJSONObject = WordJSONObject()
        val datumArrayList: ArrayList<WordJSONObject.Datum> = ArrayList<WordJSONObject.Datum>()
        val datum: WordJSONObject.Datum =
            convertGoogleTranslate2Datum(googleTranslateJSONObject, word)
        datumArrayList.add(datum)
        if (googleTranslateJSONObject.getRelatedWords() != null && googleTranslateJSONObject.getRelatedWords()
                .getWord() != null
        ) {
            for (wordRelative in googleTranslateJSONObject.getRelatedWords().getWord()) {
                val datumOther: WordJSONObject.Datum =
                    WordJSONObject.Datum(false, "", wordRelative, "", "0", null)
                datumArrayList.add(datumOther)
            }
        }
        wordJSONObject.setData(datumArrayList)
        return wordJSONObject
    }

    fun convertGoogleTranslate2Datum(
        googleTranslateJSONObject: GoogleTranslateJSONObject,
        word: String?
    ): WordJSONObject.Datum {
        var origin = ""
        var phonetic: String? = "" // tempMeans = "";
        val means: ArrayList<WordJSONObject.Mean> = ArrayList<WordJSONObject.Mean>()
        if (googleTranslateJSONObject.getSentences() != null && googleTranslateJSONObject.getSentences()
                .size > 0
        ) {
            for (i in 0 until googleTranslateJSONObject.getSentences().size) {
                val p: String = googleTranslateJSONObject.getSentences().get(i).getSrcTranslit()
                if (p != null && !p.isEmpty()) {
                    phonetic += p
                }
            }
        }
        if (googleTranslateJSONObject.getSentences() != null && !googleTranslateJSONObject.getSentences()
                .isEmpty()
        ) {
            val kind = ""
            val mean = StringBuilder()
            for (sentence in googleTranslateJSONObject.getSentences()) {
                if (sentence.getOrig() != null) {
                    origin = sentence.getOrig()
                }
                if (sentence.getTrans() != null) mean.append(if (mean.length == 0) sentence.getTrans() else ", " + sentence.getTrans())
            }
            //            tempMeans = mean.toString();
            means.add(WordJSONObject.Mean(mean.toString(), kind))
        }
        if (googleTranslateJSONObject.getDict() != null && !googleTranslateJSONObject.getDict()
                .isEmpty()
        ) {
            for (dict in googleTranslateJSONObject.getDict()) {
                if (origin != dict.getBaseForm()) continue
                var kind = ""
                val mean = StringBuilder()
                if (dict.getPos() != null) kind = dict.getPos()
                if (dict.getTerms() != null) {
                    for (term in dict.getTerms()) {
                        mean.append(if (mean.length == 0) term else "; $term")
                    }
                }
                means.add(WordJSONObject.Mean(mean.toString(), kind))
            }
        }
        val datum: WordJSONObject.Datum = WordJSONObject.Datum(true, "", word, phonetic, "0", means)

        // tu dong nghia
        datum.setSynsetArrayList(googleTranslateJSONObject.getSynsets())
        return datum
    }

    // MARK : WORD
    fun getMeanAndPhoneticByWord(from: String, to: String, query: String): String {
        val google_translate_url: String = java.lang.String.format(
            GOOGLE_URL_TRANSLATE,
            from,
            to,
            URLEncoder.encode(query)
        )
        var json = ""
        val request = Request.Builder()
            .url(google_translate_url)
            .header("charset", "UTF-8")
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
            .build()
        var response: Response? = null
        try {
            response = client.newCall(request).execute()
            if (response.body() != null) {
                json = response.body()!!.string()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var jsonObject: GoogleTranslateJSONObject?
        try {
            jsonObject = Gson().fromJson(json, GoogleTranslateJSONObject::class.java)
            Log.d("gson", "getMeanAndPhoneticByWord: " + Gson().toJson(json))
        } catch (e: JsonSyntaxException) {
            jsonObject = newGoogleTranslateApi(query, from, to)
        }
        if (jsonObject?.getSentences() == null) return ""
        var trans = StringBuilder()
        var tran = ""
        if (jsonObject.getSentences().get(0).getTrans() != null) {
            tran = jsonObject.getSentences().get(0).getTrans()
            trans = StringBuilder(tran)
        }
        if (jsonObject.getDict() != null && !jsonObject.getDict().isEmpty()) {
            for (dict in jsonObject.getDict()) {
                if (dict.getTerms() != null && !dict.getTerms().isEmpty()) {
                    for (m in dict.getTerms()) {
                        if (tran.toLowerCase() != m.trim { it <= ' ' }.toLowerCase()) {
                            trans.append(", ").append(m)
                        }
                    }
                }
            }
        }
        val srcTranslate = StringBuilder()
        try {

            if (jsonObject.sentences.size > 0) {
                for (i in 0 until jsonObject.sentences.size) {
                    try{
                        val p: String = jsonObject.sentences[i].srcTranslit
                        if (p != null && !p.isEmpty()) {
                            srcTranslate.append(p)
                        }else {
                            continue
                            Log.d("MainAcivity", "getMeanAndPhoneticByWord: ${Gson().toJson(jsonObject.sentences)}")
                        }
                    }catch (e:Exception){
                        continue
                    }
                }
            }
        }catch (e:Exception){

        }

        return query + "_" + trans.toString() + "_" + srcTranslate
    }
}
