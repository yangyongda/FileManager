package com.yyd.fjsd.filemanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.adapters.MusicAdapter;
import com.yyd.fjsd.filemanager.adapters.VideoAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {

    private static final String VIDEO = "video";

    private ArrayList<String> videoList;
    private RecyclerView mRecyclerView;


    public VideoFragment() {
        // Required empty public constructor
    }


    public static VideoFragment newInstance(ArrayList<String> list) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(VIDEO, list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoList = getArguments().getStringArrayList(VIDEO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        mRecyclerView = view.findViewById(R.id.video);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VideoAdapter adapter = new VideoAdapter(this.getContext(), videoList);
        mRecyclerView.setAdapter(adapter);

        return view;
    }

}
