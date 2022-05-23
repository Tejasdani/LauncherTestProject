package com.example.jiolaunchertest.dao;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppList implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("appName")
    @Expose
    private String appName;
    @SerializedName("packageName")
    @Expose
    private String packageName;
    @SerializedName("row")
    @Expose
    private Integer row;
    @SerializedName("promoVideoUrl")
    @Expose
    private String promoVideoUrl;
    @SerializedName("promoBannerUrl")
    @Expose
    private String promoBannerUrl;
    @SerializedName("uninstallOption")
    @Expose
    private Integer uninstallOption;
    @SerializedName("promoNotification")
    @Expose
    private Integer promoNotification;
    @SerializedName("movable")
    @Expose
    private Integer movable;
    @SerializedName("clearDataOption")
    @Expose
    private Integer clearDataOption;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public String getPromoVideoUrl() {
        return promoVideoUrl;
    }

    public void setPromoVideoUrl(String promoVideoUrl) {
        this.promoVideoUrl = promoVideoUrl;
    }

    public String getPromoBannerUrl() {
        return promoBannerUrl;
    }

    public void setPromoBannerUrl(String promoBannerUrl) {
        this.promoBannerUrl = promoBannerUrl;
    }

    public Integer getUninstallOption() {
        return uninstallOption;
    }

    public void setUninstallOption(Integer uninstallOption) {
        this.uninstallOption = uninstallOption;
    }

    public Integer getPromoNotification() {
        return promoNotification;
    }

    public void setPromoNotification(Integer promoNotification) {
        this.promoNotification = promoNotification;
    }

    public Integer getMovable() {
        return movable;
    }

    public void setMovable(Integer movable) {
        this.movable = movable;
    }

    public Integer getClearDataOption() {
        return clearDataOption;
    }

    public void setClearDataOption(Integer clearDataOption) {
        this.clearDataOption = clearDataOption;
    }
    public AppList() {

    }

    protected AppList(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        appName = in.readString();
        packageName = in.readString();
        row = in.readByte() == 0x00 ? null : in.readInt();
        promoVideoUrl = in.readString();
        promoBannerUrl = in.readString();
        uninstallOption = in.readByte() == 0x00 ? null : in.readInt();
        promoNotification = in.readByte() == 0x00 ? null : in.readInt();
        movable = in.readByte() == 0x00 ? null : in.readInt();
        clearDataOption = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(appName);
        dest.writeString(packageName);
        if (row == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(row);
        }
        dest.writeString(promoVideoUrl);
        dest.writeString(promoBannerUrl);
        if (uninstallOption == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(uninstallOption);
        }
        if (promoNotification == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(promoNotification);
        }
        if (movable == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(movable);
        }
        if (clearDataOption == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(clearDataOption);
        }
    }

    @SuppressWarnings("unused")
    public static final Creator<AppList> CREATOR = new Creator<AppList>() {
        @Override
        public AppList createFromParcel(Parcel in) {
            return new AppList(in);
        }

        @Override
        public AppList[] newArray(int size) {
            return new AppList[size];
        }
    };
}
