package com.yyd.fjsd.filemanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.adapters.ApkAdapter;
import com.yyd.fjsd.filemanager.adapters.ZipAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ZipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZipFragment extends Fragment {

    private static final String ZIP = "zip";

    private ArrayList<String> zipList;
    private RecyclerView mRecyclerView;


    public ZipFragment() {
        // Required empty public constructor
    }

    public static ZipFragment newInstance(ArrayList<String> list) {
        ZipFragment fragment = new ZipFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ZIP, list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            zipList = getArguments().getStringArrayList(ZIP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zip, container, false);
        mRecyclerView = view.findViewById(R.id.zip);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        ZipAdapter adapter = new ZipAdapter(this.getContext(), zipList);
        mRecyclerView.setAdapter(adapter);

        return view;
    }

}
