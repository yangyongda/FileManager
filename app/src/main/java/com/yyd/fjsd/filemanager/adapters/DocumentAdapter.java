package com.yyd.fjsd.filemanager.adapters;

import android.content.Context;
import android.content.Intent;
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

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> documentList;

    public DocumentAdapter(Context mContext, ArrayList<String> documentList) {
        this.mContext = mContext;
        this.documentList = documentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.document_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String fileName = new File(documentList.get(position)).getName();
        if(fileName.endsWith(".txt"))
            holder.icon.setImageResource(R.drawable.txt);
        else if(fileName.endsWith(".pdf"))
            holder.icon.setImageResource(R.drawable.pdf);
        else if(fileName.endsWith(".doc") || fileName.endsWith(".docx"))
            holder.icon.setImageResource(R.drawable.doc);

        holder.name.setText(fileName);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent();
                mIntent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("file://" + documentList.get(position));
                if(fileName.endsWith(".txt"))
                    mIntent.setDataAndType(uri , "text/plain");
                else if(fileName.endsWith(".pdf"))
                    mIntent.setDataAndType(uri , "application/pdf");
                else if(fileName.endsWith(".doc"))
                    mIntent.setDataAndType(uri , "application/msword");
                else
                    mIntent.setDataAndType(uri , "text/plain");

                mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.document_icon);
            name = itemView.findViewById(R.id.document_name);
            cardView = itemView.findViewById(R.id.documentCardView);
        }
    }
}
