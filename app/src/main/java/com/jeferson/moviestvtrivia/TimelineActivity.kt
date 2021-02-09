package com.jeferson.moviestvtrivia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeferson.moviestvtrivia.databinding.ActivityTimelineBinding
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.nio.file.Paths
import java.util.concurrent.TimeUnit


class TimelineActivity : AppCompatActivity() {
    data class SubtitleModel(var start: String?, var end: String?, var text: String?)
    private var subtitlesList = ArrayList<SubtitleModel>()

    private lateinit var binding: ActivityTimelineBinding
    private var triviaAdapter : TriviaAdapter? = null
    private var triviaList: ArrayList<TriviaModel>? = null
    private var timelineTimer: CountDownTimer? = null
    private var timelineDuration: Long? = null
    private var timelineTick: Long? = null
    private var timelineCurrentTick: Long? = null
    private var triviaPosition: Int = 0
    private var pauseTimeline = false
    private var marginFactor: Float? = null
    private var dpRatio: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimelineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        triviaList = Constants.defaultTriviaList()
        timelineDuration = 13000.toLong()
        timelineTick = 100.toLong()
        timelineCurrentTick = 0.toLong()
        // controla altura da view com base na progressão do tempo
        marginFactor = 150f/timelineDuration!!.toFloat()
        // fator de conversão DP do PX
        dpRatio = this@TimelineActivity.resources.displayMetrics.density

        setupSubtitles()

        setupTriviaRecyclerView()

        binding.ivPausePlayButton.setOnClickListener {
            pausePlayTimelineTimer()
        }
    }

    override fun onDestroy() {
        if(timelineTimer != null) {
            timelineTimer!!.cancel()
        }
        binding.flRvHolder.alpha = 0f
        super.onDestroy()
    }

    private fun setupTriviaRecyclerView(){

        binding.flRvHolder.alpha = 0f

        // DISABLE RECYCLERVIEW SCROLL VIA TOUCH CLICK
        binding.rvTriviaList.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return true
            }
        }) // END DISABLE

        binding.rvTriviaList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        triviaAdapter = TriviaAdapter(triviaList!!, this)
        binding.rvTriviaList.adapter = triviaAdapter
        binding.progressBar.max = timelineDuration!!.toInt()
        binding.tvTimerEnd.text = formatTimestamp(timelineDuration!!)
        // First call to initialize the timer
        setupTimelineTimer()
    } // END setupTriviaRecyclerView

    private  fun setupTimelineTimer() {
        timelineTimer = object : CountDownTimer(timelineDuration!!, timelineTick!!) {

            private var timeShowNextTrivia = triviaList!![triviaPosition].getTimeShow()
            private val layoutParams = binding.flRvHolder.layoutParams as LinearLayout.LayoutParams
            private var pixelForDp = (((timelineCurrentTick!!.toFloat() * marginFactor!!).toLong()+50.toLong()).toInt() * dpRatio!!).toInt()

            override fun onTick(millisUntilFinished: Long) {

                if (pauseTimeline) {
                    // To ensure start timer from paused time
                    timelineDuration = millisUntilFinished
                    cancel()
                } else {

                    timelineCurrentTick = timelineCurrentTick?.plus(timelineTick!!)
                    binding.progressBar.progress = timelineCurrentTick!!.toInt()
                    binding.tvTimerCurrent.text = formatTimestamp(timelineCurrentTick!!)

                    // TICK de novo item
                    if (timelineCurrentTick == timeShowNextTrivia) {
                        binding.rvTriviaList.smoothScrollToPosition(triviaPosition)
                        triviaPosition++
                        if (triviaPosition < triviaList!!.size) {
                            timeShowNextTrivia = triviaList!![triviaPosition].getTimeShow()
                        }
                        
                        //val layoutParams = binding.flRvHolder.layoutParams as LinearLayout.LayoutParams
                        //var
                        pixelForDp = (((timelineCurrentTick!!.toFloat() * marginFactor!!).toLong()+50.toLong()).toInt() * dpRatio!!).toInt()
                        layoutParams.topMargin = pixelForDp
                        binding.flRvHolder.layoutParams = layoutParams

                        if(binding.flRvHolder.alpha == 0f) {
                            binding.flRvHolder.animate().alpha(1f).setDuration(1000).setInterpolator(AccelerateInterpolator()).start()
                        }
                        // TODO implementar leitura separada da trivia
                        binding.tvSubtitles.text = subtitlesList!![triviaPosition-1].text
                    }

                    // 500 ms antes do proximo tick esconde a trivia anterior
                    if (timelineCurrentTick == timeShowNextTrivia-(500.toLong())) {
                        if(binding.flRvHolder.alpha == 1f) {
                            binding.flRvHolder.animate().alpha(0f).setDuration(500).setInterpolator(AccelerateInterpolator()).start()

                        }
                    }




                }
            }

            override fun onFinish() {
                // TODO: retornar ao padrão
                //val intent = Intent(this@TimelineActivity, StartActivity::class.java)
                binding.flRvHolder.alpha = 0f
                val intent = Intent(this@TimelineActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.start()
    } // END setupTimelineTimer

    private fun pausePlayTimelineTimer() {
        if(timelineTimer != null) {
            pauseTimeline = !pauseTimeline
            if(!pauseTimeline) {
                setupTimelineTimer()
            }
        }
    }

    private fun formatTimestamp(mills: Long?):String {
        return String.format("%2d'%02d''%02d",
                TimeUnit.MILLISECONDS.toHours(mills!!),
                TimeUnit.MILLISECONDS.toMinutes(mills!!) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mills!!)),
                TimeUnit.MILLISECONDS.toSeconds(mills!!) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mills!!))
        )
    }

    private fun setupSubtitles() {
        val inputStream: InputStream = resources.openRawResource(resources.getIdentifier("bm","raw", packageName))
        val reader = BufferedReader(inputStream.reader())
        var start: String? = null
        var end: String? = null
        var text: String? = null
        var lineCount = 0

        reader.use { reader ->
            var line = reader.readLine()
            while (line != null) {
                lineCount++
                when(lineCount) {
                    1 -> null
                    2 -> {
                        start = line.substring(0,line.indexOf("-")-1)
                        end = line.substring(line.indexOf(">")+1,line.length)
                    }
                    3 -> {
                        text = line
                        val subtitle = SubtitleModel(start,end,text)
                        subtitlesList.add(subtitle)
                    }
                    else -> lineCount = 0
                }
                line = reader.readLine()
            }
        }

    }

}