package com.yyd.fjsd.filemanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.yyd.fjsd.filemanager.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> imageList;

    public PictureAdapter(Context mContext, ArrayList<String> imageList) {
        this.mContext = mContext;
        this.imageList = imageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_item_layout, parent, false);
        PictureAdapter.ViewHolder holder = new PictureAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(mContext)
                .load(imageList.get(position))
                .centerCrop()
                .into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent();
                mIntent.setAction(android.content.Intent.ACTION_VIEW);
                Uri uri = Uri.parse("file://" + imageList.get(position));
                mIntent.setDataAndType(uri , "image/jpeg");
                mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.pictureCardView);

        }
    }
}
