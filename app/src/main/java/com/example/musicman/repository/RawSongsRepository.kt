package com.example.musicman.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicman.R
import com.example.musicman.extensions.getAndroidUri
import com.example.musicman.model.Song

class RawSongsRepository(private val context: Context) : SongsRepository {

    private val _latestSong by lazy {
        MutableLiveData<Song?>().apply {
            value = getLatestFromPreferences()
        }
    }

    override fun getSongsIds() = listOf("raw1", "raw2", "raw3")

    override fun getSong(id: String): Song? = buildSongFromMetadata(id)

    override fun getSongs(): List<Song> = getSongsIds().mapNotNull { getSong(it) }

    override fun getSongUri(id: String): Uri? = when (id) {
        "raw1" -> R.raw.el_mundo_extrano.getAndroidUri(context)
        "raw2" -> R.raw.el_tesoro.getAndroidUri(context)
        "raw3" -> R.raw.espacio_vacio.getAndroidUri(context)
        else -> null
    }

    override fun setLatestPlayedSong(song: Song) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit(commit = true) {
            putString(KEY_LATEST_SONG, song.id)
        }

        _latestSong.value = song
    }

    override fun getLatestPlayedSong(): LiveData<Song?> = _latestSong

    private fun getLatestFromPreferences(): Song? {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val songId = prefs.getString(KEY_LATEST_SONG, null) ?: return null
        return getSong(songId)
    }

    private fun buildSongFromMetadata(id: String): Song? {
        val metadataRetriever = MediaMetadataRetriever()
        metadataRetriever.setDataSource(context, getSongUri(id) ?: return null)

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

        return Song(id, title, album, artist, albumArtist, track, disk, artworkBitmap)
    }

    companion object {
        private const val PREFS = "RawRepositoryPreferences"
        private const val KEY_LATEST_SONG = "latest_song"
    }
}