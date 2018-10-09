package com.yyd.fjsd.filemanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.adapters.PictureAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PictureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PictureFragment extends Fragment {

    private static final String PICTURE = "picture";

    private ArrayList<String> pictureList;
    private RecyclerView mRecyclerView;


    public PictureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param list picture path list.
     * @return A new instance of fragment PictureFragment.
     */
    public static PictureFragment newInstance(ArrayList<String> list) {
        PictureFragment fragment = new PictureFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(PICTURE, list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pictureList = getArguments().getStringArrayList(PICTURE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        mRecyclerView = view.findViewById(R.id.picture);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        PictureAdapter adapter = new PictureAdapter(this.getContext(), pictureList);
        mRecyclerView.setAdapter(adapter);

        return view;
    }

}
