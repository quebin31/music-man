package com.example.musicman.repository

import android.net.Uri
import com.example.musicman.model.Song

interface SongsRepository {
    fun getSongsIds(): List<String>
    fun getSong(id: String): Song?
    fun getSongs(): List<Song>
    fun getSongUri(id: String): Uri?
}