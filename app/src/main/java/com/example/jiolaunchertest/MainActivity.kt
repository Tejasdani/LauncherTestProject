package com.example.jiolaunchertest

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity() {

    lateinit var fragmentManager: FragmentManager
    lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        frameLayout = findViewById(R.id.fragmentContainer)
        frameLayout = FrameLayout(this)
        fragmentManager = supportFragmentManager
        addHomeFragment()
    }

    private fun addHomeFragment() {
        try {
            val homeFragment: AppTrayFragment = AppTrayFragment.newInstance()
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, homeFragment)
                .commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}