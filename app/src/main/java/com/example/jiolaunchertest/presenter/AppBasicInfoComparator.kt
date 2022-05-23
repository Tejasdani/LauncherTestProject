package com.example.jiolaunchertest.presenter

import com.example.jiolaunchertest.dao.AppListData
import java.util.*

class AppBasicInfoComparator : Comparator<AppListData> {

    override fun compare(object1: AppListData, object2: AppListData): Int {
        return object1.appList.row - object2.appList.row
    }

    companion object {
        val INSTANCE = AppBasicInfoComparator()
    }
}
