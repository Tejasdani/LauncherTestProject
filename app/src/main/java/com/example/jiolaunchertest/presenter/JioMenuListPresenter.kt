package com.example.jiolaunchertest.presenter

import android.view.View
import com.example.jiolaunchertest.dao.AppListData
import com.example.jiolaunchertest.databinding.ListItemAppTrayMenuBinding
import com.example.jiolaunchertest.listner.AppTrayListListener

class JioMenuListPresenter : AppTrayListListener.Presenter {
    override fun onMenuClick(app: AppListData) {
        app.packageName?.let {
            view.openAppActivity(app)
        }
    }


    override fun closeAppTray() {
        view.closeAppTrayPage()

    }

    override fun touchPosition(position: Int, view1: View) {
        view.getTouchPosition(position, view1)
    }

    override fun onMenuFocus(
        hasFocus: Boolean,
        app: AppListData,
        itemView: ListItemAppTrayMenuBinding
    ): Unit? = view.onMenuItemFocus(hasFocus, app, itemView)

    override fun updateDataList(packageName: String, ops: String) {

    }


    override fun arrangeList(by: String) {

    }

    override fun readyToLoadAppData() {
    }

    override lateinit var view: AppTrayListListener.View


}