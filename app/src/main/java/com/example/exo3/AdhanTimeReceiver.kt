package com.example.exo3

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log

class AdhanTimeReceiver : BroadcastReceiver(){
// Send notification with adhan media player
    val CUSTOM_INTENT = "Start.Adhan.APP"

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action.equals(CUSTOM_INTENT))
        {
            val titre = "الاذان"
            val contenu = "حان الان موعد الاذان"

            val pIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), intent, 0)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val mChannel = NotificationChannel(
                    "ch00", "ch00", NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(mChannel)
                val noti = Notification.Builder(context,"ch00")
                    .setContentTitle(titre)
                    .setContentText(contenu).setSmallIcon(android.R.drawable.btn_star)
                    .setContentIntent(pIntent).setAutoCancel(true)

                    .build()
                notificationManager.notify(0, noti)

            }else{

                val noti = Notification.Builder(context)
                    .setContentTitle(titre)
                    .setContentText(contenu).setSmallIcon(android.R.drawable.btn_star)
                    .setContentIntent(pIntent).setAutoCancel(true)

                    .build()
                notificationManager.notify(0, noti)

            }

            val player : MediaPlayer = MediaPlayer.create(context, R.raw.adhan)
            player.start()
        }

    }



}
