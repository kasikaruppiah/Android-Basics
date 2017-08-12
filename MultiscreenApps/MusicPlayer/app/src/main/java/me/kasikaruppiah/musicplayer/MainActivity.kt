package me.kasikaruppiah.musicplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mediaPlayer = MediaPlayer.create(this, R.raw.sorry)
        play_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                mediaPlayer.setOnCompletionListener { object : MediaPlayer.OnCompletionListener {
                    override fun onCompletion(p0: MediaPlayer?) {
                        Toast.makeText(this@MainActivity, "I'm Done!", Toast.LENGTH_SHORT).show()
                    }

                }}
                mediaPlayer.start()
            }
        })
        pause_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                mediaPlayer.pause()
            }
        })
    }
}
