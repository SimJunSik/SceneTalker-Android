package com.android.yapp.scenetalker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Integer.valueOf;


public class MyWrittenAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int resourceId;
    private FragmentManager fm;
    private List<GetPostInfo> dataList;
    private final int TYPE_ITEM=1;
    //ViewPager fViewPager;
    private FeedPage currentView;
    FeedPage fp=new FeedPage();
    static String feedid;
    int dramaid;

    public MyWrittenAdapter(Context context,int resourceId,List<GetPostInfo>dataList){
        this.context=context;
        this.resourceId=resourceId;
        this.dataList=dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_item_feed, parent, false);
        holder = new MyWrittenAdapter.ItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.onBind(dataList.get(position),position);


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public void setDataListBitmap(int position, Bitmap bitmap) {
        this.dataList.get(position).setBitmap_image(bitmap);
    }
    public void itemReload(final String feed_id, String post_id, final int position){
        Call<JsonObject> call = NetRetrofit.getInstance().getOneFeed(feed_id,post_id);
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Gson gson = new Gson();
                if(response.body()==null) {
                    Log.i("삭제 실패",response.errorBody().toString());
                    return;
                }
                Log.i("삭제 완료",response.body().toString());
                dataList.remove(position);
                notifyItemRemoved(position);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("err",t.getMessage());
                call.cancel();
            }
        });
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView feed_post;
        TextView feed_time;
        TextView comment_num;
        TextView heart_num;
        ImageButton feedHeartBtn,feedCommentBtn;
        ImageView feed_img;
        TextView drama_title;
        Button delete_feed;
        CircleImageView profile_img;


        public ItemViewHolder(View itemView){
            super(itemView);
            drama_title=itemView.findViewById(R.id.title);
            name=itemView.findViewById(R.id.username);
            feed_post=itemView.findViewById(R.id.feed_post);
            feed_time=itemView.findViewById(R.id.feed_time);
            comment_num=itemView.findViewById(R.id.comment_num);
            heart_num=itemView.findViewById(R.id.heart_num);
            feed_img = itemView.findViewById(R.id.feed_image);
            feedHeartBtn = itemView.findViewById(R.id.feed_heart_btn);
            feedCommentBtn = itemView.findViewById(R.id.feed_comment_btn);
            delete_feed = itemView.findViewById(R.id.feed_delete_button);
            profile_img=itemView.findViewById(R.id.profileImg);

        }

        void onBind(final GetPostInfo dataList, final int position){
            drama_title.setText(dataList.getPost_drama_title());
            name.setText(dataList.getAuthor_name());
            feed_post.setText(dataList.getContent());
            feed_time.setText(dataList.getUpdated_at());
            comment_num.setText(String.valueOf(dataList.getComment_counts()));
            heart_num.setText(String.valueOf(dataList.getLike_counts()));
            if(dataList.getImage() != null){
                if(dataList.getBitmap_image() == null) {
                    Uri uri = Uri.parse(dataList.getImage());
                    Glide.with(context).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            setDataListBitmap(position,resource);
                            Glide.with(context).load(resource).into(feed_img);
                        }
                    });
                }else{
                    Glide.with(context).load(dataList.getBitmap_image()).into(feed_img);
                }
            }
            Uri uri = Uri.parse(dataList.getUser_profile_img());

            Glide.with(context).load(uri).error(R.drawable.default_image).into(profile_img);
            if (dataList.is_liked_by_me){
                feedHeartBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.full_heart));
            }else{
                feedHeartBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.heart));
            }


            feedCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,CommentActivity.class);
                    intent.putExtra("feedId",dataList.getFeed());
                    intent.putExtra("postId",dataList.getId());

                    context.startActivity(intent);
                }
            });
            delete_feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedid = dataList.getFeed();
                    dramaid = Integer.parseInt(dataList.getId());

                    Log.e("피이드으아이디이",feedid);
                    Log.e("드라마아이이디이",String.valueOf(dramaid));

                    String username = name.getText().toString();
                    String comment = feed_post.getText().toString();
                    String comment_time = feed_time.getText().toString();
                    int comment_numm = Integer.parseInt(comment_num.getText().toString());
                    int heart_numm = Integer.parseInt(heart_num.getText().toString());
                    //String image = dataList.getBitmap_image().toString();
                    String image = "";
                    FeedInfo feedInfo = new FeedInfo(username,comment,comment_time,comment_numm,heart_numm,image);
                    Call<JsonObject> call = NetRetrofit.getInstance().deleteFeedPost(feedInfo,feedid,dramaid);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Gson gson = new Gson();
                            if(response.body() == null){
                                return;
                            }

                            Log.i("코드",""+response.code());

                            Log.i("결과",response.body().toString());
                            JSONParser parser = new JSONParser();
                            Object obj = null;
                            try {
                                obj = parser.parse(response.body().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            JSONObject jsonObj = (JSONObject) obj;

                            String result = jsonObj.get("result").toString();
                            if(result.equals("OK")){
                                itemReload(dataList.getFeed(),dataList.getId(),position);
                            }
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

    }







}
