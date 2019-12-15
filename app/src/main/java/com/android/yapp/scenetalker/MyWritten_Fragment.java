package com.android.yapp.scenetalker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class MyWritten_Fragment extends Fragment {
    private RecyclerView recyclerView=null;
    private MyWrittenAdapter feedAdapter=null;
    private List<FeedInfo> dataList=null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.my_feed_fragment,container,false);
//
        return rootView;

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
//            if(resultCode == Activity.RESULT_OK){
//                String result=data.getStringExtra("result");
//                Log.i("result", result);
//                getActivity().finish();
//                startActivity(getActivity().getIntent());
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//            }
//        }
//    }//onActivityResult

//    private void init(View rootView){
//        recyclerView = rootView.findViewById(R.id.recyclerview4);
//        dataList = new ArrayList<FeedInfo>();
//    }
//    public void setRecyclerView(){
//        feedAdapter = new MyWrittenAdapter(getActivity().getApplicationContext(),R.layout.item_feed,dataList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(feedAdapter);
//    }





}
