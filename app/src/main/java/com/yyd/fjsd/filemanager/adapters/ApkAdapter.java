package com.yyd.fjsd.filemanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyd.fjsd.filemanager.R;

import java.io.File;
import java.util.ArrayList;

public class ApkAdapter extends RecyclerView.Adapter<ApkAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> apkList;

    public ApkAdapter(Context mContext, ArrayList<String> apkList) {
        this.mContext = mContext;
        this.apkList = apkList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.apk_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(new File(apkList.get(position)).getName());
        holder.icon.setImageDrawable(getApkIcon(mContext, apkList.get(position)));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent();
                mIntent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("file://" + apkList.get(position));
                mIntent.setDataAndType(uri , "application/vnd.android.package-archive");
                mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return apkList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.apk_icon);
            name = itemView.findViewById(R.id.apk_name);
            cardView = itemView.findViewById(R.id.apkCardView);
        }
    }

    public static Drawable getApkIcon(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            return appInfo.loadIcon(pm);
        }
        return null;
    }


}
