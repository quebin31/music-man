package com.example.musicman.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.musicman.R
import com.example.musicman.databinding.ListItemSongBinding
import com.example.musicman.model.Song


class SongViewHolder(private val binding: ListItemSongBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(song: Song, onItemClick: (Song) -> Unit) {
        val context = itemView.context
        val unknownString = context.getString(R.string.unknown_string)

        song.albumArt?.let { binding.songAlbumArtwork.setImageBitmap(it) }
        binding.songTitle.text = song.title ?: unknownString

        val artist = song.effectiveArtist ?: unknownString
        val album = song.album ?: unknownString
        binding.songArtistAlbum.text = context.getString(R.string.artist_album, artist, album)

        binding.root.setOnClickListener { onItemClick(song) }
    }
}

