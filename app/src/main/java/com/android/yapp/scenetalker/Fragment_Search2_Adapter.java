package com.android.yapp.scenetalker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Search2_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int resourceId;
    private ArrayList<GetPostInfo> arraylist;
    private List<GetPostInfo> dataList;
    private final int TYPE_ITEM=1;

    public Fragment_Search2_Adapter(Context context, int resourceId, List<GetPostInfo>dataList){
        this.context=context;
        this.resourceId=resourceId;
        this.dataList=dataList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
        holder = new ItemViewHolder(view);
        arraylist = new ArrayList<>();
        arraylist.addAll(dataList);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)  {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.onBind(dataList.get(position),position);    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setDataListBitmap(int position, Bitmap bitmap) {
        this.dataList.get(position).setBitmap_image(bitmap);
    }
    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView feed_post;
        TextView feed_time;
        TextView comment_num;
        TextView heart_num;
        ImageButton feedHeartBtn,feedCommentBtn;
        ImageView feed_img;
        CircleImageView profileimg;
       // TextView drama_title;

        public ItemViewHolder(View itemView){
            super(itemView);
           // drama_title=itemView.findViewById(R.id.title);
            name=itemView.findViewById(R.id.username);
            feed_post=itemView.findViewById(R.id.feed_post);
            feed_time=itemView.findViewById(R.id.feed_time);
            comment_num=itemView.findViewById(R.id.comment_num);
            heart_num=itemView.findViewById(R.id.heart_num);
            feed_img = itemView.findViewById(R.id.feed_image);
            feedHeartBtn = itemView.findViewById(R.id.feed_heart_btn);
            feedCommentBtn = itemView.findViewById(R.id.feed_comment_btn);
            profileimg=itemView.findViewById(R.id.profileImg);

        }

        void onBind(final GetPostInfo dataList, final int position){
//            drama_title.setText(dataList.getPost_drama_title());
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

            Glide.with(context).load(uri).error(R.drawable.default_image).into(profileimg);

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
        }

    }


}