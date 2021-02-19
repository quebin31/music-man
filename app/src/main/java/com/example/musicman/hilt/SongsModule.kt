package com.example.musicman.hilt

import com.example.musicman.repository.RawSongsRepository
import com.example.musicman.repository.SongsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class SongsModule {

    @Binds
    abstract fun bindSongsRepository(rawSongsRepository: RawSongsRepository): SongsRepository
}