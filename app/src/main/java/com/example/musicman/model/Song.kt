package com.example.musicman.model

import android.graphics.Bitmap

data class Song(
    val id: String,
    val title: String?,
    val album: String?,
    val artist: String?,
    val albumArtist: String?,
    val track: Int?,
    val disk: Int?,
    val artworkBitmap: Bitmap?
) {
    val effectiveArtist: String?
        get() = albumArtist ?: artist
}
