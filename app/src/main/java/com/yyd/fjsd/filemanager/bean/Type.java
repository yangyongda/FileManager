package com.yyd.fjsd.filemanager.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Type implements Parcelable{
    private int icon;
    private String name;

    public Type(int icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    protected Type(Parcel in) {
        icon = in.readInt();
        name = in.readString();
    }

    public static final Creator<Type> CREATOR = new Creator<Type>() {
        @Override
        public Type createFromParcel(Parcel in) {
            return new Type(in);
        }

        @Override
        public Type[] newArray(int size) {
            return new Type[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(icon);
        parcel.writeString(name);
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
