package com.example.jiolaunchertest.dao;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class AppListData implements Parcelable {

    private String packageName;
    private String applicationName;
    private int version;
    private boolean isSystemApp;

    private int position;

    private PackageManager packageManager;
    private ApplicationInfo applicationInfo;
    private boolean isLeanback;
    private AppList appList;

    public AppListData(int position, PackageInfo packageInfo, PackageManager packageManager) {
        this.position = position;
        this.packageManager = packageManager;
        this.applicationInfo = packageInfo.applicationInfo;
        this.version = packageInfo.versionCode;
        this.isSystemApp = (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    public AppListData() {
    }

    protected AppListData(Parcel in) {
        this.packageName = in.readString();
        this.applicationName = in.readString();
        this.version = in.readInt();
        this.position = in.readInt();
        this.appList = in.readParcelable(AppList.class.getClassLoader());
    }

    public void setAppList(AppList appList) {
        this.appList = appList;
    }

    public AppList getAppList() {
        return appList;
    }

    public int getPosition() {
        return position;
    }

    public String getApplicationName() {
        if (applicationName == null) {
            CharSequence label = applicationInfo.loadLabel(packageManager);
            applicationName = label != null ? label.toString() : applicationInfo.packageName;
        }
        return applicationName;
    }

    //public Drawable getBanner() {
    //    return applicationInfo.loadBanner(packageManager);
    //}
    public Drawable getBanner() {
        Intent intent = packageManager.getLeanbackLaunchIntentForPackage(applicationInfo.packageName);
        if (intent == null)
            intent = packageManager.getLaunchIntentForPackage(applicationInfo.packageName);
        assert intent != null;
        ResolveInfo resolveInfo = packageManager.resolveActivity(intent, 0);
        assert resolveInfo != null;
        if (resolveInfo.activityInfo.loadLogo(packageManager) == null) {
            return resolveInfo.activityInfo.loadIcon(packageManager);
        } else {
            return resolveInfo.activityInfo.loadLogo(packageManager);
        }
    }

    public Drawable getUnbadgedIcon() {
        return applicationInfo.loadUnbadgedIcon(packageManager);
    }

    public Drawable getLogo() {
        return applicationInfo.loadLogo(packageManager);
    }

    public boolean isLeanbackCategoryBased() {
        return isLeanback;
    }

    public String getPackageName() {
        return applicationInfo.packageName;
    }

    public int getVersion() {
        return version;
    }

    public Drawable getIcon() {
        return applicationInfo.loadIcon(packageManager);
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppListData that = (AppListData) o;
        if (version != that.version) return false;
        if (position != that.position) return false;
        if (isSystemApp != that.isSystemApp) return false;
        if (!Objects.equals(packageName, that.packageName))
            return false;
        if (!Objects.equals(applicationName, that.applicationName))
            return false;
        if (!Objects.equals(packageManager, that.packageManager))
            return false;
        if (!Objects.equals(appList, that.appList))
            return false;
        return Objects.equals(applicationInfo, that.applicationInfo);
    }

    @Override
    public int hashCode() {
        int result = packageName != null ? packageName.hashCode() : 0;
        result = 31 * result + (applicationName != null ? applicationName.hashCode() : 0);
        result = 31 * result + version;
        result = 31 * result + position;
        result = 31 * result + (isSystemApp ? 1 : 0);
        result = 31 * result + (packageManager != null ? packageManager.hashCode() : 0);
        result = 31 * result + (applicationInfo != null ? applicationInfo.hashCode() : 0);
        result = 31 * result + (appList != null ? appList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return applicationName + " " + packageName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeString(this.applicationName);
        dest.writeInt(this.version);
        dest.writeInt(this.position);
        dest.writeParcelable(this.appList, flags);
    }

    public static final Creator<AppListData> CREATOR = new Creator<AppListData>() {
        @Override
        public AppListData createFromParcel(Parcel source) {
            return new AppListData(source);
        }

        @Override
        public AppListData[] newArray(int size) {
            return new AppListData[size];
        }
    };
}
