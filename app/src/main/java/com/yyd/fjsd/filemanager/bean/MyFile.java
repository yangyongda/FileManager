package com.yyd.fjsd.filemanager.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class MyFile implements Parcelable {
    private int icon;
    private String fileName;
    private String path;
    private long size;
    private int isDirectory; // 1:文件夹 0:文件


    public MyFile(int icon, String fileName, String path, long size, int isDirectory) {
        this.icon = icon;
        this.fileName = fileName;
        this.path = path;
        this.size = size;
        this.isDirectory = isDirectory;
    }

    protected MyFile(Parcel in) {
        icon = in.readInt();
        fileName = in.readString();
        path = in.readString();
        size = in.readLong();
        isDirectory = in.readInt();
    }

    public static final Creator<MyFile> CREATOR = new Creator<MyFile>() {
        @Override
        public MyFile createFromParcel(Parcel in) {
            return new MyFile(in);
        }

        @Override
        public MyFile[] newArray(int size) {
            return new MyFile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(icon);
        parcel.writeString(fileName);
        parcel.writeString(path);
        parcel.writeLong(size);
        parcel.writeInt(isDirectory);
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getIsDirectory() {
        return isDirectory;
    }

    public void setIsDirectory(int isDirectory) {
        this.isDirectory = isDirectory;
    }
}
