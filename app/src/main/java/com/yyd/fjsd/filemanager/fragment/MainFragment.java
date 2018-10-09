package com.yyd.fjsd.filemanager.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.activitys.MainActivity;
import com.yyd.fjsd.filemanager.adapters.MainRecyclerViewAdapter;
import com.yyd.fjsd.filemanager.bean.Type;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ArrayList<Type> mTypes;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(ArrayList<Type> types) {
        MainFragment fragment = new MainFragment();
        //设置传递的参数
        Bundle args = new Bundle();
        args.putParcelableArrayList("types", types);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取参数，这个参数会传递给Adapter
        if (getArguments() != null) {
            mTypes = getArguments().getParcelableArrayList("types");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        //设置RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        MainRecyclerViewAdapter adapter = new MainRecyclerViewAdapter(mTypes, this.getActivity(), ((MainActivity)this.getActivity()).getHandler());
        mRecyclerView.setAdapter(adapter);
        return view;
    }


}
