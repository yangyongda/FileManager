package com.yyd.fjsd.filemanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.adapters.FileListAdapter;
import com.yyd.fjsd.filemanager.bean.MyFile;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FileListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileListFragment extends Fragment {

    private static final String FILE_LIST = "file_list";

    private RecyclerView mRecyclerView;
    private ArrayList<MyFile> mFilelist;


    public FileListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param files Parameter 1.
     * @return A new instance of fragment FileListFragment.
     */
    public static FileListFragment newInstance(ArrayList<MyFile> files) {
        FileListFragment fragment = new FileListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(FILE_LIST, files);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFilelist = getArguments().getParcelableArrayList(FILE_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file_list, container, false);
        mRecyclerView = view.findViewById(R.id.fileList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        FileListAdapter adapter = new FileListAdapter(mFilelist, this.getContext());
        mRecyclerView.setAdapter(adapter);
        return view;
    }


}
