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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.onBind(dataList.get(position),position);

//        final String feed_id = dataList.get(position).getFeed();
//        final String id = dataList.get(position).getId();
//
//        Button delete_button = ((ItemViewHolder) holder).delete_feed;
//
//        delete_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Call<JsonObject> call = NetRetrofit.getInstance().deleteFeedPost(feed_id,id);
//                call.enqueue(new Callback<JsonObject>() {
//                    @Override
//                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                        Gson gson = new Gson();
//                        if(response.body()==null) {
//                            Log.i("삭제 실패",response.errorBody().toString());
//                            return;
//                        }
//                        Log.i("삭제 완료",response.body().toString());
//                    }
//
//                    @Override
//                    public void onFailure(Call<JsonObject> call, Throwable t) {
//                        Log.e("err",t.getMessage());
//                        call.cancel();
//                    }
//                });
//                dataList.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, dataList.size());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public void setDataListBitmap(int position, Bitmap bitmap) {
        this.dataList.get(position).setBitmap_image(bitmap);
    }
    public void itemReload(final String feed_id, String post_id, final int position){

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

        void onBind(final GetPostInfo data, final int position){
            drama_title.setText(data.getPost_drama_title());
            name.setText(data.getAuthor_name());
            feed_post.setText(data.getContent());
            feed_time.setText(Utils.getTimeFormat(data.getUpdated_at()));
            comment_num.setText(String.valueOf(data.getComment_counts()));
            heart_num.setText(String.valueOf(data.getLike_counts()));
            if(data.getImage() != null){
                if(data.getBitmap_image() == null) {
                    Uri uri = Uri.parse(data.getImage());
                    Glide.with(context).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            setDataListBitmap(position,resource);
                            Glide.with(context).load(resource).into(feed_img);
                        }
                    });
                }else{
                    Glide.with(context).load(data.getBitmap_image()).into(feed_img);
                }
            }
            Uri uri = Uri.parse(data.getUser_profile_img());

            Glide.with(context).load(uri).error(R.drawable.default_image).into(profile_img);
            if (data.is_liked_by_me){
                feedHeartBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.full_heart));
            }else{
                feedHeartBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.heart));
            }


            feedCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,CommentActivity.class);
                    intent.putExtra("feedId",data.getFeed());
                    intent.putExtra("postId",data.getId());

                    context.startActivity(intent);
                }
            });
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,CommentActivity.class);

                    intent.putExtra("feedId", data.getFeed());
                    intent.putExtra("postId", data.getId());

                    context.startActivity(intent);
                }
            });
            feed_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,CommentActivity.class);

                    intent.putExtra("feedId", data.getFeed());
                    intent.putExtra("postId", data.getId());

                    context.startActivity(intent);
                }
            });
            delete_feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedid = data.getFeed();
                    dramaid = Integer.parseInt(data.getId());

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
//                                itemReload(dataList.getFeed(),dataList.getId(),position);
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.e("err",t.getMessage());
                            call.cancel();
                        }
                    });

                    dataList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, dataList.size());
                }
            });
        }

    }







}
