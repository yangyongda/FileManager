package com.yyd.fjsd.filemanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.adapters.DocumentAdapter;
import com.yyd.fjsd.filemanager.adapters.VideoAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DocumentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocumentFragment extends Fragment {

    private static final String DOCUMENT = "document";

    private ArrayList<String> documentList;
    private RecyclerView mRecyclerView;


    public DocumentFragment() {
        // Required empty public constructor
    }


    public static DocumentFragment newInstance(ArrayList<String> list) {
        DocumentFragment fragment = new DocumentFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(DOCUMENT, list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            documentList = getArguments().getStringArrayList(DOCUMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_document, container, false);
        mRecyclerView = view.findViewById(R.id.document);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        DocumentAdapter adapter = new DocumentAdapter(this.getContext(), documentList);
        mRecyclerView.setAdapter(adapter);

        return view;
    }

}
