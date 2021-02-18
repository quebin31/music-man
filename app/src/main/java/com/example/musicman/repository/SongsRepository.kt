package com.example.musicman.repository

import androidx.lifecycle.LiveData
import com.example.musicman.model.Song

interface SongsRepository {
    fun getSongs(): LiveData<List<Song>>
    fun getSong(id: String): LiveData<Song?>
    fun getLatestPlayedSong(): LiveData<Song?>
    fun setLatestPlayedSong(song: Song)
}