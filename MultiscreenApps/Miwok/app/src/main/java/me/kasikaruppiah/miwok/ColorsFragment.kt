package me.kasikaruppiah.miwok


import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.word_list.view.*


/**
 * A simple [Fragment] subclass.
 */
class ColorsFragment : Fragment() {
    var mediaPlayer: MediaPlayer? = null

    lateinit var mAudioManager: AudioManager

    var afChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            releaseMediaPlayer()
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            mediaPlayer!!.pause()
            mediaPlayer!!.seekTo(0)
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            mediaPlayer!!.start()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.word_list, container, false);

        var words = ArrayList<Word>()
        words.add(Word("red", "weṭeṭṭi", R.drawable.color_red, R.raw.color_red))
        words.add(Word("green", "chokokki", R.drawable.color_green, R.raw.color_green))
        words.add(Word("brown", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown))
        words.add(Word("gray", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray))
        words.add(Word("black", "kululli", R.drawable.color_black, R.raw.color_black))
        words.add(Word("white", "kelelli", R.drawable.color_white, R.raw.color_white))
        words.add(Word("dusty yellow", "ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow))
        words.add(Word("mustard yellow", "chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow))
        val itemsAdapter = WordAdapter(activity, words, R.color.category_colors)
        rootView.list.adapter = itemsAdapter

        rootView.list.setOnItemClickListener { adapterView, view, i, l ->
            run {
                releaseMediaPlayer()
                mAudioManager = activity.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val result = mAudioManager.requestAudioFocus(afChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(activity, words.get(i).audioResourceId)
                    mediaPlayer!!.start()
                    mediaPlayer!!.setOnCompletionListener { p0 -> releaseMediaPlayer() }
                }
            }
        }

        return rootView
    }

    fun releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
            mAudioManager.abandonAudioFocus(afChangeListener)
        }
    }

    override fun onStop() {
        super.onStop()
        releaseMediaPlayer()
    }
}// Required empty public constructor
