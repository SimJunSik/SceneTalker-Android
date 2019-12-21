package com.android.yapp.scenetalker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyWrittenActivity extends AppCompatActivity {
    ImageButton close;
    private RecyclerView recyclerView=null;
    private MyWrittenAdapter feedAdapter=null;
    private List<GetPostInfo> dataList=null;

    //private List<FeedInfo> dataList=null;
    MyWritten_Fragment myWritten_fragment;
    MyWritten_Nothing_Fragment myWritten_nothing_fragment;

    String contents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_feed_activity);

        myWritten_fragment = new MyWritten_Fragment();
        myWritten_nothing_fragment = new MyWritten_Nothing_Fragment();

        init();
        getfeed();
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
        dataList = new ArrayList<GetPostInfo>();
    }


    private void setRecyclerView(){
        feedAdapter = new MyWrittenAdapter(getApplicationContext(),R.layout.my_item_feed,dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedAdapter);
    }
    private void getfeed(){
        Call<JsonArray> call2 = NetRetrofit.getInstance().myWrite();
        call2.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Gson gson = new Gson();
                if(response.body()==null)
                    return;
                JsonArray array = response.body().getAsJsonArray();
                Log.i("피드",response.body().toString());

                ArrayList<GetPostInfo> posts=new ArrayList<>();
                for(int i=0;i<array.size();i++){
                    GetPostInfo info = gson.fromJson(array.get(i),GetPostInfo.class);

                    if(info != null){
                        posts.add(info);
                        dataList.add(info);
                    }

                }
                setRecyclerView();

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("err",t.getMessage());
                call.cancel();
            }
        });
    }

}
