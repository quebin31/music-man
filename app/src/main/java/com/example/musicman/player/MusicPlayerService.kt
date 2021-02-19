package com.example.musicman.player

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import java.lang.IllegalStateException

class MusicPlayerService : MediaBrowserServiceCompat() {

    private var foreground = false
    private var mediaSession: MediaSessionCompat? = null
    private var mediaPlayer = MediaPlayer()

    override fun onCreate() {
        super.onCreate()

        mediaSession = MediaSessionCompat(applicationContext, TAG).apply {
            isActive = true
            val playbackState = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_PAUSE)
                .build()

            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
            setPlaybackState(playbackState)
            setSessionToken(sessionToken)
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPrepareFromUri(uri: Uri?, extras: Bundle?) {
                    uri?.let {
                        mediaPlayer.reset()
                        mediaPlayer.setDataSource(applicationContext, it)
                        mediaPlayer.prepare()
                        val metadata: MediaMetadataCompat? = extras?.getParcelable(KEY_METADATA)
                        metadata?.let {
                            setMetadata(metadata)
                        }
                    }
                }

                override fun onPlay() {
                    try {
                        if (mediaPlayer.isPlaying) return
                        mediaPlayer.start()

                        val position = mediaPlayer.currentPosition
                        updatePlaybackState(PlaybackStateCompat.STATE_PLAYING, position.toLong())
                        updateNotification(showPause = true)
                    } catch (e: IllegalStateException) {
                        Log.w(TAG, "onPlay: media player is on invalid state")
                    }
                }

                override fun onPause() {
                    try {
                        if (!mediaPlayer.isPlaying) return
                        mediaPlayer.pause()

                        val position = mediaPlayer.currentPosition
                        updatePlaybackState(PlaybackStateCompat.STATE_PAUSED, position.toLong())
                        updateNotification(showPause = false)
                    } catch (e: IllegalStateException) {
                        Log.w(TAG, "onPause: media player is on invalid state")
                    }
                }

                override fun onStop() {
                    try {
                        mediaPlayer.stop()

                        updatePlaybackState(PlaybackStateCompat.STATE_STOPPED, 0L)
                        stopForeground()
                        stopSelf()
                    } catch (e: IllegalStateException) {
                        Log.w(TAG, "onStop: media player is on invalid state")
                    }
                }

                override fun onSeekTo(pos: Long) {
                    try {
                        mediaPlayer.seekTo(pos.toInt())
                    } catch (e: IllegalStateException) {
                        Log.w(TAG, "onSeekTo: media player is on invalid state")
                    }
                }
            })
        }

        mediaPlayer.setOnCompletionListener {
            updatePlaybackState(PlaybackStateCompat.STATE_STOPPED, 0L)
            updateNotification(showPause = false)
        }

        mediaPlayer.setOnErrorListener { mp, _, _ ->
            mp.reset()
            updatePlaybackState(PlaybackStateCompat.STATE_STOPPED, 0L)
            updateNotification(showPause = false)
            true
        }
    }

    private fun updatePlaybackState(state: Int, position: Long) {
        val newState = PlaybackStateCompat.Builder().run {
            when (state) {
                PlaybackStateCompat.STATE_PLAYING -> {
                    setActions(PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_STOP)
                }

                PlaybackStateCompat.STATE_PAUSED -> {
                    setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_STOP)
                }

                PlaybackStateCompat.STATE_STOPPED -> {
                    setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_PAUSE)
                }
            }

            setState(state, position, 1f)
            build()
        }

        mediaSession!!.setPlaybackState(newState)
    }

    private fun updateNotification(showPause: Boolean) {
        val notification =
            MediaNotificationBuilder(applicationContext, mediaSession!!).run {
                build(NOTIFICATION_CHANNEL, showPause)
            }

        updateForeground(notification)
    }

    private fun updateForeground(notification: Notification) {
        if (!foreground) {
            startForeground(NOTIFICATION_ID, notification)
            foreground = true
        } else {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun stopForeground() {
        stopForeground(true)
        foreground = false
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot = BrowserRoot(EMPTY_ROOT_ID, null)

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        result.sendResult(mutableListOf())
    }

    companion object {
        private const val TAG = "MusicPlayerService"
        const val EMPTY_ROOT_ID = "@empty@"
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_CHANNEL = "com.example.musicman.playerservice"
        const val KEY_METADATA = "com.example.musicman.metadata"
    }
}