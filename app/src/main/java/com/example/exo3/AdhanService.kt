package com.example.exo3

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.fixedRateTimer


class AdhanService : Service() {
    val CUSTOM_INTENT = "Start.Adhan.APP"
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val context = this
        val pref = getSharedPreferences("PREF",0)
        val fajr = pref.getString("fajr", "fajrr")!!
        val duhr = pref.getString("duhr", "duhr")!!
        val asr = pref.getString("asr", "asr")!!
        val maghrib = pref.getString("maghrib", "maghrib")!!
        val isha = pref.getString("isha", "isha")!!
        var prayersTime = arrayListOf(fajr, duhr, asr, maghrib, isha)
        fixedRateTimer("timer",false,0,1000*60*60*24){

            var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location ->
                    val coordinates = Coordinates(location.latitude, location.longitude)
                    val date = DateComponents.from(Date())
                    val params = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
                    params.madhab = Madhab.SHAFI
                    params.adjustments.fajr = 2
                    val prayerTimes = PrayerTimes(coordinates, date, params)
                    val formatter = SimpleDateFormat("HH:mm")
                    formatter.timeZone = TimeZone.getTimeZone("Africa/Algiers")
                    val fajr = formatter.format(prayerTimes.fajr)
                    val duhr = formatter.format(prayerTimes.dhuhr)
                    val asr = formatter.format(prayerTimes.asr)
                    val maghrib = "20:40"
                    val isha = formatter.format(prayerTimes.isha)
                    savePrayerTimes(fajr, duhr, asr, maghrib, isha)
                    prayersTime = arrayListOf(fajr, duhr, asr, maghrib, isha)

                }
        }

        fixedRateTimer("timer",false,0,1000*60){
            val sdf = SimpleDateFormat("HH:mm")
            val currentTime = sdf.format(Date())
            Log.i("currentTime", currentTime)
            if (prayersTime.contains(currentTime)){
                val i = Intent()
                i.action = CUSTOM_INTENT
                i.setClass(context, AdhanTimeReceiver::class.java)
                context.sendBroadcast(i)
                Log.i("notification sent", currentTime)
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun savePrayerTimes(fajr : String, duhr : String, asr : String, maghrib : String, isha : String){

        val pref = getSharedPreferences("PREF",0)
        val editor = pref.edit()
        editor.putString("fajr", fajr)
        editor.putString("duhr", duhr)
        editor.putString("asr", asr)
        editor.putString("maghrib", maghrib)
        editor.putString("isha", isha)
        editor.commit()

    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val RESTART_SERVICE = "Restart.Adhan.APP"
        val i = Intent()
        i.action = RESTART_SERVICE
        i.setClass(this, AutoStart::class.java)
        this.sendBroadcast(i)
        super.onTaskRemoved(rootIntent)
    }
}