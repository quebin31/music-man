package com.example.musicman.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever

inline val MediaMetadataRetriever.title: String?
    get() = extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)

inline val MediaMetadataRetriever.album: String?
    get() = extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)

inline val MediaMetadataRetriever.artist: String?
    get() = extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

inline val MediaMetadataRetriever.albumArtist: String?
    get() = extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)

inline val MediaMetadataRetriever.track: Long?
    get() = extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)?.toLongOrNull()

inline val MediaMetadataRetriever.disc: Long?
    get() = extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER)?.toLongOrNull()

inline val MediaMetadataRetriever.albumArt: Bitmap?
    get() = embeddedPicture?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }


