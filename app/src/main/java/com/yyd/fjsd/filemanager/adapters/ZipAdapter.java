package com.yyd.fjsd.filemanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyd.fjsd.filemanager.R;

import java.io.File;
import java.util.ArrayList;

public class ZipAdapter extends RecyclerView.Adapter<ZipAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> zipList;

    public ZipAdapter(Context mContext, ArrayList<String> zipList) {
        this.mContext = mContext;
        this.zipList = zipList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.zip_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(new File(zipList.get(position)).getName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent();
                mIntent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("file://" + zipList.get(position));
                mIntent.setDataAndType(uri , "application/zip");
                mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return zipList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.zip_name);
            cardView = itemView.findViewById(R.id.zipCardView);
        }
    }
}
