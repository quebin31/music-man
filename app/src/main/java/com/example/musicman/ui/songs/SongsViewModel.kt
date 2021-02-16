package com.example.musicman.ui.songs

import androidx.lifecycle.ViewModel
import com.example.musicman.repository.SongsRepository

class SongsViewModel : ViewModel() {
    lateinit var repository: SongsRepository

    val songs by lazy { repository.getSongs() }
}