package com.example.musicman.ui.songs


import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.musicman.R
import com.example.musicman.databinding.FragmentSongsBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class SongsFragment : Fragment(R.layout.fragment_songs) {

    private val songsViewModel by viewModels<SongsViewModel>()
    private val binding by viewBinding(FragmentSongsBinding::bind)
}