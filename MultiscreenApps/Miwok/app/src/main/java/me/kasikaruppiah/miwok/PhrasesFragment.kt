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
class PhrasesFragment : Fragment() {
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

        val words = ArrayList<Word>()
        words.add(Word("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going))
        words.add(Word("What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name))
        words.add(Word("My name is...", "oyaaset...", R.raw.phrase_my_name_is))
        words.add(Word("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling))
        words.add(Word("I’m feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good))
        words.add(Word("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming))
        words.add(Word("Yes, I’m coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming))
        words.add(Word("I’m coming.", "әәnәm", R.raw.phrase_im_coming))
        words.add(Word("Let’s go.", "yoowutis", R.raw.phrase_lets_go))
        words.add(Word("Come here.", "әnni'nem", R.raw.phrase_come_here))
        val itemsAdapter = WordAdapter(activity, words, R.color.category_phrases)
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
