package com.example.musicman.extensions

import android.graphics.Bitmap
import android.net.Uri
import android.support.v4.media.MediaMetadataCompat

inline val MediaMetadataCompat.id: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)

inline val MediaMetadataCompat.uri: Uri?
    get() = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)?.let { Uri.parse(it) }

inline val MediaMetadataCompat.title: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_TITLE)

inline val MediaMetadataCompat.artist: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_ARTIST)

inline val MediaMetadataCompat.album: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_ALBUM)

inline val MediaMetadataCompat.albumArtist: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST)

inline val MediaMetadataCompat.track: Long
    get() = getLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER)

inline val MediaMetadataCompat.disc: Long
    get() = getLong(MediaMetadataCompat.METADATA_KEY_DISC_NUMBER)

inline val MediaMetadataCompat.albumArt: Bitmap?
    get() = getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART)

inline val MediaMetadataCompat.effectiveArtist: String?
    get() = albumArtist ?: artist

const val NO_GET = "Builder doesn't have get"

inline var MediaMetadataCompat.Builder.id: String
    get() = throw IllegalAccessException(NO_GET)
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, value)
    }

inline var MediaMetadataCompat.Builder.uri: String
    get() = throw IllegalAccessException(NO_GET)
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, value)
    }

inline var MediaMetadataCompat.Builder.title: String
    get() = throw IllegalAccessException(NO_GET)
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_TITLE, value)
    }

inline var MediaMetadataCompat.Builder.artist: String
    get() = throw IllegalAccessException(NO_GET)
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_ARTIST, value)
    }

inline var MediaMetadataCompat.Builder.album: String
    get() = throw IllegalAccessException(NO_GET)
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_ALBUM, value)
    }

inline var MediaMetadataCompat.Builder.albumArtist: String
    get() = throw IllegalAccessException(NO_GET)
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, value)
    }

inline var MediaMetadataCompat.Builder.track: Long
    get() = throw IllegalAccessException(NO_GET)
    set(value) {
        putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, value)
    }

inline var MediaMetadataCompat.Builder.disc: Long
    get() = throw IllegalAccessException(NO_GET)
    set(value) {
        putLong(MediaMetadataCompat.METADATA_KEY_DISC_NUMBER, value)
    }

inline var MediaMetadataCompat.Builder.albumArt: Bitmap
    get() = throw IllegalAccessException(NO_GET)
    set(value) {
        putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, value)
    }