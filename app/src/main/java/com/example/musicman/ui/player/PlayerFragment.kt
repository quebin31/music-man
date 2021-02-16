package com.example.musicman.ui.player

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.musicman.R
import com.example.musicman.databinding.FragmentPlayerBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private val detailsViewModel by viewModels<PlayerViewModel>()
    private val binding by viewBinding(FragmentPlayerBinding::bind)


}