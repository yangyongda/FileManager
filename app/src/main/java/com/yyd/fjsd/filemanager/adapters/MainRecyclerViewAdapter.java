package com.yyd.fjsd.filemanager.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.activitys.MainActivity;
import com.yyd.fjsd.filemanager.asynctask.LoadFilterFileTask;
import com.yyd.fjsd.filemanager.bean.Type;
import com.yyd.fjsd.filemanager.fragment.LoadFragment;
import com.yyd.fjsd.filemanager.utils.TypeConstant;

import java.util.ArrayList;



public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Type> mTypes;
    private Context mContext;
    private Handler mHandler;
    private View view;

    public MainRecyclerViewAdapter(ArrayList<Type> types, Context context, Handler handler){
        mTypes = types;
        mContext = context;
        mHandler = handler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type__item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //填充数据
        Type type = mTypes.get(position);
        holder.icon.setImageResource(type.getIcon());
        holder.name.setText(type.getName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入加载页面，提高用户体验
                LoadFragment loadFragment = new LoadFragment();
                FragmentManager fragmentManager = ((MainActivity)mContext).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.primary_content, loadFragment);
                transaction.commit();
                switch (position){
                    case TypeConstant.PICTURE:
                        new LoadFilterFileTask(mContext, mHandler).execute(TypeConstant.PICTURE);
                        break;
                    case TypeConstant.MUSIC:
                        new LoadFilterFileTask(mContext, mHandler).execute(TypeConstant.MUSIC);
                        break;
                    case TypeConstant.VIDEO:
                        new LoadFilterFileTask(mContext, mHandler).execute(TypeConstant.VIDEO);
                        break;
                    case TypeConstant.DOCUMENT:
                        new LoadFilterFileTask(mContext, mHandler).execute(TypeConstant.DOCUMENT);
                        break;
                    case TypeConstant.APK:
                        new LoadFilterFileTask(mContext, mHandler).execute(TypeConstant.APK);
                        break;
                    case TypeConstant.ZIP:
                        new LoadFilterFileTask(mContext, mHandler).execute(TypeConstant.ZIP);
                        break;
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        //返回数据长度
        return mTypes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            cardView = itemView.findViewById(R.id.type_item);
        }
    }
}
