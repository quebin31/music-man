package com.example.musicman.ui.player

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.musicman.model.Song
import com.example.musicman.repository.SongsRepository

class PlayerViewModel : ViewModel() {
    lateinit var repository: SongsRepository

    val latestPlayedSong: LiveData<Song?>
        get() = repository.getLatestPlayedSong()

    fun setLatestPlayedSong(song: Song) {
        repository.setLatestPlayedSong(song)
    }

    fun getSongUri(song: Song): Uri? = repository.getSongUri(song.id)

    fun getSong(songId: String): Song? = repository.getSong(songId)
}