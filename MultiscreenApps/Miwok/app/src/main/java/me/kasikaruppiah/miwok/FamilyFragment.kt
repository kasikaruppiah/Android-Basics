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
class FamilyFragment : Fragment() {
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
        words.add(Word("father", "әpә", R.drawable.family_father, R.raw.family_father))
        words.add(Word("mother", "әṭa", R.drawable.family_mother, R.raw.family_mother))
        words.add(Word("son", "angsi", R.drawable.family_son, R.raw.family_son))
        words.add(Word("daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter))
        words.add(Word("older brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother))
        words.add(Word("younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother))
        words.add(Word("older sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister))
        words.add(Word("younger sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister))
        words.add(Word("grandmother", "ama", R.drawable.family_grandmother, R.raw.family_grandmother))
        words.add(Word("grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather))
        val itemsAdapter = WordAdapter(activity, words, R.color.category_family)
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
