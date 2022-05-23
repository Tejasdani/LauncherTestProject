package com.example.jiolaunchertest.presenter

import android.content.Context
import android.util.Log
import com.example.jiolaunchertest.Utils.appList
import com.example.jiolaunchertest.dao.AbstractAsyncLoader
import com.example.jiolaunchertest.dao.AppBasicDataService
import com.example.jiolaunchertest.dao.AppListData

class AppListFromPackageNamesLoader(context: Context) :
    AbstractAsyncLoader<List<AppListData>>(context) {

    private val appBasicDataService = AppBasicDataService(getContext().packageManager)

    override fun loadInBackground(): List<AppListData> {
        Log.d("AppListFromPackageNamesLoader","loadInBackground")
        appList = appBasicDataService.updateStoreAppListing(context)
        return appBasicDataService.getAll(appList)
    }

    companion object {
        const val ID = 5
    }
}
