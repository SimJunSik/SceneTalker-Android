package com.android.yapp.scenetalker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Fragment_Search1 extends Fragment {
    static RecyclerView recyclerView;
    static Fragment_Search1_Adapter searchAdapter;
    LinearLayoutManager layoutManager;
    ImageButton b;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_search1,container,false);
        initUi(rootView);

        return rootView;
    }

    private void initUi(ViewGroup rootView){
        recyclerView = rootView.findViewById(R.id.search1_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        searchAdapter = new Fragment_Search1_Adapter();
        searchAdapter.addItem(new SearchInfo("고해리 정체",b));
        searchAdapter.addItem(new SearchInfo("수지 립스틱",b));
        searchAdapter.addItem(new SearchInfo("이승기",b));
        recyclerView.setAdapter(searchAdapter);
    }
}