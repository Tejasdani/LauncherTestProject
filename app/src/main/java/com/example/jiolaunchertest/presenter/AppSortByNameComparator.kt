package com.example.jiolaunchertest.presenter

import com.example.jiolaunchertest.dao.AppListData
import java.util.*

class AppSortByNameComparator : Comparator<AppListData> {

    override fun compare(object1: AppListData, object2: AppListData): Int {
        return object1.applicationName[0] - object2.applicationName[0]
    }

    companion object {
        val INSTANCE = AppSortByNameComparator()
    }
}
