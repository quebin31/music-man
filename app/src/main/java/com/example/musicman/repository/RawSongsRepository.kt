package com.example.musicman.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.example.musicman.R
import com.example.musicman.extensions.getAndroidUri
import com.example.musicman.model.Song

class RawSongsRepository(private val context: Context) : SongsRepository {

    override fun getSongsIds() = listOf("raw1", "raw2", "raw3")

    override fun getSong(id: String): Song? = when (id) {
        "raw1" -> Song(
            id,
            "El Mundo Extraño",
            "La Sintesis O'Konor",
            "El Mató a un Policía Motorizado",
            "El Mató a un Policía Motorizado",
            9,
            1,
            getAlbumArtwork(id)
        )

        "raw2" -> Song(
            id,
            "El Tesoro",
            "La Sintesis O'Konor",
            "El Mató a un Policía Motorizado",
            "El Mató a un Policía Motorizado",
            1,
            1,
            getAlbumArtwork(id)
        )

        "raw3" -> Song(
            id,
            "Espacio Vacío",
            "Espacio Vacío",
            "El Mató a un Policía Motorizado",
            "El Mató a un Policía Motorizado, Carolina Durante",
            1,
            1,
            getAlbumArtwork(id)
        )

        else -> null
    }

    override fun getSongs(): List<Song> = getSongsIds().mapNotNull { getSong(it) }

    override fun getSongUri(id: String): Uri? = when (id) {
        "raw1" -> R.raw.el_mundo_extrano.getAndroidUri(context)
        "raw2" -> R.raw.el_tesoro.getAndroidUri(context)
        "raw3" -> R.raw.espacio_vacio.getAndroidUri(context)
        else -> null
    }

    private fun getAlbumArtwork(id: String): Bitmap? {
        val metadataRetriever = MediaMetadataRetriever()
        metadataRetriever.setDataSource(context, getSongUri(id) ?: return null)
        val pictureBytes = metadataRetriever.embeddedPicture
        return BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes?.size ?: return null)
    }
}