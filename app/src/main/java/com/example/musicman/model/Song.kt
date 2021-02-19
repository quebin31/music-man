package com.example.musicman.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import com.example.musicman.R
import com.example.musicman.extensions.*

data class Song(
    val id: String,
    val uri: Uri,
    val title: String?,
    val album: String?,
    val artist: String?,
    val albumArtist: String?,
    val track: Long?,
    val disc: Long?,
    val albumArt: Bitmap?
) {
    val effectiveArtist: String? get() = albumArtist ?: artist

    fun asMediaMetadata(context: Context): MediaMetadataCompat {
        val unknown = context.getString(R.string.unknown_string)
        val defaultAlbumArt by lazy {
            BitmapFactory.decodeResource(context.resources, R.drawable.default_album_artwork)
        }

        return MediaMetadataCompat.Builder().let {
            it.id = id
            it.uri = uri.toString()
            it.title = title ?: unknown
            it.artist = artist ?: unknown
            it.album = album ?: unknown
            it.artist = artist ?: unknown
            it.albumArtist = albumArtist ?: unknown
            it.track = track ?: 0L
            it.disc = disc ?: 0L
            it.albumArt = albumArt ?: defaultAlbumArt
            it.build()
        }
    }

    companion object {
        fun fromMediaMetadata(metadata: MediaMetadataCompat) = Song(
            metadata.id!!,
            metadata.uri!!,
            metadata.title,
            metadata.album,
            metadata.artist,
            metadata.albumArtist,
            metadata.track,
            metadata.disc,
            metadata.albumArt
        )
    }
}

