package com.example.musicman.ui.player

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.musicman.R
import com.example.musicman.databinding.FragmentPlayerBinding
import com.example.musicman.model.Song
import com.example.musicman.repository.RawSongsRepository
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private val playerViewModel by viewModels<PlayerViewModel>()
    private val binding by viewBinding(FragmentPlayerBinding::bind)
    // TODO: Use media player directly or through media browser service

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        playerViewModel.repository = RawSongsRepository(requireContext())

        playerViewModel.currentSong.observe(viewLifecycleOwner) { song ->
            if (song == null) {
                showNothingPlaying()
            } else {
                showSongInfoPlaying(song)
                // TODO: There's a song playing, check the media player status
                // TODO: Update play/pause button
            }
        }
    }

    private fun showNothingPlaying() {
        binding.nothingPlaying.visibility = View.VISIBLE
        binding.playerGroup.visibility = View.GONE
    }

    private fun showSongInfoPlaying(song: Song) {
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

    override fun onResume() {
        super.onResume()

        arguments?.getString("song_id")?.let {
            val song = playerViewModel.repository.getSong(it)
            playerViewModel.setCurrentSong(song ?: return)
            // TODO: Change current data source from media player if song is different from current
        }
    }
}