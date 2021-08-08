package com.devapp.modoulewritehand.mvvm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.devapp.modoulewritehand.ItemsWordSound
import com.devapp.modoulewritehand.MainActivity
import com.devapp.modoulewritehand.retrofit.ApiLinkMp3
import com.devapp.modoulewritehand.retrofit.PostBodyFinger
import com.devapp.modoulewritehand.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlin.math.sin

class LearningSoundViewModel(private val repositoryFingerWrite: RepositoryFingerWrite) :
    ViewModel() {
    private val TAG = "LearningSoundViewModel"
    var currentCard = MutableLiveData<Int>()
    var resultResponse = MutableLiveData<List<Any?>?>()
    var listItemSound = MutableLiveData<List<ItemsWordSound>>()
    var linkMp3 = MutableLiveData<String?>()
    private val linkMp3Api:ApiLinkMp3
    fun setCurrentCard(value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            currentCard.postValue(value)
        }
    }
    init {
        val retrofitBuilderSpeechLink = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl("https://readspeaker.jp/")
            .build()
        linkMp3Api = retrofitBuilderSpeechLink.create(ApiLinkMp3::class.java)
    }
    fun postBody(postBodyFinger: PostBodyFinger) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryFingerWrite.postBody(postBodyFinger)
                .enqueue(object : Callback<List<Any?>> {
                    override fun onResponse(
                        call: Call<List<Any?>>,
                        response: Response<List<Any?>>
                    ) {
                        if (response.code() in 200..209) {
                            if (response.isSuccessful)
                                if (response.body() != null)
                                    resultResponse.postValue(response.body())
                                else Log.e(TAG, "onResponse: ${response.body()}")
                            else Log.e(TAG, "onResponse: failure response")
                        } else {
                            resultResponse.postValue(null)
                        }
                    }

                    override fun onFailure(call: Call<List<Any?>>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${call.timeout()} ${t.message}")
                        resultResponse.postValue(null)
                    }

                })
        }
    }

    fun getLinkMp3(text: String, talkId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            linkMp3Api.getSpeechLink(
                text,
                talkId, //JIHUN 18(MALE), HYERYUN 14(FMALE)
                100,
                85,
                100,
                2,
                0
            ).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.body() != null) {
                        val temp = response.body()!!.substring(5).trim()
                        val sound =
                            "https://readspeaker.jp/ASLCLCLVVS/JMEJSYGDCHMSMHSRKPJL/$temp"
                        Log.e("MainActivity", "onResponseViewModel: $text $talkId $sound" )
                        linkMp3.postValue(sound)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e(TAG, "onFailure: onFailure ${t.message}")
                }
            })
        }
    }

    companion object {
        class LearningSoundViewModelFactory(val repositoryFingerWrite: RepositoryFingerWrite) :
            ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(LearningSoundViewModel::class.java))
                    return LearningSoundViewModel(repositoryFingerWrite) as T
                else throw IllegalArgumentException("Not found isAssignableFrom")
            }
        }
    }
}