package com.example.musicman.repository

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.musicman.R
import com.example.musicman.extensions.*
import com.example.musicman.model.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RawSongsRepository @Inject constructor(@ApplicationContext private val context: Context) :
    SongsRepository {

    override fun getSongs(): LiveData<List<Song>> = liveData {
        Log.i("Repo", "getSongs: getting songs")
        emit(listOf("raw1", "raw2", "raw3").mapNotNull { buildSong(it) })
    }

    override fun getSong(id: String) = buildSong(id)

    override fun setLatestPlayedSong(song: Song) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit(commit = true) {
            putString(KEY_LATEST_SONG, song.id)
        }
    }

    override fun getLatestPlayedSong(): LiveData<Song?> = liveData {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val song = prefs.getString(KEY_LATEST_SONG, null)?.let { buildSong(it) }
        emit(song)
    }

    private fun buildSong(id: String): Song? {
        val uri = when (id) {
            "raw1" -> context.getAndroidUri(R.raw.raw1)
            "raw2" -> context.getAndroidUri(R.raw.raw2)
            "raw3" -> context.getAndroidUri(R.raw.raw3)
            else -> return null
        }

        val metadataRetriever = MediaMetadataRetriever().apply {
            setDataSource(context, uri)
        }

        val title = metadataRetriever.title
        val album = metadataRetriever.album
        val artist = metadataRetriever.artist
        val albumArtist = metadataRetriever.albumArtist
        val track = metadataRetriever.track
        val disc = metadataRetriever.disc
        val artworkBitmap = metadataRetriever.albumArt

        return Song(id, uri, title, album, artist, albumArtist, track, disc, artworkBitmap)
    }

    companion object {
        private const val PREFS = "RawRepositoryPreferences"
        private const val KEY_LATEST_SONG = "latest_song"
    }
}