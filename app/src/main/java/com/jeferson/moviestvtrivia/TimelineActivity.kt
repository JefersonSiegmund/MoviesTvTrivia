package com.jeferson.moviestvtrivia

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.jeferson.moviestvtrivia.databinding.ActivityTimelineBinding
import java.util.concurrent.TimeUnit


class TimelineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimelineBinding
    private var triviaAdapter : TriviaAdapter? = null
    private var triviaList: ArrayList<TriviaModel>? = null
    private var timelineTimer: CountDownTimer? = null
    private var timelineDuration: Long? = null
    private var timelineTick: Long? = null
    private var timelineCurrentTick: Long? = null
    private var triviaPosition: Int = 0
    private var pauseTimeline = false
    private var marginFactor: Long? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimelineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        triviaList = Constants.defaultTriviaList()
        timelineDuration = 13000.toLong()
        timelineTick = 100.toLong()
        timelineCurrentTick = 0.toLong()
        marginFactor = 150.toLong()/timelineDuration!!

        setupTriviaRecyclerView()

        binding.ivPausePlayButton.setOnClickListener {
            pausePlayTimelineTimer()
        }

        // Set a SeekBar change listener
/*        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                //text_view.text = "Progress : $i"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
                Toast.makeText(applicationContext,"start tracking",Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
                Toast.makeText(applicationContext,"stop tracking",Toast.LENGTH_SHORT).show()
            }
        })*/

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
                        
                        val layoutParams = binding.flRvHolder.layoutParams as LinearLayout.LayoutParams
                        //layoutParams.setMargins(20, ((timelineCurrentTick!! * marginFactor!!)+50.toLong()).toInt(), 0, 0)
                        layoutParams.setMargins(20, 100, 0, 0)
                        binding.flRvHolder.layoutParams = layoutParams

                        if(binding.flRvHolder.alpha == 0f) {
                            binding.flRvHolder.animate().alpha(1f).setDuration(1000).setInterpolator(AccelerateInterpolator()).start()
                        }
                    }

                    // 500 ms antes do proximo tick
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

}