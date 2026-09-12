package com.tejuzumaki.directdrop

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder

class DirectDropForegroundService : Service() {
    companion object {
        const val CHANNEL_ID = "DirectDropChannel"
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "DirectDrop Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action ?: return START_STICKY
        val peerName = intent.getStringExtra("peer_name") ?: "Unknown"
        val message = intent.getStringExtra("message") ?: ""

        if (action == "SHOW_NOTIFICATION" || action == "START_RINGING") {
            val notification: Notification
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification = Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("DirectDrop: $peerName")
                    .setContentText(if (message.isNotEmpty()) message else "Incoming call")
                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
                    .build()
            } else {
                @Suppress("DEPRECATION")
                notification = Notification.Builder(this)
                    .setContentTitle("DirectDrop: $peerName")
                    .setContentText(if (message.isNotEmpty()) message else "Incoming call")
                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
                    .build()
            }
            
            startForeground(1, notification)

            if (action == "START_RINGING") {
                val alarmTone: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                val ringtone = RingtoneManager.getRingtone(applicationContext, alarmTone)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ringtone.audioAttributes = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                        .build()
                }
                ringtone.play()
            }
        } else if (action == "STOP_RINGING") {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
