package com.example.musicman.repository

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.musicman.R
import com.example.musicman.extensions.getAndroidUri
import com.example.musicman.model.Song

class RawSongsRepository(private val context: Context) : SongsRepository {

    override fun getSongs(): LiveData<List<Song>> = liveData {
        Log.i("Repo", "getSongs: getting songs")
        emit(listOf("raw1", "raw2", "raw3").mapNotNull { buildSong(it) })
    }

    override fun getSong(id: String): LiveData<Song?> = liveData {
        emit(buildSong(id))
    }

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
            "raw1" -> R.raw.raw1.getAndroidUri(context)
            "raw2" -> R.raw.raw2.getAndroidUri(context)
            "raw3" -> R.raw.raw3.getAndroidUri(context)
            else -> return null
        }

        val metadataRetriever = MediaMetadataRetriever()
        metadataRetriever.setDataSource(context, uri)

        val title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        val album = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
        val artist = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        val albumArtist =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)
        val track =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)
                ?.toIntOrNull()
        val disk =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER)
                ?.toIntOrNull()

        val artworkBitmap = metadataRetriever.embeddedPicture?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }

        return Song(id, uri, title, album, artist, albumArtist, track, disk, artworkBitmap)
    }

    companion object {
        private const val PREFS = "RawRepositoryPreferences"
        private const val KEY_LATEST_SONG = "latest_song"
    }
}