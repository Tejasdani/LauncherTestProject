package com.example.jiolaunchertest.listner

import android.content.Context
import android.content.Intent

interface DesktopReceiverListener {
    fun desktopResult(context: Context?, intent: Intent?)
}