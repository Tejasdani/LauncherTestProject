package com.example.jiolaunchertest.dao

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.WorkerThread
import com.example.jiolaunchertest.Utils.appPackageList
import com.example.jiolaunchertest.Utils.arrangeAppItem
import com.example.jiolaunchertest.presenter.AppBasicInfoComparator
import com.google.gson.GsonBuilder
import java.io.InputStream
import java.nio.charset.Charset


@WorkerThread
class AppBasicDataService(val packageManager: PackageManager) {

    private var TAG = "AppBasicDataService"

    @WorkerThread
    fun getAll(appList: MutableMap<String, AppList>): List<AppListData> {
        val applications = packageManager.getInstalledPackages(0)
        val packages = ArrayList<AppListData>(applications.size)
        var position = applications.size
        applications.forEach {


            if (it.applicationInfo != null &&
                (packageManager.queryIntentActivities(
                    Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
                        .setPackage(it.applicationInfo.packageName),
                    PackageManager.MATCH_ALL
                ).isNotEmpty() || packageManager.queryIntentActivities(
                    Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_LAUNCHER)
                        .setPackage(it.applicationInfo.packageName),
                    PackageManager.MATCH_ALL
                ).isNotEmpty())
            ) {
                val appData = AppListData(position, it, packageManager)
                if (appList.containsKey(it.packageName)) {
                    appData.appList = appList.getValue(it.packageName)
                } else {
                    val dummyList = AppList()
                    dummyList.packageName = it.packageName
                    dummyList.row = position
                    appData.appList = dummyList
                }
                Log.d(TAG, "app-position: ${appData.appList.row} , ${it.packageName}")
                appPackageList.plus(it.packageName)
                packages.add(appData)
                position++
            }
        }
        return arrangeAppItem(packages.sortedWith(AppBasicInfoComparator.INSTANCE))
    }

    @WorkerThread
    fun getAppListData(
        appList: List<AppListData>,
        storeList: MutableMap<String, AppList>,
        count: Int,
        packageNames: String
    ): AppListData? {
        try {
            val packageInfo = packageManager.getPackageInfo(packageNames, 0)
            if (packageInfo != null &&
                (packageManager.queryIntentActivities(
                    Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
                        .setPackage(packageNames),
                    PackageManager.MATCH_ALL
                ).isNotEmpty() || packageManager.queryIntentActivities(
                    Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_LAUNCHER)
                        .setPackage(packageNames),
                    PackageManager.MATCH_ALL
                ).isNotEmpty())
            ) {
                val apps = AppListData(count, packageInfo, packageManager)
                if (storeList.containsKey(packageNames)) {
                    apps.appList = storeList.getValue(packageNames)
                } else {
                    val dummyList = AppList()
                    dummyList.packageName = packageNames
                    dummyList.row = appList.size
                    apps.appList = dummyList
                }
                appPackageList.plus(apps.packageName)
                return apps
            } else {
                appPackageList.minus(packageNames)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            return null
        }
        return null
    }

    @WorkerThread
    fun updateStoreAppListing(context: Context): MutableMap<String, AppList> {
        val storeAppList: MutableMap<String, AppList> = mutableMapOf()
        val gson = GsonBuilder().setPrettyPrinting().create()
//
        try {
            val inputStream: InputStream = context.assets.open("store_app_list.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonString = String(buffer, Charset.forName("UTF-8"))
            if (!jsonString.isNullOrEmpty()) {
                val example = gson.fromJson(jsonString, Example::class.java)
                example.appList.forEach {
                    storeAppList[it.packageName] = it
                }
            }
        } catch (e: Exception) {
        }
        return storeAppList
    }

}