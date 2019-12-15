package com.android.yapp.scenetalker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Search2 extends Fragment {
    private RecyclerView recyclerView=null;
    private Fragment_Search2_Adapter feedAdapter2=null;
    private  List<FeedInfo> dataList=null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_search2,container,false);
        init(rootView);
//        add();
        setRecyclerView();
        return rootView;
    }

    private void init(View rootView){
        recyclerView = rootView.findViewById(R.id.match_recyclerview);
    }

    private void setRecyclerView(){
        feedAdapter2 = new Fragment_Search2_Adapter(getActivity().getApplicationContext(), R.layout.item_feed,dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedAdapter2);
    }

}