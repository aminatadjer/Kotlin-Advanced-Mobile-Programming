package com.example.adhanprayer


import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.azan.Azan
import com.azan.Method
import com.azan.astrologicalCalc.Location
import com.azan.astrologicalCalc.SimpleDate
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AdhanService: Service() {

    var mediaPlayer: MediaPlayer? = null
    var audiManager: AudioManager? = null
    private val channelId = "01"
    private val channelName = "Amina's channel"
    private val importance = NotificationManager.IMPORTANCE_HIGH

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        val today = SimpleDate(GregorianCalendar())
        val location = Location(30.045411, 31.236735, 2.0, 0)
        val azan = Azan(location, Method.EGYPT_SURVEY)
        val prayerTimes = azan.getPrayerTimes(today)
        val prayerTime=arrayOf(prayerTimes.fajr(),prayerTimes.thuhr(),prayerTimes.assr(),prayerTimes.maghrib(),prayerTimes.ishaa(),"22:50:00")
        audiManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mediaPlayer = MediaPlayer.create(this, R.raw.adhan)
        val sdf = SimpleDateFormat("hh:mm:ss")
        var currentTime = sdf.format(Date())
        while (!prayerTime.contains(currentTime)) {
            currentTime = sdf.format(Date())
        }
        notification()
        mediaPlayer!!.start()
        mediaPlayer!!.setOnCompletionListener({ mediaPlayer?.release() })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    fun notification() {
        val myintent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, System.currentTimeMillis().toInt(), myintent, 0)
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.putExtra("data", "detail")
        val pNotifIntent1 = PendingIntent.getActivity(this, System.currentTimeMillis().toInt(), notificationIntent, 0)
        val icon = Icon.createWithResource(this, android.R.drawable.btn_minus)
        val action = Notification.Action.Builder(icon, "detail", pNotifIntent1).build()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                channelId, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
        }
        val myNotification = Notification.Builder(this, channelId)
            .setContentTitle("Adhan")
            .setContentText("It's prayer Time !")
            .setSmallIcon(android.R.drawable.btn_dialog)
            .setContentIntent(pendingIntent)
            .addAction(action)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(1, myNotification)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

}