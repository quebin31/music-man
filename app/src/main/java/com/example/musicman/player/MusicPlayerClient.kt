package com.example.musicman.player

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.MutableLiveData

class MusicPlayerClient(context: Context, serviceComponent: ComponentName) {
    val isConnected = MutableLiveData<Boolean>().apply {
        postValue(false)
    }

    val playbackState = MutableLiveData<PlaybackStateCompat?>().apply {
        postValue(null)
    }

    val nowPlaying = MutableLiveData<MediaMetadataCompat?>().apply {
        postValue(null)
    }

    val metadata: MediaMetadataCompat?
        get() = mediaController.metadata

    private val controllerCallback = ControllerCallback()
    private lateinit var mediaController: MediaControllerCompat
    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    private val connectionCallback = ConnectionCallback(context)
    private val mediaBrowser =
        MediaBrowserCompat(context, serviceComponent, connectionCallback, null)

    private inner class ConnectionCallback(private val context: Context) :
        MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                registerCallback(controllerCallback)
            }

            // Let them now the connection state and playback state
            isConnected.postValue(true)
            playbackState.postValue(mediaController.playbackState)
            nowPlaying.postValue(mediaController.metadata)
        }

        override fun onConnectionSuspended() {
            mediaController.unregisterCallback(controllerCallback)
            isConnected.postValue(false)
        }

        override fun onConnectionFailed() {
            isConnected.postValue(false)
        }
    }

    private inner class ControllerCallback : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            playbackState.postValue(state)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            nowPlaying.postValue(metadata)
        }

        override fun onSessionDestroyed() {
            connectionCallback.onConnectionSuspended()
        }
    }

    fun connect() {
        mediaBrowser.connect()
    }

    fun disconnect() {
        mediaBrowser.disconnect()
    }
}