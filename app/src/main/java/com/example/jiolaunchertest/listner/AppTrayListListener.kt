package com.example.jiolaunchertest.listner


import com.example.jiolaunchertest.dao.AppListData
import com.example.jiolaunchertest.databinding.ListItemAppTrayMenuBinding

interface AppTrayListListener {
    interface View {
        fun closeAppTrayPage()
        fun getTouchPosition(position: Int,view:android.view.View)
        fun openAppActivity(appListData: AppListData)
        fun onMenuItemFocus(
            hasFocus: Boolean,
            app: AppListData,
            itemView: ListItemAppTrayMenuBinding
        )
    }

    interface Presenter : BasePresenterListener<View> {
        fun onMenuClick(app: AppListData)
        fun closeAppTray()
        fun touchPosition(position: Int,view:android.view.View)
        fun onMenuFocus(
            hasFocus: Boolean,
            app: AppListData,
            itemView: ListItemAppTrayMenuBinding
        ): Unit?

        fun updateDataList(packageName: String, ops: String)
        fun arrangeList(by: String)

        fun readyToLoadAppData()
    }
}