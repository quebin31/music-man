package com.example.musicman.ui.songs


import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicman.R
import com.example.musicman.databinding.FragmentSongsBinding
import com.example.musicman.decorations.SimpleItemDecoration
import com.example.musicman.model.Song
import com.example.musicman.repository.RawSongsRepository
import com.example.musicman.ui.adapter.SongAdapter
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class SongsFragment : Fragment(R.layout.fragment_songs) {

    private val songsViewModel by viewModels<SongsViewModel>()
    private val binding by viewBinding(FragmentSongsBinding::bind)
    private val adapter by lazy {
        SongAdapter(requireContext(), ::onSongItemClick)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songsViewModel.repository = RawSongsRepository(requireContext())

        binding.songsList.layoutManager = LinearLayoutManager(requireActivity())
        binding.songsList.adapter = adapter
        binding.songsList.addItemDecoration(SimpleItemDecoration(resources.getDimensionPixelSize(R.dimen.padding_half)))
        adapter.updateSongs(songsViewModel.songs)
    }

    private fun onSongItemClick(song: Song) {
        val bundle = bundleOf("song_id" to song.id)
        findNavController().navigate(R.id.navigate_to_player, bundle)
    }
}