package com.android.yapp.scenetalker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Search2_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int resourceId;
    private ArrayList<FeedInfo> arraylist;
    private List<FeedInfo> dataList =null ;
    private final int TYPE_ITEM=1;

    public Fragment_Search2_Adapter(Context context, int resourceId, List<FeedInfo>dataList){
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.onBind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
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

        public ItemViewHolder(View itemView){
            super(itemView);
            name=itemView.findViewById(R.id.username);
            feed_post=itemView.findViewById(R.id.feed_post);
            feed_time=itemView.findViewById(R.id.feed_time);
            comment_num=itemView.findViewById(R.id.comment_num);
            heart_num=itemView.findViewById(R.id.heart_num);

        }

        void onBind(FeedInfo dataList){
            name.setText(dataList.getUsername());
            feed_post.setText(dataList.getComment());
            feed_time.setText(dataList.getComment_time());
            comment_num.setText(Integer.toString(dataList.getComment_num()));
            heart_num.setText(Integer.toString(dataList.getHeart_num()));
        }

    }


}