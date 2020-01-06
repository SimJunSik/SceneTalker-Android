package com.scenetalker.yapp.scenetalker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.yapp.scenetalker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedPage extends AppCompatActivity {

    ProgressBar progressBar;
    private RecyclerView recyclerView=null;
    private FeedAdapter feedAdapter=null;
    private List<GetPostInfo> dataList=null;
    ImageButton close,search;
    FloatingActionButton write_btn;
    TextView drama_name_title;
    public static String drama_title,episode;
    public static int drama_id;

    @Override
    protected void onResume() {
        super.onResume();
//        init();
//        getfeed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);

        Intent intent=getIntent();
        drama_title = intent.getExtras().getString("name");
        episode=intent.getExtras().getString("episode");
        drama_id=intent.getExtras().getInt("id");

        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        close=(ImageButton)findViewById(R.id.feed_close_btn);
        search=(ImageButton)findViewById(R.id.feed_search_btn);
        write_btn=(FloatingActionButton)findViewById(R.id.write_btn);

        drama_name_title=(TextView)findViewById(R.id.drama_title);
        drama_name_title.setText(drama_title+" 게시판");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),WritePage.class);
                intent.putExtra("name",drama_title);
                intent.putExtra("dramaId",drama_id);
                // startActivity(intent);
                startActivityForResult(intent, 1);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                intent.putExtra("dramaId",drama_id);
                startActivity(intent);
            }
        });

        init();
        getfeed();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                Log.i("result", result);
                finish();
                startActivity(getIntent());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
            }
        }
    }//onActivityResult

    private void init(){
        recyclerView = findViewById(R.id.recyclerview3);
        dataList = new ArrayList<GetPostInfo>();
    }

    private void setRecyclerView(){
        feedAdapter = new FeedAdapter(getApplicationContext(),R.layout.item_feed,dataList,getSupportFragmentManager());
        feedAdapter.setActivity(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedAdapter);
    }

    private void getfeed(){
        Call<JsonArray> call2 = NetRetrofit.getInstance().getFeed(drama_id);
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
                    //contents=info.getContent();

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
