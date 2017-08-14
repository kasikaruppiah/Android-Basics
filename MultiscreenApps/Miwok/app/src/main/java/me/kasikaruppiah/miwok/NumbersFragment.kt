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
class NumbersFragment : Fragment() {

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

        val words = arrayListOf<Word>()
        words.add(Word("one", "lutti", R.drawable.number_one, R.raw.number_one))
        words.add(Word("two", "otiiko", R.drawable.number_two, R.raw.number_two))
        words.add(Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three))
        words.add(Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four))
        words.add(Word("five", "massokka", R.drawable.number_five, R.raw.number_five))
        words.add(Word("six", "temmokka", R.drawable.number_six, R.raw.number_six))
        words.add(Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven))
        words.add(Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight))
        words.add(Word("nine", "wo’e", R.drawable.number_nine, R.raw.number_nine))
        words.add(Word("ten", "na’aacha", R.drawable.number_ten, R.raw.number_ten))
        val itemsAdapter = WordAdapter(activity, words, R.color.category_numbers)
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
