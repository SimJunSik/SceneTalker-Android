package com.android.yapp.scenetalker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyBookmarkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int resourceId;
    private FragmentManager fm;
    private List<DramaInfo> dataList;
    OnInfoItemClickListener listener;


    public MyBookmarkAdapter(Context context, int resourceId, List<DramaInfo>dataList){
        this.context=context;
        this.resourceId=resourceId;
        this.dataList=dataList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offair, parent, false);
        holder = new MyBookmarkAdapter.ItemViewHolder(view);

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


    class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView production;
        TextView dname;
        TextView rating;
        TextView time;
        Button count;
        Button gotofeed;
        ImageButton bookmark_button;


        public ItemViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.image);
            production = itemView.findViewById(R.id.production);
            dname = itemView.findViewById(R.id.dramaname);
            rating = itemView.findViewById(R.id.rating);
            time = itemView.findViewById(R.id.time);
            count = itemView.findViewById(R.id.count);
            gotofeed = itemView.findViewById(R.id.gotofeed);
            bookmark_button=itemView.findViewById(R.id.bookmark_button);
            bookmark_button = itemView.findViewById(R.id.bookmark_button);


            gotofeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,FeedPage.class);
                    context.startActivity(intent);
                }
            });
        }

        void onBind(final DramaInfo item, final int position){
            if(item.getPoster_url() != null&& !item.getPoster_url().equals("")){
                //image.setVisibility(View.VISIBLE);
                //image.setImageURI(Uri.parse("file://"+picturepath));
                Glide.with(context).load(item.getPoster_url()).into(image);
            }
            production.setText(item.getBroadcasting_station());
            dname.setText(item.getTitle());
            rating.setText(item.getRating()+"%");
            String day = "";
            if(item.getBroadcasting_day() != null && item.getBroadcasting_day().size() != 0){
                StringTokenizer st = new StringTokenizer(item.getBroadcasting_start_time(),":");
                String time = st.nextToken()+"시 ";
                String min = st.nextToken();
                if(!min.equals("00")) {
                    time += min + "분";
                }
                day = item.getBroadcasting_day().get(0)+"~"+item.getBroadcasting_day().get(item.getBroadcasting_day().size()-1)+" "+time;
            }
            time.setText(day);
            count.setText(String.valueOf(getAdapterPosition()+1));
            if (item.isIs_bookmarked_by_me()){
                bookmark_button.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmark_full));
            }else{
                bookmark_button.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmark_empty));
            }
        }

    }


}
