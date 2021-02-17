package com.example.musicman.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.musicman.model.Song

interface SongsRepository {
    fun getSongsIds(): List<String>
    fun getSong(id: String): Song?
    fun getSongs(): List<Song>
    fun getSongUri(id: String): Uri?
    fun getLatestPlayedSong(): LiveData<Song?>
    fun setLatestPlayedSong(song: Song)
}