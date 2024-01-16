package com.example.musicapp

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.ScaleAnimation
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chibde.visualizer.SquareBarVisualizer
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var prevBtn: ClickableImageButton
    private lateinit var nextBtn: ClickableImageButton
    private lateinit var playBtn: ClickableImageButton
    private lateinit var seekBar: SeekBar
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var songTime: TextView
    private lateinit var songTitle: TextView
    private lateinit var squareVisualizer: SquareBarVisualizer

    private var isChecked: Boolean = false
    private var songPath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        setupListeners()
    }

    private fun initView() {
        playBtn = findViewById(R.id.play_btn)
        nextBtn = findViewById(R.id.next_btn)
        prevBtn = findViewById(R.id.prev_btn)
        seekBar = findViewById(R.id.seekBar)
        squareVisualizer = findViewById(R.id.visualizer)
        songTime = findViewById(R.id.song_time)
        songTitle = findViewById(R.id.song_title)

        mediaPlayer = initializeMediaPlayer()
        setupSeekBar()
        setupCompletionListener()
        convertDuration()
    }

    private fun initializeMediaPlayer(): MediaPlayer {
        val player = MediaPlayer.create(this, R.raw.audio1)

        try {
            player.apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(songPath)

                prepare()
                squareVisualizer.setColor(getColor(R.color.wave_color))
                squareVisualizer.setGap(2)
                squareVisualizer.setDensity(70f)
                squareVisualizer.setPlayer(mediaPlayer.audioSessionId)
            }
        } catch (e: IOException) {
            Log.d("Exception", "File not found")
            e.printStackTrace()
        }
        return player
    }

    private fun setupSeekBar() {
        seekBar.max = mediaPlayer.duration
        mediaPlayer.setOnPreparedListener {
            seekBar.max = mediaPlayer.duration
            updateSeekBar()
        }
    }

    private fun convertDuration() {
        val remainingTime = mediaPlayer.duration - mediaPlayer.currentPosition
        val mint = (remainingTime / 1000) / 60
        val sec = (remainingTime / 1000) % 60

        runOnUiThread {
            songTime.text = String.format("%02d:%02d", mint, sec)
        }

        songTitle.text = String.format("Audio1")
    }

    private fun updateSeekBar() {
        val handler = Handler()
        handler.postDelayed({
            seekBar.progress = mediaPlayer.currentPosition
            updateSeekBar()
            convertDuration()
        }, 1000)
    }

    private fun setupCompletionListener() {
        mediaPlayer.setOnCompletionListener {
            isChecked = false
            updateButtonDrawable(isChecked)
        }
    }

    private fun setupListeners() {
        playButtonAction(playBtn)
        playButtonAction(nextBtn)
        playButtonAction(prevBtn)
        playBtn.setOnClickListener {
            isChecked = !isChecked
            updateButtonDrawable(isChecked)
        }

        nextBtn.setOnClickListener {
            Toast.makeText(this, "Next Music", Toast.LENGTH_SHORT).show()
        }

        prevBtn.setOnClickListener {
            Toast.makeText(this, "Previous Music", Toast.LENGTH_SHORT).show()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun playButtonAction(btn: ClickableImageButton) {
        btn.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> scaleButton(btn, 0.7f)
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    scaleButton(btn, 1.0f)
                    btn.performClick()
                }
            }
            true
        }
    }

    private fun scaleButton(view: View, scaleFactor: Float) {
        val scaleAnimation = ScaleAnimation(
            1f, scaleFactor,
            1f, scaleFactor,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        )
        scaleAnimation.duration = 150
        scaleAnimation.fillAfter = true
        view.startAnimation(scaleAnimation)
    }

    private fun updateButtonDrawable(isChecked: Boolean) {
        try {
            if (isChecked) {
                playBtn.setImageResource(R.drawable.pause)
                mediaPlayer.start()
            } else {
                playBtn.setImageResource(R.drawable.play)
                mediaPlayer.pause()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

class ClickableImageButton(context: Context, attrs: AttributeSet) :
    androidx.appcompat.widget.AppCompatImageButton(context, attrs) {

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
