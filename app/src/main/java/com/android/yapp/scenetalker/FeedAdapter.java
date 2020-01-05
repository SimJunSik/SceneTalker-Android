package com.android.yapp.scenetalker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.yapp.scenetalker.FeedPage.drama_id;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int resourceId;
    private FragmentManager fm;
    private List<GetPostInfo> dataList;
    private final int TYPE_HEADER =0;
    private final int TYPE_ITEM=1;
    ViewPager fViewPager;
    Activity activity;
    private FeedPage currentView;
    FeedPage fp=new FeedPage();
    int page;
    String nextPage;
    TextView episode_result,potato_percent,cider_percent;
    int potato_count,cider_count;
    String episode_num;
    PagerAdapter2 pagerAdapter2;
    ItemViewHolder itemViewHolder;
    public FeedAdapter(Context context,int resourceId,List<GetPostInfo>dataList,FragmentManager fm){
        this.context=context;
        this.resourceId=resourceId;
        this.dataList=dataList;
        this.fm=fm;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_header, parent, false);
            holder = new HeaderViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
            holder = new ItemViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof HeaderViewHolder) {

        } else {
            fPagerAdapter pagerAdapter=new fPagerAdapter(context);
            itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.onBind(dataList.get(position - 1),position-1);
        }
        holder.getItemId();

    }

    @Override
    public int getItemCount() {
        return dataList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    public void itemReload(final String feed_id, String post_id, final int position){
        Call<JsonObject> call = NetRetrofit.getInstance().getOneFeed(feed_id,post_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Gson gson = new Gson();
                if(response.body()==null) {
                    Log.i("라이크 실패",response.errorBody().toString());
                    return;
                }
                Log.i("라이크",response.body().toString());
                GetPostInfo info = gson.fromJson(response.body(),GetPostInfo.class);
                info.setBitmap_image(dataList.get(position).getBitmap_image());
                dataList.set(position,info);
                notifyItemChanged(position+1);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("err",t.getMessage());
                call.cancel();
            }
        });

    }

    public void setDataListBitmap(int position, Bitmap bitmap) {
        this.dataList.get(position).setBitmap_image(bitmap);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView feed_post;
        TextView feed_time;
        TextView comment_num;
        TextView heart_num;
        ImageButton feedHeartBtn,feedCommentBtn;
        ImageView feed_img;

        public ItemViewHolder(View itemView){
            super(itemView);
            name=itemView.findViewById(R.id.username);
            feed_post=itemView.findViewById(R.id.feed_post);
            feed_time=itemView.findViewById(R.id.feed_time);
            comment_num=itemView.findViewById(R.id.comment_num);
            heart_num=itemView.findViewById(R.id.heart_num);
            feed_img = itemView.findViewById(R.id.feed_image);
            feedHeartBtn = itemView.findViewById(R.id.feed_heart_btn);
            feedCommentBtn = itemView.findViewById(R.id.feed_comment_btn);
        }

        void onBind(final GetPostInfo dataList, final int position){
            name.setText(dataList.getAuthor_name());
            feed_post.setText(dataList.getContent());
            feed_time.setText(Utils.getTimeFormat(dataList.getUpdated_at()));
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

            if (dataList.is_liked_by_me){
                feedHeartBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.full_heart));
            }else{
                feedHeartBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.heart));
            }

            feedHeartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call<JsonObject> call = NetRetrofit.getInstance().setLike(dataList.getFeed(),dataList.getId());
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Gson gson = new Gson();
                            if(response.body()==null) {
                                Log.i("라이크 실패",response.errorBody().toString());
                                return;
                            }
                            Log.i("라이크",response.body().toString());
                            itemReload(dataList.getFeed(),dataList.getId(),position);
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.e("err",t.getMessage());
                            call.cancel();
                        }
                    });
                }
            });
            feedCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,CommentActivity.class);

                    intent.putExtra("feedId",dataList.getFeed());
                    intent.putExtra("postId",dataList.getId());
                    intent.putExtra("commentId",dataList.getContent());

                    context.startActivity(intent);
                }
            });

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,CommentActivity.class);

                    intent.putExtra("feedId", dataList.getFeed());
                    intent.putExtra("postId", dataList.getId());

                    context.startActivity(intent);
                }
            });
            feed_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,CommentActivity.class);

                    intent.putExtra("feedId", dataList.getFeed());
                    intent.putExtra("postId", dataList.getId());

                    context.startActivity(intent);
                }
            });
        }

    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderViewHolder(View headerView) {
            super(headerView);
            page = 1;
            getItems(page);
            fViewPager=headerView.findViewById(R.id.fviewPager);
            pagerAdapter2=new PagerAdapter2(fm);
            fViewPager.setClipToPadding(false);
            fViewPager.setPadding(30,0,30,0);
            fViewPager.setAdapter(pagerAdapter2);
        }
    }

    public class PagerAdapter2 extends FragmentPagerAdapter {
        FeedFragment adapter;
        ArrayList<CountInfo> countitems;

        public PagerAdapter2(FragmentManager fm) {
            super(fm);
            countitems = new ArrayList<>();
        }

        public void setItems(ArrayList<CountInfo> countitems) {
            for(int i=0;i<countitems.size();i++){
                this.countitems.add(countitems.get(i));
            }
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            adapter = FeedFragment.create(position);
            adapter.setItem(countitems.get(position));
            // 해당하는 page의 Fragment 생성
            if(position == countitems.size()-1 && nextPage != null){
                getItems(page);
            }
            return adapter;
        }

        @Override
        public int getCount() {
            return countitems.size();  // 보여지는 페이지 개수

        }

        @Override
        public float getPageWidth (int position) {
            return 0.85f;
        }
    }

    public void getItems(int itempage){
        Call<JsonArray> call2 = NetRetrofit.getInstance().getDramaCount(drama_id);
        call2.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Gson gson = new Gson();
                if(response.body()==null)
                    return;
                JsonArray array = response.body().getAsJsonArray();

                ArrayList<CountInfo> counts=new ArrayList<>();

                for(int i=0;i<array.size();i++){
                    CountInfo info = gson.fromJson(array.get(i),CountInfo.class);
                    episode_num=info.getEpisode();
                    potato_count=info.getSweet_potato_count();
                    cider_count=info.getSoda_count();

                    //System.out.println(episode_num+potato_count+"/"+cider_count);

                    if(info != null){
                        counts.add(info);
                    }

                }
                page++;
                pagerAdapter2.setItems(counts);

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("err",t.getMessage());
                call.cancel();
            }
        });

    }

}
