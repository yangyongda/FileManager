package com.yyd.fjsd.filemanager.adapters;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyd.fjsd.filemanager.MyApplication;
import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.activitys.MainActivity;
import com.yyd.fjsd.filemanager.bean.MyFile;
import com.yyd.fjsd.filemanager.fragment.FileListFragment;
import com.yyd.fjsd.filemanager.utils.ActionModeCallback;
import com.yyd.fjsd.filemanager.utils.FileUtil;
import com.yyd.fjsd.filemanager.utils.RunStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {

    private ArrayList<MyFile> mMyFils;
    private Context mContext;


    public FileListAdapter(ArrayList<MyFile> myFiles, Context context) {
        mMyFils = myFiles;
        mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filelist_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MyFile myFile = mMyFils.get(position);
        holder.icon.setImageResource(myFile.getIcon());
        holder.fileNmae.setText(myFile.getFileName());

        if (MyApplication.getInstance().runStatus == RunStatus.NORMAL_MODE) {
            MyApplication.getInstance().getSelectedList().clear();
        }

        //设置被选中的背景颜色，不加这段代码会出现滑动时其他位置的item被选中(背景为灰)
        if (MyApplication.getInstance().getSelectedList().containsKey(position) && MyApplication.getInstance().runStatus == RunStatus.SELECT_MODE) {
            holder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.grep));
        } else {
            holder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        if (myFile.getIsDirectory() == 0) {
            //文件
            holder.size.setText(FileUtil.sizeTransform(myFile.getSize()));
            holder.size.setVisibility(View.VISIBLE);
            holder.next.setVisibility(View.GONE);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (MyApplication.getInstance().runStatus) {
                        case RunStatus.NORMAL_MODE:
                            FileUtil.openFile(mContext, myFile.getPath());
                            break;
                        case RunStatus.SELECT_MODE:
                            if (MyApplication.getInstance().getSelectedList().containsKey(position)) {
                                view.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                                MyApplication.getInstance().getSelectedList().remove(position);
                            } else {
                                view.setBackgroundColor(mContext.getResources().getColor(R.color.grep));
                                MyApplication.getInstance().getSelectedList().put(position, myFile.getPath());
                            }
                            break;

                    }

                }
            });
        } else {
            //文件夹
            holder.size.setVisibility(View.GONE);
            holder.next.setVisibility(View.VISIBLE);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (MyApplication.getInstance().runStatus) {
                        case RunStatus.NORMAL_MODE:
                        case RunStatus.COPY_MODE:
                        case RunStatus.CUT_MODE:
                            MyApplication.getInstance().prePath.add(new File(myFile.getPath()).getParent()); //保存父路径
                            MyApplication.getInstance().prePosition.add(position);
                            MyApplication.getInstance().currPath = myFile.getPath();    //保存当前路径
                            List<File> list = FileUtil.getFileList(myFile.getPath());
                            ArrayList<MyFile> myFileList = FileUtil.FileToMyFile(list);
                            //FileListFragment
                            FileListFragment fileListFragment = FileListFragment.newInstance(myFileList, 0);
                            FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.primary_content, fileListFragment);
                            transaction.commit();
                            break;
                        case RunStatus.SELECT_MODE:
                            if (MyApplication.getInstance().getSelectedList().containsKey(position)) {
                                view.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                                MyApplication.getInstance().getSelectedList().remove(position);
                            } else {
                                view.setBackgroundColor(mContext.getResources().getColor(R.color.grep));
                                MyApplication.getInstance().getSelectedList().put(position, myFile.getPath());
                            }
                            break;

                    }

                }
            });
        }
        //长按item进入选择模式
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (MyApplication.getInstance().runStatus == RunStatus.NORMAL_MODE) {
                    MyApplication.getInstance().runStatus = RunStatus.SELECT_MODE;  //进入选择模式
                    view.setBackgroundColor(mContext.getResources().getColor(R.color.grep));
                    MyApplication.getInstance().getSelectedList().put(position, myFile.getPath()); //添加到选择列表
                    ((AppCompatActivity) mContext).startSupportActionMode(new ActionModeCallback(mContext, FileListAdapter.this));

                }
                return true;
            }
        });
        //Log.v("yang", MyApplication.getInstance().runStatus + " ");
        if (MyApplication.getInstance().runStatus == RunStatus.COPY_MODE || MyApplication.getInstance().runStatus == RunStatus.CUT_MODE) {
            ((MainActivity)mContext).mPaste_Layout.setVisibility(View.VISIBLE);
        } else {
            ((MainActivity)mContext).mPaste_Layout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mMyFils.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView fileNmae;
        TextView size;
        ImageView next;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            fileNmae = itemView.findViewById(R.id.fileName);
            size = itemView.findViewById(R.id.size);
            next = itemView.findViewById(R.id.next);
            cardView = itemView.findViewById(R.id.filelist_item);
        }
    }
}
