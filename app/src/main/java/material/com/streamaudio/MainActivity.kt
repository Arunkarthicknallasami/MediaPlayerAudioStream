package material.com.streamaudio

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mediaPlayer = MediaPlayer()
        var attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        mediaPlayer?.setAudioAttributes(attributes)
        mediaPlayer?.setDataSource("http://www.largesound.com/ashborytour/sound/brobob.mp3")
        mediaPlayer?.setOnBufferingUpdateListener(this)
        mediaPlayer?.isLooping = true
        mediaPlayer?.setOnCompletionListener { this }
        fab.setOnClickListener { view ->
            mediaPlayer?.apply {
                if (!this.isPlaying) {
                    mediaPlayer?.prepareAsync()
                    mediaPlayer?.setOnPreparedListener { mp ->
                        if (!isPaused) {
                            mp.start()
                        }
                    }

                }

            }
        }
    }

    private var isPaused: Boolean = false

    override fun onPause() {
        super.onPause()
        mediaPlayer?.apply {
            if (isPlaying) {
                txtVw_status.text = "Paused"
                isPaused = true
                pause()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer?.apply {
            if (isPaused) {
                isPaused = false
                start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.apply {
            release()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        if (mp?.isPlaying == false) {
            txtVw_status.text = "Completed"
        }
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        if (mp?.isPlaying == true) {
            txtVw_status.text = "Playing"
        } else {
            txtVw_status.text = "Buffering"
        }
    }
}
