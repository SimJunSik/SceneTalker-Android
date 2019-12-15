package com.android.yapp.scenetalker;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MyWrittenActivity extends AppCompatActivity {
    ImageButton close;
    private RecyclerView recyclerView=null;
    private MyWrittenAdapter feedAdapter=null;
    private List<FeedInfo> dataList=null;
    MyWritten_Fragment myWritten_fragment;
    MyWritten_Nothing_Fragment myWritten_nothing_fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_feed_activity);

        myWritten_fragment = new MyWritten_Fragment();
        myWritten_nothing_fragment = new MyWritten_Nothing_Fragment();

        init();
        add();
        setRecyclerView();

        close=findViewById(R.id.back_btn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        if (dataList.size()==0){
            FragmentManager fragmentManager= getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.myfeed_fragment_place, myWritten_nothing_fragment).commit();
        }

//        else{
//            FragmentManager fragmentManager= getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.myfeed_fragment_place, myWritten_fragment).commit();
//        }
    }

    private void init(){
        recyclerView = findViewById(R.id.recyclerview4);
        dataList = new ArrayList<FeedInfo>();
    }
    private void add(){

        dataList.add(new FeedInfo("hsg","아휴","방금 전",1,1));

    }

    private void setRecyclerView(){
        feedAdapter = new MyWrittenAdapter(getApplicationContext(),R.layout.item_feed,dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedAdapter);
    }


}
