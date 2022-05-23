package com.example.jiolaunchertest.presenter

import android.os.Bundle
import android.util.Log
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.example.jiolaunchertest.MyApplication
import com.example.jiolaunchertest.Utils
import com.example.jiolaunchertest.dao.*
import com.example.jiolaunchertest.listner.AppListContractListener.Companion.PACKAGES_ARGUMENT
import com.example.jiolaunchertest.databinding.ListItemAppTrayMenuBinding
import com.example.jiolaunchertest.listner.AppListContractListener

class AppListPresenter(
    private val loader: AbstractAsyncLoader<List<AppListData>>,
    private val loaderManager: LoaderManager
) :
    LoaderManager.LoaderCallbacks<List<AppListData>>, AppListContractListener.Presenter {

    override lateinit var view: AppListContractListener.View
    private var packageNames: List<String> = emptyList()

    companion object {
        private var appData: MutableList<AppListData> = mutableListOf()
    }

    private var storeAppList: MutableMap<String, AppList> = mutableMapOf()

    override fun initialize(bundle: Bundle) {
        Log.d("initialize AppListPresenter", "AppListPresenter:initialize")
        packageNames =
            bundle.getStringArrayList(PACKAGES_ARGUMENT) ?: throw IllegalArgumentException()
        startLoadingData()
    }

    private fun startLoadingData() {
        Log.d("startLoadingData", "AppListPresenter:startLoadingData")
        loaderManager.initLoader(AppListFromPackageNamesLoader.ID, null, this)
    }

    override fun onCreateLoader(bunid: Int, args: Bundle?): Loader<List<AppListData>> {
        return loader
    }

    override fun onLoadFinished(loader: Loader<List<AppListData>>, data: List<AppListData>) {
        Log.d("onLoadFinished", "AppListPresenter:onLoadFinished")
        //view.setUpViews()
        appData = data.toMutableList()
        //  view.hideLoading()

        storeAppList = Utils.appList
        for (appListData: AppListData in appData) {
            Utils.shortCutDrawableList.add(
                IconData(
                    appListData.applicationName,
                    appListData.icon
                )
            )
        }
        if (data.isEmpty()) {
            view.nothingToDisplay()
            Log.e("AppList :", "dataEmpty")
        } else {
            Log.e("AppList :", "data")
            Utils.menuList = appData
            view.showAppList(appData)
        }
        loaderManager.destroyLoader(AppListFromPackageNamesLoader.ID)
    }

    override fun onLoaderReset(loader: Loader<List<AppListData>>) {}

    override fun itemCount(): Int = appData.size

    override fun onAppClick(app: AppListData) =
        app.packageName?.let { view.openAppActivity(packageName = it) }

    override fun onAppFocus(
        hasFocus: Boolean,
        app: AppListData,
        itemView: ListItemAppTrayMenuBinding
    ) = view.onAppItemFocus(hasFocus, app, itemView)

    override fun updateDataList(packageName: String, ops: String, isFromAppTray: Boolean) {
        val appDataItem =
            AppBasicDataService(MyApplication.context.packageManager).getAppListData(
                appData,
                storeAppList,
                appData.size,
                packageName
            )
        val isItemExistInList = appData.filter { it.packageName == packageName }
        Log.d(
            "AppListPresenter ",
            "package-app-exist: $isItemExistInList , size: ${isItemExistInList.size}, store-pos: ${if (appDataItem != null) appDataItem.appList.row else -1}}"
        )
        if (appDataItem != null && ops == "add" && isItemExistInList.isEmpty()) {
            Log.d(
                "AppListPresenter",
                "package-installed-${packageName} :- ${appDataItem.applicationName}"
            )
            var position = appDataItem.appList.row - 1
            if (appData.size <= appDataItem.appList.row)
                position = appData.size
            appData.add(position, appDataItem)
            Utils.shortCutDrawableList.add(
                IconData(
                    appDataItem.applicationName,
                    appDataItem.icon
                )
            )
            view.addDataInList(position, appData.size, isFromAppTray)
        } else if (ops == "remove" && isItemExistInList.isNotEmpty()) {
            val item = appData.find { it.packageName == packageName }
            appData.remove(item)
            view.removeDataFromList(
                0,
                item!!.packageName,
                item.applicationName,
                Utils.menuList.size,
                isFromAppTray
            )
        } else if (ops == "replace" && isItemExistInList.isNotEmpty()) {
            Log.d("AppListPresenter" ,"package-replace-${packageName} :- ${appDataItem!!.applicationName}")
            val item = appData.find { it.packageName == packageName }
            val position = appData.indexOf(item) - 1
            view.refreshDataFromList(position, appData.size, isFromAppTray)
        }
    }

    override fun arrangeList(by: String) {
        if (by == "name") {
            appData = appData.sortedWith(AppSortByNameComparator.INSTANCE).toMutableList()
            view.showAppList(appData)
        }
    }


    override fun onBindViewOnPosition(position: Int, holder: AppListContractListener.ItemView) {
        return holder.bind(appData[position])
    }
}