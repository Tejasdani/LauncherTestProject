package com.example.jiolaunchertest.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jiolaunchertest.R
import com.example.jiolaunchertest.Utils.isSettingsOpen
import com.example.jiolaunchertest.dao.AppListData
import com.example.jiolaunchertest.listner.AppTrayListListener
import com.github.zawadz88.materialpopupmenu.popupMenu
import java.util.*


class AppListRecyclerAdapter(
    private var context: Context,
    private var menuList: MutableList<AppListData>,
    val presenter: AppTrayListListener.Presenter
) : RecyclerView.Adapter<AppListRecyclerAdapter.ViewHolder>(), Filterable {

    var appList: MutableList<AppListData> = mutableListOf()
    private val SETTING_PKG_NAME = "com.android.settings"

    init {

        appList = menuList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.list_item_app_tray_menu, parent, false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.appName.text = appList[position].applicationName
        holder.appBanner.setImageDrawable(appList[position].icon)

        holder.itemView.tag = appList[position]
        holder.itemView.setOnClickListener {
            presenter.onMenuClick(appList[position])
        }
        holder.itemView.setOnClickListener {
            onHandleMenu(it, holder.itemView.tag as AppListData)
        }
    }

    override fun getItemCount(): Int {
        return appList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var appBanner: ImageView = view.findViewById(R.id.package_banner) as ImageView
        var appName: TextView = view.findViewById(R.id.application_name) as TextView

    }


    fun refreshData(newList: MutableList<AppListData>) {
        menuList.clear()
        menuList.addAll(newList)
        notifyDataSetChanged()

    }

    private fun onHandleMenu(view: View, appData: AppListData) {
        if (appData.packageName == SETTING_PKG_NAME) {
            isSettingsOpen = true
        }else{
            val popupMenu = popupMenu {
                dropdownGravity = Gravity.CENTER
                dropDownVerticalOffset = view.height / 2
                dropDownHorizontalOffset = view.width / 2
                section {
                    item {
                        label = "UnInstall"
                        callback = {
                            try {
                                Log.d(" AppListRVAdapter ", "PKGNAME " + appData.packageName)
                                var packageURI = Uri.parse("package:" + appData.packageName)
                                var uninstallIntent =
                                    Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageURI)
                                uninstallIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
                                view.context.startActivity(uninstallIntent)

                            } catch (e: Exception) {
                                Log.e(
                                    "AppListRecyclerAdapter",
                                    "app-un-install Exception ${e.message} "
                                )
                            }

                        }
                    }

                }
            }
            popupMenu.show(view.context, view)
        }


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                appList = if (charSearch.isEmpty()) {
                    menuList
                } else {
                    val resultList: MutableList<AppListData> = mutableListOf()

                    for (row in menuList) {
                        if (row.applicationName.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = appList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                appList = results?.values as MutableList<AppListData>
                notifyDataSetChanged()
            }
        }
    }
}