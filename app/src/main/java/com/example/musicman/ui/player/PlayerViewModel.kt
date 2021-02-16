package com.example.musicman.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicman.model.Song
import com.example.musicman.repository.SongsRepository

class PlayerViewModel : ViewModel() {
    lateinit var repository: SongsRepository

    val currentSong: LiveData<Song?>
        get() = repository.getCurrentSong()

    fun setCurrentSong(song: Song) {
        repository.setCurrentSong(song)
    }
}