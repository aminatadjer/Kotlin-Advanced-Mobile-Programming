package com.example.exo3

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val PERMISSION_CODE: Int = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            val permission = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            requestPermissions(permission, PERMISSION_CODE)
        }else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location ->
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
                    fajr_view.text = fajr
                    duhr_view.text = duhr
                    asr_view.text =  asr
                    maghrib_view.text = maghrib
                    isha_view.text = isha
                    savePrayerTimes(fajr, duhr, asr, maghrib, isha)
                }
        }
        val adhanServiceIntent = Intent(this, AdhanService::class.java)
        this.startService(adhanServiceIntent)
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

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {
                    Toast.makeText(this, "Vous ne disposez pas des permissions necessaires", Toast.LENGTH_SHORT)
                }
                return
            }
        }
    }


}
