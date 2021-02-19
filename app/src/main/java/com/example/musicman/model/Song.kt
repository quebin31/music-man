package com.example.musicman.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import com.example.musicman.R

data class Song(
    val id: String,
    val uri: Uri,
    val title: String?,
    val album: String?,
    val artist: String?,
    val albumArtist: String?,
    val track: Int?,
    val disk: Int?,
    val artworkBitmap: Bitmap?
) {
    val effectiveArtist: String? get() = albumArtist ?: artist

    fun asMediaMetadata(context: Context): MediaMetadataCompat {
        val unknown = context.getString(R.string.unknown_string)
        val defaultArtwork by lazy {
            BitmapFactory.decodeResource(context.resources, R.drawable.default_album_artwork)
        }

        return MediaMetadataCompat.Builder().run {
            putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
            putString(MediaMetadataCompat.METADATA_KEY_TITLE, title ?: unknown)
            putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, title ?: unknown)
            putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist ?: unknown)
            putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album ?: unknown)
            putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, albumArtist ?: unknown)
            putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, track?.toLong() ?: 0L)
            putLong(MediaMetadataCompat.METADATA_KEY_DISC_NUMBER, disk?.toLong() ?: 0L)
            putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, artworkBitmap ?: defaultArtwork)
            build()
        }
    }
}
