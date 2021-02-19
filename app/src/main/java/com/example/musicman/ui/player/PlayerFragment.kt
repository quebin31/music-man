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
import com.example.musicman.extensions.id
import com.example.musicman.extensions.showToast
import com.example.musicman.room.Song
import com.example.musicman.player.MusicPlayerService
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerFragment : Fragment(R.layout.fragment_player) {

    private lateinit var mediaBrowser: MediaBrowserCompat
    private val playerViewModel by viewModels<PlayerViewModel>()
    private val binding by viewBinding(FragmentPlayerBinding::bind)
    private val arguments by navArgs<PlayerFragmentArgs>()
    private val mediaController by lazy {
        MediaControllerCompat.getMediaController(requireActivity())
    }

    private val connectionCallback = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mediaBrowser.sessionToken.also { sessionToken ->
                val mediaController = MediaControllerCompat(requireContext(), sessionToken)
                MediaControllerCompat.setMediaController(requireActivity(), mediaController)
                setupTransportControls()
            }
        }

        override fun onConnectionFailed() {
            requireContext().showToast("Connection to music service failed")
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

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            metadata?.let {
                showSongInformation(Song.fromMediaMetadata(it))
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
        togglePlayPauseButton(mediaController.playbackState.state)

        // Change latest played song if received by args and (maybe) start playing it
        arguments.songId?.let {
            playerViewModel.getSong(it)?.let { song ->
                playerViewModel.setLatestPlayedSong(song.id)
                maybePlaySong(song)
            }
        }

        playerViewModel.latestPlayedSong.observe(viewLifecycleOwner) { songId ->
            if (songId == null) {
                showNothingIsPlaying()
                return@observe
            }

            playerViewModel.getSong(songId)?.let { song ->
                showSongInformation(song)

                binding.playButton.setOnClickListener {
                    when (mediaController.playbackState.state) {
                        PlaybackStateCompat.STATE_NONE -> maybePlaySong(song)
                        PlaybackStateCompat.STATE_PLAYING -> mediaController.transportControls.pause()
                        PlaybackStateCompat.STATE_PAUSED -> mediaController.transportControls.play()
                        PlaybackStateCompat.STATE_STOPPED -> {
                            mediaController.transportControls.seekTo(0L)
                            mediaController.transportControls.play()
                        }
                        else -> Log.w(TAG, "setupTransportControls: Nothing to do")
                    }
                }
            }
        }
    }

    private fun maybePlaySong(song: Song) {
        if (song.id != mediaController.metadata?.id) {
            val uri = song.uri
            val bundle =
                bundleOf(MusicPlayerService.KEY_METADATA to song.asMediaMetadata(requireContext()))

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

        val unknown = requireContext().getString(R.string.unknown_string)

        song.albumArt?.let { binding.songAlbumArtwork.setImageBitmap(it) }
        binding.songTitle.text = song.title ?: unknown
        binding.songArtist.text = song.effectiveArtist ?: unknown
        binding.songAlbum.text = song.album ?: unknown

        val unknownNumber = requireContext().getString(R.string.unknown_number)
        val track = song.track?.toString() ?: unknownNumber
        val disc = song.disc?.toString() ?: unknownNumber
        binding.songTrackDisk.text = requireContext().getString(R.string.track_disc, track, disc)
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
