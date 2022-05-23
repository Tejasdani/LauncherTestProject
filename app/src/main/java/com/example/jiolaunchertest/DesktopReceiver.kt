package com.example.jiolaunchertest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.jiolaunchertest.listner.DesktopReceiverListener


class DesktopReceiver : BroadcastReceiver() {
     lateinit var desktopReceiverListener: DesktopReceiverListener
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("DesktopReceiver ","Desktop-receiver: ${intent!!.action}")
        desktopReceiverListener.desktopResult(context,intent)
    }

    fun setOnDesktopReceiverListener(desktopReceiverListenerParams: DesktopReceiverListener){
        desktopReceiverListener = desktopReceiverListenerParams
    }


}