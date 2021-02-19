package com.example.musicman.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.musicman.model.Song
import com.example.musicman.repository.SongsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(private val repository: SongsRepository) : ViewModel() {
    val latestPlayedSong: LiveData<Song?>
        get() = repository.getLatestPlayedSong()

    fun setLatestPlayedSong(song: Song) {
        repository.setLatestPlayedSong(song)
    }

    fun getSong(songId: String) = repository.getSong(songId)
}