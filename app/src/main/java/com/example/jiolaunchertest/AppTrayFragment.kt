package com.example.jiolaunchertest

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jiolaunchertest.Utils.isAppOpened
import com.example.jiolaunchertest.Utils.isJioMenuOpen
import com.example.jiolaunchertest.adapter.AppListRecyclerAdapter
import com.example.jiolaunchertest.dao.AppListData
import com.example.jiolaunchertest.databinding.ListItemAppTrayMenuBinding
import com.example.jiolaunchertest.listner.AppListContractListener
import com.example.jiolaunchertest.listner.AppTrayListListener
import com.example.jiolaunchertest.listner.DesktopReceiverListener
import com.example.jiolaunchertest.presenter.AppListFromPackageNamesLoader
import com.example.jiolaunchertest.presenter.AppListPresenter
import com.example.jiolaunchertest.presenter.JioMenuListPresenter
import kotlinx.android.synthetic.main.fragment_app_tray.*


//https://www.android-examples.com/get-list-installed-apps-package-name-icons/

class AppTrayFragment : Fragment(), AppListContractListener.View, AppTrayListListener.View {
    private var TAG = "AppTrayFragment "

    private lateinit var mAppTrayPresenter: AppTrayListListener.Presenter
    private lateinit var mAppListRecyclerAdapter: AppListRecyclerAdapter
    private var mDesktopReceiver = DesktopReceiver()
    private var mIsOpen: Boolean = false
    private var spanCount: Int = 0
    private lateinit var mAppListPresenter: AppListContractListener.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAppListPresenter = AppListPresenter(
            AppListFromPackageNamesLoader(requireContext()),
            LoaderManager.getInstance(this)
        )
        mAppTrayPresenter = JioMenuListPresenter()

        retainInstance = true
        isJioMenuOpen = true

    }

    companion object {
        fun newInstance(): AppTrayFragment {
            return AppTrayFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_app_tray, container, false)
    }


    @SuppressLint("WrongConstant", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "AppTray-fragment:onViewCreated")
        arguments = Bundle().apply {
            putStringArrayList(Utils.PACKAGES_ARGUMENT, ArrayList())
        }
        mAppTrayPresenter.view = this
        mAppListPresenter.view = this

        tvAllApps.visibility = View.VISIBLE

        mIsOpen = true
        broadCastReceiverMethod()

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 6
        } else {
            spanCount = 4
        }

        val allAppslayoutManager =
            GridLayoutManager(requireContext()!!, spanCount, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_app_list!!.layoutManager = allAppslayoutManager

        val recentAppsLayoutManager =
            GridLayoutManager(requireContext()!!, spanCount, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_recent_list!!.layoutManager = recentAppsLayoutManager

        setAppList()

        etSearchApps.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mAppListRecyclerAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }

    override fun onResume() {
        super.onResume()
        Utils.registerPackageListener(activity!!, mDesktopReceiver)

    }

    private fun sendDataWithBroadCast(isApplicationOpen: Boolean) {
        val intent = Intent()
        intent.action = getString(R.string.closed_by_launcher)
        requireActivity()!!.sendBroadcast(intent)
//        activity!!.finish()
    }

    private fun broadCastReceiverMethod() {
        mDesktopReceiver.setOnDesktopReceiverListener(object : DesktopReceiverListener {
            override fun desktopResult(context: Context?, intent: Intent?) {
                Log.d("broadCastReceiverMethod ", "package-receiver: ${intent!!.action}")
                if (intent?.action === Intent.ACTION_PACKAGE_ADDED &&
                    intent?.data != null && intent.data!!.encodedSchemeSpecificPart != null
                ) {
                    Log.d(
                        "broadCastReceiverMethod ",
                        "package-added---:${intent.data!!.encodedSchemeSpecificPart}"
                    )
                    mAppListPresenter.updateDataList(
                        intent.data!!.schemeSpecificPart,
                        "add",
                        isFromAppTray = true
                    )

                } else if (intent.action === Intent.ACTION_PACKAGE_REMOVED &&
                    intent.data != null && intent.data!!.schemeSpecificPart != null
                ) {
                    mAppListPresenter.updateDataList(
                        intent?.data!!.schemeSpecificPart,
                        "remove",
                        isFromAppTray = true
                    )
                    Log.d(
                        "broadCastReceiverMethod ",
                        "package-removed---:${intent.data!!.schemeSpecificPart}"
                    )
                }
            }

        })

    }

    override fun showAppList(appList: MutableList<AppListData>) {
        Utils.menuList = appList
        mAppListRecyclerAdapter.refreshData(Utils.menuList)
    }

    private fun setAppList() {
        mAppListRecyclerAdapter =
            AppListRecyclerAdapter(
                requireContext()!!,
                Utils.menuList,
                mAppTrayPresenter
            );
        recycler_view_app_list.adapter = mAppListRecyclerAdapter
        mAppListRecyclerAdapter.notifyDataSetChanged()
        Handler().postDelayed({ mAppListPresenter.initialize(arguments!!) }, 2000)
    }


    override fun addDataInList(position: Int, dataCount: Int, isFromAppTray: Boolean) {
        mAppListRecyclerAdapter.refreshData(Utils.menuList)
    }

    override fun removeDataFromList(
        position: Int,
        packageName: String,
        itemName: String,
        dataCount: Int,
        isFromAppTray: Boolean
    ) {
        if (isFromAppTray) {
            mAppListRecyclerAdapter.refreshData(Utils.menuList)

        }
    }


    override fun refreshDataFromList(position: Int, dataCount: Int, isFromAppTray: Boolean) {

    }

    override fun closeAppTrayPage() {
        sendDataWithBroadCast(isApplicationOpen = false)
    }

    override fun getTouchPosition(position: Int, view: View) {
        TODO("Not yet implemented")
    }

    override fun openAppActivity(appListData: AppListData) {
        isAppOpened = true
        Utils.launchApplication(appListData.packageName)
        //  sendDataWithBroadCast(isApplicationOpen = true)

    }

    override fun onMenuItemFocus(
        hasFocus: Boolean,
        app: AppListData,
        itemView: ListItemAppTrayMenuBinding
    ) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        Utils.unregisterPackageListener(activity!!, mDesktopReceiver)
    }

}
