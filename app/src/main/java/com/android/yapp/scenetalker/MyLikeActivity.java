package com.android.yapp.scenetalker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;


public class MyLikeActivity extends AppCompatActivity {
    ImageButton close;
    private RecyclerView recyclerView=null;
    private MyLikeAdapter feedAdapter=null;
    private List<GetPostInfo> dataList=null;
    MyLike_Fragment myLike_fragment;
    MyLike_Nothing_Fragment myLike_nothing_fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_feed_like_activity);

        myLike_fragment = new MyLike_Fragment();
        myLike_nothing_fragment = new MyLike_Nothing_Fragment();

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
            fragmentTransaction.replace(R.id.myfeed_like_fragment_place, myLike_nothing_fragment).commit();
        }

    }

    private void init(){
        recyclerView = findViewById(R.id.recyclerview5);
        dataList = new ArrayList<GetPostInfo>();
    }

    private void setRecyclerView(){
        feedAdapter = new MyLikeAdapter(getApplicationContext(),R.layout.my_item_feed,dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedAdapter);
    }
    private void getfeed(){
        Call<JsonArray> call2 = NetRetrofit.getInstance().myLike();
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
