package com.android.yapp.scenetalker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class SearchActivity extends AppCompatActivity {

    private ImageButton backtofeed;
    private ImageButton search_button;
    private EditText search_text;
    Fragment_Search2 fragment_search2;
    Fragment_Search1 fragment_search1;
    Fragment_Search3 fragment_search3;
    int dramaId;

    private List<GetPostInfo> dataList = null;
    private ArrayList<GetPostInfo> arraylist;

    Fragment_Search2_Adapter feedAdapter2;
    private RecyclerView recyclerView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final Intent intent = new Intent(this.getIntent());
        dramaId = intent.getIntExtra("dramaId",-1);

        search_text = findViewById(R.id.search_text);
        search_button = findViewById(R.id.search_button);
        backtofeed = findViewById(R.id.backtofeed);
        fragment_search2 = new Fragment_Search2();
        fragment_search1 = new Fragment_Search1();
        fragment_search3 = new Fragment_Search3();
        FragmentManager fragmentManager1 = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
        fragmentTransaction1.add(R.id.fragment_place, fragment_search1);
        fragmentTransaction1.commit();
        init();

        setRecyclerView();
        backtofeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = search_text.getText().toString();
                search(text);
            }
        });
    }
    private void init(){
        recyclerView = findViewById(R.id.match_recyclerview);
        dataList = new ArrayList<GetPostInfo>();
    }

    private void setRecyclerView() {
        feedAdapter2 = new Fragment_Search2_Adapter(getApplicationContext(), R.layout.item_feed, dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedAdapter2);
    }

    public void search(String charText) {
          dataList.clear();
        if (charText.length() == 0) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fragment_search1).commit();
        } else {
            getSearch(charText);
        }
        feedAdapter2.notifyDataSetChanged();
    }


    private void getSearch(String charText){
        Call<JsonArray> call2 = NetRetrofit.getInstance().feed(dramaId,charText);
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
