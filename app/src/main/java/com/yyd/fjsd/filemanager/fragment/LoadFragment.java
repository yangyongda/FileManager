package com.yyd.fjsd.filemanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyd.fjsd.filemanager.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoadFragment extends Fragment {

    public LoadFragment() {
        // Required empty public constructor
    }


    public static LoadFragment newInstance(String param1, String param2) {
        LoadFragment fragment = new LoadFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_load, container, false);
    }

}
