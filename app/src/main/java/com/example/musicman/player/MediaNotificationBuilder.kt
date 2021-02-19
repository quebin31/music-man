package com.example.musicman.player

import android.app.Notification
import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media.session.MediaButtonReceiver
import com.example.musicman.R
import com.example.musicman.ui.MainActivity

class MediaNotificationBuilder(
    private val context: Context,
    private val mediaSession: MediaSessionCompat
) {

    fun build(notificationChannel: String, showPause: Boolean): Notification {
        val controller = mediaSession.controller
        val metadata = controller.metadata
        val description = metadata.description

        return NotificationCompat.Builder(context, notificationChannel).run {
            setContentTitle(description.title)
            setContentText(description.subtitle)
            setSubText(description.description)
            setLargeIcon(description.iconBitmap)

            setContentIntent(MainActivity.newPlayerPendingIntent(context))
            setDeleteIntent(
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_STOP
                )
            )

            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setSmallIcon(R.drawable.ic_baseline_music_note_24)
            color = ContextCompat.getColor(context, R.color.design_default_color_primary_dark)

            val drawable = if (showPause) R.drawable.ic_baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24
            val actionString = if (showPause) context.getString(R.string.pause) else context.getString(R.string.play)

            addAction(
                NotificationCompat.Action(
                    drawable,
                    actionString,
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        context,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE
                    )
                )
            )

            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(0)
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            context,
                            PlaybackStateCompat.ACTION_STOP
                        )
                    )
            )

            build()
        }
    }
}