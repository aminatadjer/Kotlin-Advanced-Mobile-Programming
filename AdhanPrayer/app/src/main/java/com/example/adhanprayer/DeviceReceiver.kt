package com.example.adhanprayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DeviceReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent!!.action
        if(Intent.ACTION_BOOT_COMPLETED.equals(action))
        {
            val intent =  Intent(context, AdhanService::class.java)
            context?.startService(intent)
        }
    }
}