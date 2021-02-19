package com.example.musicman.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicman.model.Song
import com.example.musicman.repository.SongsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(private val repository: SongsRepository) : ViewModel() {
    val latestPlayedSong: LiveData<String?>
        get() = repository.latestPlayedSong

    fun setLatestPlayedSong(id: String) {
        viewModelScope.launch {
            repository.setLatestPlayedSong(id)
        }
    }

    fun getSong(id: String) = repository[id]
}