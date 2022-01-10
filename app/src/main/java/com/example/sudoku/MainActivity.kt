package com.example.sudoku

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    var player: MediaPlayer? = null
    private var playerPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        player = MediaPlayer.create(this, R.raw.music)
    }

    override fun onStart() {
        super.onStart()
        player?.seekTo(playerPosition)
        player?.start()
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
        playerPosition = player?.currentPosition ?: 0
    }
}