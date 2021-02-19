package com.example.musicman.repository

import android.content.Context
import android.media.MediaMetadataRetriever
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.musicman.R
import com.example.musicman.extensions.*
import com.example.musicman.room.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RawSongsRepository @Inject constructor(@ApplicationContext private val context: Context) :
    SongsRepository {

    private val dataStore: DataStore<Preferences> = context.createDataStore(STORE_NAME)

    override val songs: LiveData<List<Song>>
        get() = liveData {
            emit(listOf("raw1", "raw2", "raw3").mapNotNull { buildRawSong(it) })
        }

    override val latestPlayedSong: LiveData<String?>
        get() = dataStore.data.map { it[LATEST_SONG] }.asLiveData()

    override fun get(id: String): Song? = buildRawSong(id)

    override suspend fun setLatestPlayedSong(id: String) {
        dataStore.edit { settings ->
            settings[LATEST_SONG] = id
        }
    }

    private fun buildRawSong(id: String): Song? {
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
        private const val STORE_NAME = "com.example.musicman.store"
        private val LATEST_SONG = stringPreferencesKey("com.example.musicman.store.latestsong")
    }
}