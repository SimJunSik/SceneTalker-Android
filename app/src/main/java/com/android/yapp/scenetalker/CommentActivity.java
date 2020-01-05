package com.android.yapp.scenetalker;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.yapp.scenetalker.databinding.ActivityCommentBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {
    ActivityCommentBinding binding;
    GetPostInfo feed;
    ArrayList<GetCommentInfo> comments;
    String feedId,id;
    CommentAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_comment);
        feedId = getIntent().getStringExtra("feedId");
        id = getIntent().getStringExtra("postId");
        getFeed(feedId,id);
        getComment(feedId,id);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        binding.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.commentOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment(feedId,id);
            }
        });
    }

    public void getFeed(final String feed_id, String post_id){
        Call<JsonObject> call = NetRetrofit.getInstance().getOneFeed(feed_id,post_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Gson gson = new Gson();
                if(response.body()==null) {
                    Log.i("라이크 실패",response.errorBody().toString());
                    return;
                }
                Log.i("피드",response.body().toString());
                feed = gson.fromJson(response.body(),GetPostInfo.class);

                binding.username.setText(feed.getAuthor_name());
                binding.feedPost.setText(feed.getContent());
                String merged_time =  Utils.getTimeFormat(feed.getUpdated_at());
                binding.feedTime.setText(merged_time);
                binding.commentNum.setText(String.valueOf(feed.getComment_counts()));
                binding.heartNum.setText(String.valueOf(feed.getLike_counts()));
                if(feed.getImage() != null){
                    if(feed.getBitmap_image() == null) {
                        Uri uri = Uri.parse(feed.getImage());
                        Glide.with(getApplicationContext()).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                feed.setBitmap_image(resource);
                                Glide.with(getApplicationContext()).load(resource).into(binding.feedImage);
                            }
                        });
                    }else{
                        Glide.with(getApplicationContext()).load(feed.getBitmap_image()).into(binding.feedImage);
                    }
                }

                if (feed.is_liked_by_me){
                    binding.feedHeartBtn.setImageDrawable(getResources().getDrawable(R.drawable.full_heart));
                }else{
                    binding.feedHeartBtn.setImageDrawable(getResources().getDrawable(R.drawable.heart));
                }
                binding.feedHeartBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Call<JsonObject> call = NetRetrofit.getInstance().setLike(feed.getFeed(),feed.getId());
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                Gson gson = new Gson();
                                if(response.body()==null) {
                                    Log.i("라이크 실패",response.errorBody().toString());
                                    return;
                                }
                                Log.i("라이크",response.body().toString());
                                getFeed(feed.getFeed(),feed.getId());
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.e("err",t.getMessage());
                                call.cancel();
                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("err",t.getMessage());
                call.cancel();
            }
        });
    }

    public void getComment(final String feed_id, String post_id){
        Call<JsonArray> call = NetRetrofit.getInstance().getComment(feed_id,post_id);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Gson gson = new Gson();
                if(response.body()==null) {
                    Log.i("라이크 실패",response.errorBody().toString());
                    return;
                }
                comments = new ArrayList<>();
                for(int i=0;i<response.body().size();i++){
                    GetCommentInfo info = gson.fromJson(response.body().get(i),GetCommentInfo.class);
                    comments.add(info);
                }
                adapter = new CommentAdapter(getApplicationContext(),comments);
                binding.recylce.setAdapter(adapter);
                binding.recylce.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
                Log.i("코멘트",response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("err",t.getMessage());
                call.cancel();
            }
        });
    }

    public void addComment(final String feed_id, final String post_id){
        PostInfo postInfo = new PostInfo(binding.commentEdit.getText().toString());
        Call<JsonObject> call = NetRetrofit.getInstance().addComment(postInfo,feed_id,post_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Gson gson = new Gson();
                if(response.body()==null) {
                    Log.i("라이크 실패",response.errorBody().toString());
                    return;
                }
                Log.i("코멘트",response.body().toString());
                binding.commentEdit.setText("");
                getComment(feed_id,post_id);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("err",t.getMessage());
                call.cancel();
            }
        });
    }
}