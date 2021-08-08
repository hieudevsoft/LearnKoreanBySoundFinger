package com.devapp.modoulewritehand


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.devapp.modoulewritehand.call_back.HandFingerCallback
import com.devapp.modoulewritehand.call_back.TextListener
import com.devapp.modoulewritehand.databinding.ActivityMainBinding
import com.devapp.modoulewritehand.mvvm.LearningSoundViewModel
import com.devapp.modoulewritehand.mvvm.RepositoryFingerWrite
import com.devapp.modoulewritehand.rcv_adapter.SliderLearnSoundAdapter
import com.devapp.modoulewritehand.retrofit.PostBodyFinger
import com.devapp.modoulewritehand.retrofit.Request
import com.devapp.modoulewritehand.retrofit.RetrofitInstance
import com.devapp.modoulewritehand.retrofit.WritingGuide
import com.devapp.modoulewritehand.single_ton.AwesomeDialogObject
import com.devapp.modoulewritehand.single_ton.ColorSheetSingleTon
import com.devapp.modoulewritehand.ui_support.Animation
import com.devapp.modoulewritehand.ui_support.HorizontalMarginItemDecoration
import com.example.awesomedialog.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), HandFingerCallback {
    private val TAG = "MainActivity"
    private lateinit var _binding: ActivityMainBinding
    private lateinit var adapter: SliderLearnSoundAdapter
    private lateinit var learningSoundViewModel: LearningSoundViewModel
    private var controlCompleteLern = true
    private var mediaPlayer: MediaPlayer? = MediaPlayer()
    companion object {
        @JvmStatic
        private var didRetriever = false
        @JvmStatic
        private var talkID = 14
        @JvmStatic
        private var oldLinkMp3 = ""
    }

    private val binding get() = _binding!!
    val listTemp = mutableListOf(
        ItemsWordSound(textCorrect = "포트"),
        ItemsWordSound(textCorrect = "차"),
        ItemsWordSound(textCorrect = "수입"),
        ItemsWordSound(textCorrect = "포트"),
        ItemsWordSound(textCorrect = "태양"),
        ItemsWordSound(textCorrect = "달"),
        ItemsWordSound(textCorrect = "개"),
        ItemsWordSound(textCorrect = "고양이")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fullScreen()
        learningSoundViewModel = ViewModelProvider(
            this, LearningSoundViewModel.Companion.LearningSoundViewModelFactory(
                RepositoryFingerWrite(RetrofitInstance.getInstance().getApiServices)
            )
        ).get(LearningSoundViewModel::class.java)
        registerInternet()
        getDataCorrect()
        learningSoundViewModel.listItemSound.observe(this, { it ->
            if (it.any { it.meanBack.isEmpty() || it.phoneticTextCorrect.isEmpty() }) {
                binding.placeHolderInternet.visibility = View.VISIBLE
                binding.mainLayout.visibility = View.GONE
            }
            else {
                binding.placeHolderInternet.visibility = View.GONE
                binding.mainLayout.visibility = View.VISIBLE
                Log.d(TAG, "onCreate: $it")
                didRetriever = true
                initViewpager()
                controlSwipeCard()
                animationBottomConfig()
                adapter.setData(it.toMutableList())
                adapter.disableExcept(0)
            }
        })
        addDecorationViewPager()
        binding.cvColorCanvas.setOnClickListener {
            val colorSheetSingleTon = ColorSheetSingleTon.getInstance(this)
            colorSheetSingleTon.setListener(object : ColorSheetSingleTon.ColorSheetListener {
                override fun onColorSelected(color: Int) {
                    adapter.setCanvasBackground(color, binding.viewPager2.currentItem)
                }
            })
            colorSheetSingleTon.createColorSheet(resources.getIntArray(R.array.ColorSource))
            colorSheetSingleTon.show(supportFragmentManager)
        }
        binding.cvColorBrush.setOnClickListener {
            val colorSheetSingleTon = ColorSheetSingleTon.getInstance(this)
            colorSheetSingleTon.setListener(object : ColorSheetSingleTon.ColorSheetListener {
                override fun onColorSelected(color: Int) {
                    adapter.setColorBrush(color, binding.viewPager2.currentItem)
                }
            })
            colorSheetSingleTon.createColorSheet(resources.getIntArray(R.array.ColorSource))
            colorSheetSingleTon.show(supportFragmentManager)
        }
        binding.cvBrushSize.setOnClickListener {
            val seekBarBottom = BSDLSeekBar.newInstance(5f)
            seekBarBottom.setHandFingerCallbacl(object : HandFingerCallback {
                override fun onBrushSizeChange(size: Float) {
                    adapter.setBrushSize(size * 5f, binding.viewPager2.currentItem)
                }

                override fun onDrawFinger(listFingerPath: List<FingerPath>) {
                }

            })
            seekBarBottom.show(supportFragmentManager, seekBarBottom.tag)
        }
        binding.cvSoundTalk.setOnClickListener {
            val correctText = adapter.listItem[binding.viewPager2.currentItem].textCorrect
            lifecycleScope.launch(Dispatchers.IO) {
                learningSoundViewModel.getLinkMp3(correctText, talkID)
            }
            learningSoundViewModel.linkMp3.observe(this@MainActivity, {
                Log.e(TAG, "onResponse: $correctText $talkID $it $oldLinkMp3" )
                if(oldLinkMp3!=it){
                    playAudio(it)
                    if (it != null) {
                        oldLinkMp3=it
                    }
                }

            })
            binding.cvSoundTalk.isEnabled = false
            Handler(mainLooper).postDelayed({
                binding.cvSoundTalk.isEnabled = true
            }, 2000)
        }
        binding.cvSoundTalk.setOnLongClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.layout_choose_talk_type)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val cvJihun = dialog.findViewById<CardView>(R.id.cvJihun)
            val cvEunHee = dialog.findViewById<CardView>(R.id.cvEunHee)
            cvJihun.setOnClickListener {
                talkID = 18
                dialog.dismiss()
            }
            cvEunHee.setOnClickListener {
                talkID = 14
                dialog.dismiss()
            }
            return@setOnLongClickListener true
        }
        binding.cvFinishActivity.setOnClickListener {
            if (!isFinishing) this.finish()
        }
        binding.cvComplete.setOnClickListener {
            if (controlCompleteLern) {
                controlCompleteLern = false
                AwesomeDialogObject.showDialog(
                    activity = this,
                    onTouchOutside = false,
                    title = "Notification~",
                    body = "Do you want check result?",
                    icon = R.drawable.ic_agree_learn_sounding,
                    textPositive = "OK",
                    callBackPositive = { clickOkDialogResult() },
                    textNegative = "CANCEL",
                    callBackNegative = { }
                )
            } else {
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.layout_dialog_try_again_learn_by_sound)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setCanceledOnTouchOutside(true)
                val edtNumber = dialog.findViewById<EditText>(R.id.edtNumber)
                val tvNumberWord = dialog.findViewById<TextView>(R.id.tvNumberWord)
                tvNumberWord.text = "/ ${listTemp.size}"
                edtNumber.addTextChangedListener {
                    if (it != null) {
                        try {
                            if (it.toString().toInt() <= 0) edtNumber.setText("1")
                            if (it.toString()
                                    .toInt() > listTemp.size
                            ) edtNumber.setText(listTemp.size.toString())
                        } catch (e: Exception) {

                        }

                    }
                }
                dialog.findViewById<Button>(R.id.btnOk).setOnClickListener {
                    controlCompleteLern = true
                    try {

                        val number = edtNumber.text.toString().toInt()
                        listTemp.forEach {
                            it.isCorrect = null
                            it.isEnable = false
                            it.isLoadingWord = false
                            it.enabledFlipView = false
                            it.listWordSuggest = emptyList()
                            it.textUserChoose = mutableListOf()
                            it.phoneticTextFront = ""
                            it.meanFront = ""
                        }
                        binding.imgComplete.setImageResource(R.drawable.ic_check_canvas)
                        learningSoundViewModel.listItemSound.postValue(
                            listTemp.take(number).shuffled()
                        )
                        Log.d(TAG, "onCreate: ListItemTemp: $listTemp")
                        dialog.dismiss()

                    } catch (e: Exception) {

                    }


                }
                dialog.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                    if (dialog.isShowing) dialog.dismiss()
                }
                dialog.show()
            }

        }
        subcribeViewModel()
    }

    private fun addDecorationViewPager() {
        val itemDecoration = HorizontalMarginItemDecoration(
            this,
            R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.viewPager2.addItemDecoration(itemDecoration)
    }

    fun clickOkDialogResult() {
        adapter.setEnabledFlipView(true)
        AwesomeDialogObject.showDialog(
            activity = this@MainActivity,
            onTouchOutside = false,
            title = "${listTemp.filter { it.isCorrect != null && it.isCorrect!! }.size}/${adapter.itemCount}",
            body = "Click the text to show correct answer",
            icon = R.drawable.ic_result,
            textPositive = "OK",
            callBackPositive = { binding.imgComplete.setImageResource(R.drawable.ic_tryagain_learn_sounding) },
            textNegative = "",
            callBackNegative = {}
        )
    }

    private fun getDataCorrect() {
        runBlocking {
            learningSoundViewModel.listItemSound.postValue(listTemp)
            lifecycleScope.launch(Dispatchers.IO) {
                listTemp.forEach {
                    val result = async {
                        GetGoogleTranslateHelper.getMeanAndPhoneticByWord(
                            "ko", "en",
                            it.textCorrect
                        )
                    }.await()
                    if (result != null) {
                        launch(Dispatchers.Main) {
                            try {
                                var transform = result.split("_")[1].split(",")[0]
                                transform =
                                    transform.substring(0, 1).uppercase() + transform.substring(1)
                                        .lowercase()
                                it.meanBack = transform
                                it.phoneticTextCorrect = result.split("_")[2]
                                learningSoundViewModel.listItemSound.postValue(listTemp)
                            } catch (e: Exception) {

                            }

                        }
                    }

                }
            }
        }
    }

    private fun animationBottomConfig() {
        Animation.startAnimationFromBottom(binding.cvColorCanvas, 800, mainLooper)
        Animation.startAnimationFromBottom(binding.cvColorBrush, 1200, mainLooper)
        Animation.startAnimationFromBottom(binding.cvBrushSize, 1600, mainLooper)
        Animation.startAnimationFromBottom(binding.cvSoundTalk, 2000, mainLooper)
    }

    private val listenerOnClick = object : TextListener {
        override fun onItemClick(s: String) {
            adapter.setTextChooseUser(binding.viewPager2.currentItem, s)
            adapter.setLoading(binding.viewPager2.currentItem, true)
            lifecycleScope.launch(Dispatchers.IO) {
                Log.d(TAG, "onItemClick: loading...")
                val result = async {
                    GetGoogleTranslateHelper.getMeanAndPhoneticByWord("ko", "en",
                        listTemp[binding.viewPager2.currentItem].textUserChoose.reduce { acc, s -> acc + s }
                    )
                }.await()
                if (result != null) {
                    Log.d(TAG, "onItemClick: $result")
                    Log.d(TAG, "onItemClick: Ok")
                    launch(Dispatchers.Main) {
                        try {
                            adapter.setLoading(binding.viewPager2.currentItem, false)
                            var transform = result.split("_")[1].split(",")[0]
                            transform =
                                transform.substring(0, 1).uppercase() + transform.substring(1)
                                    .lowercase()
                            adapter.setMeanAndPhoneticFront(
                                binding.viewPager2.currentItem, transform, result.split("_")[2]
                            )
                        } catch (e: Exception) {
                            AwesomeDialogObject.showDialog(
                                activity = this@MainActivity,
                                onTouchOutside = false,
                                title = "Error!",
                                body = "Try new word again \n" +
                                        " or internet is disable",
                                icon = R.drawable.ic_error,
                                textPositive = "OK",
                                callBackPositive = { },
                                textNegative = "",
                                callBackNegative = null
                            )

                        }

                    }

                }
            }
        }

    }

    private fun playAudio(audio: String?) {
            Log.d(TAG, "playAudio: $audio")
            if (audio != null && audio.isNotEmpty()) {
                if (mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.stop()
                    releaseAudio()
                }
                mediaPlayer!!.reset()
                mediaPlayer!!.setDataSource(audio)
                mediaPlayer!!.prepareAsync()
                mediaPlayer!!.setOnPreparedListener {
                    mediaPlayer!!.start()
                }
                mediaPlayer!!.setOnCompletionListener {
                    Log.d(TAG, "playAudio: Complete $mediaPlayer")
                 
                }
        }
    }

    private fun releaseAudio() {
        if (mediaPlayer == null) return
        mediaPlayer!!.release()
    }

    override fun onDestroy() {
        releaseAudio()
        super.onDestroy()
    }

    private fun initViewpager() {
        binding.viewPager2.isUserInputEnabled = false
        adapter = SliderLearnSoundAdapter(this, listener = listenerOnClick)
        binding.viewPager2.adapter = adapter
        binding.viewPager2.clipToPadding = false
        binding.viewPager2.clipChildren = false
        binding.viewPager2.offscreenPageLimit = 3
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = currentItemHorizontalMarginPx + nextItemVisiblePx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (0.25f * kotlin.math.abs(position))

        }
        binding.viewPager2.setPageTransformer(pageTransformer)
        learningSoundViewModel.setCurrentCard(0)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) fullScreen()
        super.onWindowFocusChanged(hasFocus)
    }

    private fun fullScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowController = window.insetsController
            windowController?.hide(WindowInsets.Type.statusBars())
            windowController?.hide(WindowInsets.Type.navigationBars())
            windowController?.hide(WindowInsets.Type.captionBar())
            windowController?.hide(WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_SWIPE)
        } else {
            window.decorView.apply {
                systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            }
        }

    }

    private fun controlSwipeCard() {
        if (adapter != null) {
            val currentItem = binding.viewPager2.currentItem
            learningSoundViewModel.setCurrentCard(currentItem)
            val itemCount = adapter.itemCount - 1
            binding.cvContinue.isEnabled = currentItem != itemCount
            binding.cvPrev.isEnabled = binding.viewPager2.currentItem != 0
            binding.cvPrev.visibility = if (binding.cvPrev.isEnabled) View.VISIBLE else View.GONE
            binding.cvContinue.visibility =
                if (binding.cvContinue.isEnabled) View.VISIBLE else View.GONE
            binding.cvPrev.setOnClickListener {
                binding.viewPager2.currentItem = currentItem - 1
                adapter.disableExcept(binding.viewPager2.currentItem)
                controlSwipeCard()

            }
            binding.cvContinue.setOnClickListener {
                binding.viewPager2.currentItem = currentItem + 1
                adapter.disableExcept(binding.viewPager2.currentItem)
                controlSwipeCard()
            }
        }
    }

    private fun subcribeViewModel() {
        learningSoundViewModel.currentCard.observe(this, {
            if (it != null) {
                binding.numberCard.text = "${it + 1}/${adapter.itemCount}"
            }
        })
        learningSoundViewModel.resultResponse.observe(this, {
            if (it != null) {
                try {
                    if (it[1] != null && it[1] is List<Any?>) {
                        val temp = it[1] as List<Any?>
                        if (temp[0] != null && temp[0] is List<Any?>) {
                            val temp = temp[0] as List<Any?>
                            if (temp[1] != null && temp[1] is List<*>) {
                                val data = temp[1] as List<String>
                                adapter.setRecycleViewForCard(binding.viewPager2.currentItem, data)
                            }
                        }
                    }
                } catch (e: Exception) {
                    adapter.setRecycleViewForCard(binding.viewPager2.currentItem, emptyList())
                }
            } else {
                AwesomeDialogObject.showDialog(
                    activity = this@MainActivity,
                    onTouchOutside = false,
                    title = "Error!",
                    body = "Trying connect internet...",
                    icon = R.drawable.ic_error,
                    textPositive = "OK",
                    callBackPositive = { },
                    textNegative = "",
                    callBackNegative = null
                )
            }

        })
    }

    override fun onBrushSizeChange(size: Float) {
        Log.d(TAG, "onBrushSizeChange: $size")
    }

    override fun onDrawFinger(listFingerPath: List<FingerPath>) {
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display?.getRealMetrics(displayMetrics)
        } else windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        val writingGuide = WritingGuide(displayMetrics.heightPixels, displayMetrics.widthPixels)
        val postBodyFinger = PostBodyFinger(
            "537.36",
            0.4,
            "5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36",
            0,
            "enable_pre_space",
            listOf(getRequest(listFingerPath, writingGuide))
        )
        learningSoundViewModel.postBody(postBodyFinger)
    }

    private fun getRequest(listFingerPath: List<FingerPath>, writingGuide: WritingGuide): Request {
        var inks = mutableListOf<List<List<Int>>>()
        listFingerPath.forEach {
            inks.add(listOf(it.getXPoints(), it.getYPoints(), it.time))
        }
        return Request(
            inks.toList(),
            0,
            10,
            "",
            writingGuide
        )
    }

    private fun registerInternet() {
        val connectManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                if (!didRetriever) getDataCorrect()
                runOnUiThread {
                    binding.placeHolderInternet.visibility = View.GONE
                    binding.mainLayout.visibility = View.VISIBLE
                }
                Log.d(TAG, "onAvailable: onAvailable")
                super.onAvailable(network)
            }

            override fun onUnavailable() {
                Toast.makeText(
                    applicationContext,
                    "Device not support internet",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(TAG, "onUnavailable: onUnavailable")
                super.onUnavailable()
            }

            override fun onLost(network: Network) {
                runOnUiThread {
                    binding.placeHolderInternet.visibility = View.VISIBLE
                    binding.mainLayout.visibility = View.GONE
                }
                Log.d(TAG, "onLost: onLost")
                super.onLost(network)
            }
        })
    }

}