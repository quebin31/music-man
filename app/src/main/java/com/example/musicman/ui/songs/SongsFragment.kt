package com.example.musicman.ui.songs


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicman.R
import com.example.musicman.databinding.FragmentSongsBinding
import com.example.musicman.ui.decorations.SimpleItemDecoration
import com.example.musicman.room.Song
import com.example.musicman.ui.adapter.SongAdapter
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SongsFragment : Fragment(R.layout.fragment_songs) {

    private val songsViewModel by viewModels<SongsViewModel>()
    private val binding by viewBinding(FragmentSongsBinding::bind)
    private val adapter by lazy {
        SongAdapter(::onSongItemClick)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.songsList.layoutManager = LinearLayoutManager(requireActivity())
        binding.songsList.adapter = adapter
        binding.songsList.addItemDecoration(SimpleItemDecoration(resources.getDimensionPixelSize(R.dimen.padding_half)))
        songsViewModel.songs.observe(viewLifecycleOwner) {
            adapter.updateSongs(it)
        }
    }

    private fun onSongItemClick(song: Song) {
        val action = SongsFragmentDirections.playSong(song.id)
        findNavController().navigate(action)
    }
}