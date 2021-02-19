package com.example.musicman.ui.player

import android.content.ComponentName
import android.media.AudioManager
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.musicman.R
import com.example.musicman.databinding.FragmentPlayerBinding
import com.example.musicman.extensions.showShortToast
import com.example.musicman.model.Song
import com.example.musicman.player.MusicPlayerService
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerFragment : Fragment(R.layout.fragment_player) {

    private lateinit var mediaBrowser: MediaBrowserCompat
    private val playerViewModel by viewModels<PlayerViewModel>()
    private val binding by viewBinding(FragmentPlayerBinding::bind)
    private val arguments by navArgs<PlayerFragmentArgs>()
    private val mediaController
        get() = MediaControllerCompat.getMediaController(requireActivity())

    private val connectionCallback = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mediaBrowser.sessionToken.also { sessionToken ->
                val mediaController = MediaControllerCompat(requireContext(), sessionToken)
                MediaControllerCompat.setMediaController(requireActivity(), mediaController)
                setupTransportControls()
            }
        }

        override fun onConnectionFailed() {
            "Connection to service failed".showShortToast(requireContext())
            showNothingIsPlaying()
        }
    }

    private val mediaCallback = object : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            state?.state?.let {
                togglePlayPauseButton(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupMediaBrowser()
    }

    override fun onStart() {
        super.onStart()
        mediaBrowser.connect()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().volumeControlStream = AudioManager.STREAM_MUSIC
    }

    override fun onStop() {
        super.onStop()
        mediaController.unregisterCallback(mediaCallback)
        mediaBrowser.disconnect()
    }

    private fun setupMediaBrowser() {
        mediaBrowser = MediaBrowserCompat(
            requireActivity(),
            ComponentName(requireContext(), MusicPlayerService::class.java),
            connectionCallback,
            null,
        )
    }

    private fun setupTransportControls() {
        mediaController.registerCallback(mediaCallback)
        binding.playButton.setOnClickListener {
            when (mediaController.playbackState.state) {
                PlaybackStateCompat.STATE_PLAYING -> mediaController.transportControls.pause()
                PlaybackStateCompat.STATE_PAUSED -> mediaController.transportControls.play()
                PlaybackStateCompat.STATE_STOPPED -> {
                    mediaController.transportControls.seekTo(0L)
                    mediaController.transportControls.play()
                }
                else -> Log.w(TAG, "setupTransportControls: Cannot play")
            }
        }

        val state = mediaController.playbackState.state
        togglePlayPauseButton(state)

        // Change latest played song if received by args
        arguments.songId?.let {
            playerViewModel.getSong(it)?.let { song ->
                playerViewModel.setLatestPlayedSong(song)
            }
        }

        // Update ui whenever latest played song changes and maybe start playing it
        playerViewModel.latestPlayedSong.observe(viewLifecycleOwner) { song ->
            if (song == null) {
                showNothingIsPlaying()
            } else {
                showSongInformation(song)
                maybeStartPlayingSong(song)
            }
        }
    }

    private fun maybeStartPlayingSong(song: Song) {
        val state = mediaController.playbackState.state
        val currentSongId = mediaController.metadata?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)

        // Nothing is being played, or this song is different from the latest one
        if (state == PlaybackStateCompat.STATE_NONE || state == PlaybackStateCompat.STATE_STOPPED || song.id != currentSongId) {
            val uri = song.uri
            val bundle =
                bundleOf(
                    MusicPlayerService.KEY_METADATA to song.asMediaMetadata(requireContext()),
                )

            mediaController.transportControls.prepareFromUri(uri, bundle)
            mediaController.transportControls.play()
        }
    }

    private fun showNothingIsPlaying() {
        binding.nothingPlaying.visibility = View.VISIBLE
        binding.playerGroup.visibility = View.GONE
    }

    private fun showSongInformation(song: Song) {
        binding.nothingPlaying.visibility = View.GONE
        binding.playerGroup.visibility = View.VISIBLE

        val unknownString = requireContext().getString(R.string.unknown_string)

        song.artworkBitmap?.let { binding.songAlbumArtwork.setImageBitmap(it) }
        binding.songTitle.text = song.title ?: unknownString
        binding.songArtist.text = song.effectiveArtist ?: unknownString
        binding.songAlbum.text = song.album ?: unknownString

        val unknownNumber = requireContext().getString(R.string.unknown_number)
        val track = song.track?.toString() ?: unknownNumber
        val disk = song.disk?.toString() ?: unknownNumber
        binding.songTrackDisk.text = requireContext().getString(R.string.track_disk, track, disk)
    }

    private fun togglePlayPauseButton(state: Int) {
        if (state == PlaybackStateCompat.STATE_PLAYING) {
            binding.playButton.setImageResource(R.drawable.ic_baseline_pause_24)
        } else {
            binding.playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }

    companion object {
        private const val TAG = "PlayerFragment"
    }
}
