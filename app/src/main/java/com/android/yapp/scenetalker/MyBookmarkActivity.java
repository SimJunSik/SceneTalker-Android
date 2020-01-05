package com.android.yapp.scenetalker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

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

public class MyBookmarkActivity  extends AppCompatActivity {
    ImageButton close;
    private RecyclerView recyclerView=null;
    private MyBookmarkAdapter bookmarkAdapter=null;
    private List<DramaInfo> dataList=null;
    MyBookmark_fragment myBookmark_fragment;
    MyBookmark_Nothing_Fragment MyBookmark_Nothing_Fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_bookmark_activity);

        myBookmark_fragment = new MyBookmark_fragment();
        MyBookmark_Nothing_Fragment = new MyBookmark_Nothing_Fragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.mybookmark_fragment_place, MyBookmark_Nothing_Fragment).commit();

        init();
        getbookmark();
        setRecyclerView();

        close=findViewById(R.id.back_btn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }

    private void init(){
        recyclerView = findViewById(R.id.recyclerview5);
        dataList = new ArrayList<DramaInfo>();
    }

    private void setRecyclerView(){
        bookmarkAdapter = new MyBookmarkAdapter(getApplicationContext(),R.layout.item_offair,dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(bookmarkAdapter);
    }
    private void getbookmark(){
        Call<JsonArray> call2 = NetRetrofit.getInstance().getUserBookmarkDramas();
        call2.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Gson gson = new Gson();
                if(response.body()==null)
                    return;
                JsonArray array = response.body().getAsJsonArray();
                Log.i("북마크",response.body().toString());

                ArrayList<DramaInfo> posts=new ArrayList<>();
                for(int i=0;i<array.size();i++){
                    DramaInfo info = gson.fromJson(array.get(i),DramaInfo.class);

                    if(info != null){
                        posts.add(info);
                        dataList.add(info);

                        FragmentManager fragmentManager= getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.remove(MyBookmark_Nothing_Fragment);
                        fragmentTransaction.replace(R.id.mybookmark_fragment_place,myBookmark_fragment).commit();

                        bookmarkAdapter.notifyDataSetChanged();
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
