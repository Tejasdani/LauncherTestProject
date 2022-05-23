package com.example.jiolaunchertest.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Example implements Parcelable {

    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("appList")
    @Expose
    private List<AppList> appList = null;

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<AppList> getAppList() {
        return appList;
    }

    public void setAppList(List<AppList> appList) {
        this.appList = appList;
    }


    protected Example(Parcel in) {
        updatedAt = in.readString();
        if (in.readByte() == 0x01) {
            appList = new ArrayList<AppList>();
            in.readList(appList, AppList.class.getClassLoader());
        } else {
            appList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(updatedAt);
        if (appList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(appList);
        }
    }

    @SuppressWarnings("unused")
    public static final Creator<Example> CREATOR = new Creator<Example>() {
        @Override
        public Example createFromParcel(Parcel in) {
            return new Example(in);
        }

        @Override
        public Example[] newArray(int size) {
            return new Example[size];
        }
    };
}
