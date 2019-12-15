package com.android.yapp.scenetalker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


public class MyWrittenAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int resourceId;
    private FragmentManager fm;
    private List<FeedInfo> dataList;
    private final int TYPE_HEADER =0;
    private final int TYPE_ITEM=1;
    //ViewPager fViewPager;
    private FeedPage currentView;
    FeedPage fp=new FeedPage();
    int page;
    String nextPage;
    TextView episode_result,potato_percent,cider_percent;
    int potato_count,cider_count;
    String episode_num;

    public MyWrittenAdapter(Context context,int resourceId,List<FeedInfo>dataList){
        this.context=context;
        this.resourceId=resourceId;
        this.dataList=dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_feed_head, parent, false);
            holder = new MyWrittenAdapter.HeaderViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
            holder = new MyWrittenAdapter.ItemViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FeedAdapter.HeaderViewHolder) {

        } else {

        }

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

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderViewHolder(View headerView) {
            super(headerView);

            TextView head=headerView.findViewById(R.id.my_drama_title);
        }
    }





}
