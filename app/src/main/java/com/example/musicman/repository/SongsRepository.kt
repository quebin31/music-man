package com.example.musicman.repository

import androidx.lifecycle.LiveData
import com.example.musicman.room.Song

interface SongsRepository {
    /** Get all songs */
    val songs: LiveData<List<Song>>

    /** Get latest played song id */
    val latestPlayedSong: LiveData<String?>

    /** Get an specific song by its [id] */
    operator fun get(id: String): Song?

    /** Set the latest played song by its [id] */
    suspend fun setLatestPlayedSong(id: String)
}