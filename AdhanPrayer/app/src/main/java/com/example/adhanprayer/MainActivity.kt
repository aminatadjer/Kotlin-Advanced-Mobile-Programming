package com.example.adhanprayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent


class MainActivity : AppCompatActivity() {
    private val ACTION_SERVICE = "myaction"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myintent = Intent()
        myintent.setClass(this, DeviceReceiver::class.java)
        sendBroadcast(myintent)
        val intent = Intent(this, AdhanService::class.java)
        intent.action = ACTION_SERVICE
        startService(intent)
    }
}