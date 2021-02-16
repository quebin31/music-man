package com.example.musicman.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.musicman.R
import com.example.musicman.databinding.ListItemSongBinding
import com.example.musicman.model.Song


class SongViewHolder(private val binding: ListItemSongBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(context: Context, song: Song, onItemClick: (Song) -> Unit) {
        val unknownString = context.getString(R.string.unknown_string)

        binding.songTitle.text = song.title ?: unknownString
        val artist = song.effectiveArtist ?: unknownString
        val album = song.album ?: unknownString
        binding.songArtistAlbum.text = context.getString(R.string.artist_album, artist, album)
        song.artworkBitmap?.let {
            binding.albumArtwork.setImageBitmap(it)
        }

        binding.root.setOnClickListener { onItemClick(song) }
    }
}
