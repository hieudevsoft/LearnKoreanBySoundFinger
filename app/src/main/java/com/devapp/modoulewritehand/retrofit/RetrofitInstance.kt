package com.devapp.modoulewritehand.retrofit

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


class RetrofitInstance private constructor() {
    private val okHttp : OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .readTimeout(3,TimeUnit.SECONDS)
            .connectTimeout(20,TimeUnit.SECONDS)
            .writeTimeout(3,TimeUnit.SECONDS)
            .build()
    }

    private val retrofit:Retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl("https://inputtools.google.com/")
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }
    val getApiServices = retrofit.create(ApiServices::class.java)
    companion object{
        @JvmStatic
        private var instance: RetrofitInstance?=null
        fun getInstance(): RetrofitInstance {
            if(instance !=null) return instance as RetrofitInstance
            else synchronized(this){
                return RetrofitInstance()
            }
        }
    }

}
interface ApiServices{
    @POST("request?itc=ko-t-i0-handwrit&app=chext")
    fun postHandWriting(@Body postBodyFinger: PostBodyFinger): Call<List<Any?>>

}
interface ApiLinkMp3{
    @FormUrlEncoded
    @Headers("content-type: application/x-www-form-urlencoded; charset=UTF-8")
    @POST("/tomcat/servlet/vt")
    fun getSpeechLink(
        @Field("text") text: String,
        @Field("talkID") talkID: Int,
        @Field("volume") volume: Int,
        @Field("speed") speed: Int,
        @Field("pitch") pitch: Int,
        @Field("feeling") feeling: Int,
        @Field("dict") dict: Int
    ): Call<String>
}