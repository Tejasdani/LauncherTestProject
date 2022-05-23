package com.example.jiolaunchertest.listner

import android.os.Bundle
import com.example.jiolaunchertest.dao.AppListData
import com.example.jiolaunchertest.databinding.ListItemAppTrayMenuBinding

interface AppListContractListener {
    interface View {
        fun setUpViews(){}

        fun hideLoading(){}

        fun nothingToDisplay(){}

        fun showAppList(appList:MutableList<AppListData>)

        fun addDataInList(position: Int, dataCount: Int,isFromAppTray: Boolean)

        fun removeDataFromList(position: Int,packageName: String,itemName:String, dataCount: Int,isFromAppTray: Boolean)

        fun refreshDataFromList(position: Int, dataCount: Int,isFromAppTray: Boolean)

        fun openAppActivity(packageName: String){}

        fun onAppItemFocus(
            hasFocus: Boolean,
            app: AppListData,
            itemView: ListItemAppTrayMenuBinding
        ){}
    }

    interface ItemView {
        fun bind(appData: AppListData)
    }

    interface Presenter : BasePresenterListener<View>, ListPresenterListener<ItemView> {
        fun initialize(bundle: Bundle)
        fun onAppClick(app: AppListData): Unit?
        fun onAppFocus(
            hasFocus: Boolean,
            app: AppListData,
            itemView: ListItemAppTrayMenuBinding
        ): Unit?

        fun updateDataList(packageName: String, ops: String,isFromAppTray:Boolean)
        fun arrangeList(by: String)
    }

    companion object {
        const val PACKAGES_ARGUMENT = "args_package_names"
    }
}