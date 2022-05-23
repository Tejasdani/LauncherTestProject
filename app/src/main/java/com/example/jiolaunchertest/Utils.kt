package com.example.jiolaunchertest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.example.jiolaunchertest.MyApplication.Companion.context
import com.example.jiolaunchertest.dao.AppList
import com.example.jiolaunchertest.dao.AppListData
import com.example.jiolaunchertest.dao.IconData
import com.example.jiolaunchertest.presenter.AppBasicInfoComparator

object Utils {
    @JvmStatic
    var menuList: MutableList<AppListData> = mutableListOf()

    var shortCutDrawableList: MutableList<IconData> = mutableListOf()
    const val PACKAGES_ARGUMENT = "args_package_names"

    @JvmStatic
    var appPackageList: Set<String> = setOf()

    fun launchApplication(packageName: String) {
        val startIntent: Intent = try {
            context.packageManager.getLeanbackLaunchIntentForPackage(packageName)!!
        } catch (e: Exception) {
            context.packageManager.getLaunchIntentForPackage(packageName)!!
        }
        //startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(startIntent)


    }

    fun arrangeAppItem(installedAppList: List<AppListData>): List<AppListData> {
        var appData: MutableList<AppListData> = installedAppList.toMutableList()
        val nonMovableItem = appList.filterValues { it.movable == 1 }
        val filter = nonMovableItem.flatMap { api -> appData.filter { api.key == it.packageName } }
        appData.removeAll(filter)
        Log.d(
            "arrangeAppItem ",
            "swap-same-item-exist-count-${filter.size}, old-count-${installedAppList.size}, new-count-${appData.size}"
        )
        val finalData: MutableList<AppListData> = mutableListOf()
        finalData.addAll(filter.sortedWith(AppBasicInfoComparator.INSTANCE))
        appData = appData.sortedWith(AppBasicInfoComparator.INSTANCE).toMutableList()
        appData.forEach {
            val item = finalData.find { api -> api.appList.row > it.appList.row }
            if (item != null) {
                if (finalData.size > finalData.indexOf(item))
                    finalData.add(finalData.indexOf(item), it)
                else
                    finalData.add(it)
            } else
                finalData.add(it)
        }
        return finalData
    }

    fun registerPackageListener(context: Context, packageReceiver: BroadcastReceiver) {
        try {
            val packageIntentFilter = IntentFilter()
            packageIntentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
            packageIntentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED)
            packageIntentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
            packageIntentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED)
            packageIntentFilter.addDataScheme("package")
            context.registerReceiver(packageReceiver, packageIntentFilter)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    fun unregisterPackageListener(context: Context, packageReceiver: BroadcastReceiver) {
        try {
            context.unregisterReceiver(packageReceiver)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    var isJioMenuOpen = false
    var isAppOpened = false

    @JvmStatic
    var isSettingsOpen = false


    @JvmStatic
    lateinit var appList: MutableMap<String, AppList>

}