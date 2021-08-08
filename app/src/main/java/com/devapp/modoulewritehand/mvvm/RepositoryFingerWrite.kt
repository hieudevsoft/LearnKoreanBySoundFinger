package com.devapp.modoulewritehand.mvvm

import com.devapp.modoulewritehand.retrofit.ApiServices
import com.devapp.modoulewritehand.retrofit.PostBodyFinger
import retrofit2.Call

class RepositoryFingerWrite(private val apiServices: ApiServices){
    fun postBody(postBodyFinger: PostBodyFinger): Call<List<Any?>> {
        return apiServices.postHandWriting(postBodyFinger)
    }
}