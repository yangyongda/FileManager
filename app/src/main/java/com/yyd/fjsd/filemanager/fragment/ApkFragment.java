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
import com.yyd.fjsd.filemanager.adapters.DocumentAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ApkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApkFragment extends Fragment {

    private static final String APK = "apk";

    private ArrayList<String> apkList;
    private RecyclerView mRecyclerView;


    public ApkFragment() {
        // Required empty public constructor
    }


    public static ApkFragment newInstance(ArrayList<String> list) {
        ApkFragment fragment = new ApkFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(APK, list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            apkList = getArguments().getStringArrayList(APK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apk, container, false);
        mRecyclerView = view.findViewById(R.id.apk);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        ApkAdapter adapter = new ApkAdapter(this.getContext(), apkList);
        mRecyclerView.setAdapter(adapter);

        return view;
    }

}
