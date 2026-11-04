package com.dicoding.scheduler

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dicoding.R

class EventScheduler : BroadcastReceiver() {


    companion object {
        const val NOTIFY_CONTENT_ID = "101"
        const val CONTENT_TITLE_KEY = "Dicoding Event Now"
        const val CONTENT_TEXT_KEY = "content_text_key"
        private const val NOTIFY_ID = 101

        const val SCHEDULE_CHANNEL = "scheduler_channel"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        intent ?: return

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(
            context,
            SCHEDULE_CHANNEL
        )
            .setSmallIcon(R.drawable.baseline_upcoming_24)
            .setContentTitle(intent.getStringExtra(CONTENT_TITLE_KEY))
            .setContentText(intent.getStringExtra(CONTENT_TEXT_KEY))
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)


        val notification = builder.build()
        notificationManagerCompat.notify(NOTIFY_ID, notification)
    }


}