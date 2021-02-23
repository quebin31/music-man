package com.example.musicman.ui.player

import android.content.ComponentName
import android.media.AudioManager
import android.os.Bundle
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
import com.example.musicman.model.Song
import com.example.musicman.player.MusicPlayerClient
import com.example.musicman.player.MusicPlayerService
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerFragment : Fragment(R.layout.fragment_player) {

    private val playerViewModel by viewModels<PlayerViewModel>()
    private val binding by viewBinding(FragmentPlayerBinding::bind)
    private val arguments by navArgs<PlayerFragmentArgs>()

    private val playerClient by lazy {
        val context = requireContext()
        val serviceName = ComponentName(context, MusicPlayerService::class.java)
        MusicPlayerClient(context, serviceName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        playerClient.isConnected.observe(viewLifecycleOwner) { connected ->
            when (connected) {
                true -> setupOnConnection()
                else -> showNothingIsPlaying()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        playerClient.connect()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().volumeControlStream = AudioManager.STREAM_MUSIC
    }

    override fun onStop() {
        super.onStop()
        playerClient.disconnect()
    }

    private fun setupOnConnection() {
        playerClient.playbackState.observe(viewLifecycleOwner) {
            val state = it?.state ?: PlaybackStateCompat.STATE_NONE
            togglePlayPauseButton(state)
        }

        playerViewModel.latestPlayedSong.observe(viewLifecycleOwner) { songId ->
            if (songId == null) {
                showNothingIsPlaying()
                return@observe
            }

            playerViewModel.getSong(songId)?.let { song ->
                showSongInformation(song)

                binding.playButton.setOnClickListener {
                    when (playerClient.playbackState.value?.state) {
                        PlaybackStateCompat.STATE_NONE -> maybePlaySong(song)
                        PlaybackStateCompat.STATE_PLAYING -> playerClient.transportControls.pause()
                        PlaybackStateCompat.STATE_PAUSED -> playerClient.transportControls.play()
                        PlaybackStateCompat.STATE_STOPPED -> {
                            playerClient.transportControls.seekTo(0L)
                            playerClient.transportControls.play()
                        }

                        else -> Log.w(TAG, "setupTransportControls2: Nothing to do")
                    }
                }
            }
        }

        arguments.songId?.let {
            playerViewModel.getSong(it)?.let { song ->
                playerViewModel.setLatestPlayedSong(song.id)
                maybePlaySong(song)
            }
        }
    }

    private fun maybePlaySong(song: Song) {
        val currentId = playerClient.metadata?.id
        if (song.id != currentId) {
            val uri = song.uri
            val bundle =
                bundleOf(MusicPlayerService.KEY_METADATA to song.asMediaMetadata(requireContext()))

            playerClient.transportControls.prepareFromUri(uri, bundle)
            playerClient.transportControls.play()
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
