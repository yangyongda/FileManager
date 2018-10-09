package com.yyd.fjsd.filemanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.adapters.MusicAdapter;
import com.yyd.fjsd.filemanager.adapters.PictureAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicFragment extends Fragment {

    private static final String MUSIC = "music";

    private ArrayList<String> musicList;
    private RecyclerView mRecyclerView;


    public MusicFragment() {
        // Required empty public constructor
    }

    public static MusicFragment newInstance(ArrayList<String> list) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(MUSIC, list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            musicList = getArguments().getStringArrayList(MUSIC);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        mRecyclerView = view.findViewById(R.id.music);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        MusicAdapter adapter = new MusicAdapter(this.getContext(), musicList);
        mRecyclerView.setAdapter(adapter);

        return view;
    }

}
