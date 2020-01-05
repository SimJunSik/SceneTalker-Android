package com.android.yapp.scenetalker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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

import static com.android.yapp.scenetalker.FeedPage.drama_id;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private List<GetPostInfo> dataList;

    public CommentAdapter(Context context,List<GetPostInfo>dataList){
        this.context=context;
        this.dataList=dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        holder = new ItemViewHolder(view);
        return holder;
    }

    public void addItem(GetPostInfo info){
        dataList.add(info);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.onBind(dataList.get(position));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView comment;

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);

        }

        void onBind(final GetPostInfo dataList) {
            name.setText(dataList.getAuthor_name());
            comment.setText(dataList.getContent());
        }

    }

}