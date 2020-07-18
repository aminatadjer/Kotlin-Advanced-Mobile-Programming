package com.example.exo3

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log


class AutoStart : BroadcastReceiver() {

    val RESTART_SERVICE = "Restart.Adhan.APP"

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action.equals(RESTART_SERVICE) || intent.action.equals(Intent.ACTION_BOOT_COMPLETED))
        {


            context.startService(Intent(context, AdhanService::class.java))


        }


    }
}