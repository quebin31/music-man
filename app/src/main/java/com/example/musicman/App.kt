package com.example.musicman

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.musicman.player.MusicPlayerService

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        setupNotificationChannel()
        startMusicPlayerService()
    }

    private fun startMusicPlayerService() {
        val intent = Intent(applicationContext, MusicPlayerService::class.java)
        startService(intent)
    }

    private fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.player_channel_name)
            val description = getString(R.string.player_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(MusicPlayerService.NOTIFICATION_CHANNEL, name, importance).apply {
                this.description = description
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}