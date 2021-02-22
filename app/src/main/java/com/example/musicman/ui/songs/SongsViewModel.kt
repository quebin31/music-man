package com.example.musicman.ui.songs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicman.event.Event
import com.example.musicman.repository.SongsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongsViewModel @Inject constructor(private val repository: SongsRepository) : ViewModel() {

    val songs by lazy { repository.songs }

    private val _navigateToPlayer = MutableLiveData<Event<String>>()
    val navigateToPlayer: LiveData<Event<String>>
        get() = _navigateToPlayer

    fun songItemClicked(songId: String) {
        _navigateToPlayer.value = Event(songId)
    }
}