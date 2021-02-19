package com.example.musicman.ui.songs

import androidx.lifecycle.ViewModel
import com.example.musicman.repository.SongsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongsViewModel @Inject constructor(private val repository: SongsRepository) : ViewModel() {

    val songs by lazy { repository.songs }
}