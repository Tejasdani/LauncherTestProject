package com.example.jiolaunchertest

import android.app.Application
import android.content.Context

@Suppress("DEPRECATION")
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    init {
        context = this
    }

    companion object {
        lateinit var context: Context
    }
}