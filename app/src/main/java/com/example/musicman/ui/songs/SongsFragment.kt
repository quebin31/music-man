package com.example.musicman.ui.songs


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicman.R
import com.example.musicman.databinding.FragmentSongsBinding
import com.example.musicman.ui.decorations.SimpleItemDecoration
import com.example.musicman.model.Song
import com.example.musicman.ui.adapter.SongAdapter
import com.example.musicman.ui.player.PlayerFragment
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SongsFragment : Fragment(R.layout.fragment_songs) {

    private val songsViewModel by viewModels<SongsViewModel>()
    private val binding by viewBinding(FragmentSongsBinding::bind)
    private val adapter by lazy {
        SongAdapter {
            songsViewModel.songItemClicked(it.id)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.songsList.layoutManager = LinearLayoutManager(requireActivity())
        binding.songsList.adapter = adapter
        binding.songsList.addItemDecoration(SimpleItemDecoration(resources.getDimensionPixelSize(R.dimen.padding_half)))
        songsViewModel.songs.observe(viewLifecycleOwner) {
            Log.i(TAG, "songs observer: got list")
            adapter.updateSongs(it)
        }

        songsViewModel.navigateToPlayer.observe(viewLifecycleOwner) {
            Log.i(TAG, "navigateToPlayer observer: (event consumed = ${it.consumed})")
            it.consume()?.let { id ->
                val action = SongsFragmentDirections.playSong(id)
                findNavController().navigate(action)
            }
        }
    }

    companion object {
        private const val TAG = "SongsFragment"
    }
}