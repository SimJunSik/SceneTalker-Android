package com.android.yapp.scenetalker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffAirAdapter extends RecyclerView.Adapter<OffAirAdapter.ViewHolder> implements OnInfoItemClickListener  {
    ArrayList<DramaInfo> items = new ArrayList<DramaInfo>();
    OnInfoItemClickListener listener;
    static Context mContext;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_offair,parent,false);
        mContext = parent.getContext();
        return new ViewHolder (itemView,this, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        DramaInfo item = items.get(position);
        viewHolder.setItem(mContext,item);
        if(position == items.size()-1){
            OffAirFragment.itemHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(DramaInfo item){
        items.add(item);
    }

    public void setItems(ArrayList<DramaInfo> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public DramaInfo getItem(int position){
        return items.get(position);
    }

    public void setOnItemClickListener(OnInfoItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder,View view,int position){
        if(listener != null){
            listener.onItemClick(holder,view,position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView production;
        TextView dname;
        TextView rating;
        TextView time;
        Button count;
        Button gotofeed;

        public ViewHolder(View itemView,final OnInfoItemClickListener listener,final Context context){
            super(itemView);
            image = itemView.findViewById(R.id.image);
            production = itemView.findViewById(R.id.production);
            dname = itemView.findViewById(R.id.dramaname);
            rating = itemView.findViewById(R.id.rating);
            time = itemView.findViewById(R.id.time);
            count = itemView.findViewById(R.id.count);

            gotofeed = itemView.findViewById(R.id.gotofeed);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view){
                    int position = getAdapterPosition();
                    Context context=view.getContext();

                    if(listener != null){
                        listener.onItemClick(ViewHolder.this,view,position);
                    }

                }
            });

            gotofeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,FeedPage.class);
                    context.startActivity(intent);
                }
            });
        }

        public void setItem(Context context,DramaInfo item){
            final String feedname=item.getTitle();
            final String episode_num=item.getEpisode();
            final int drama_id=item.getId();
            gotofeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,FeedPage.class);
                    intent.putExtra("name",feedname);
                    intent.putExtra("episode",episode_num);
                    intent.putExtra("id",drama_id);



                    mContext.startActivity(intent);
                }
            });
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
        }
    }
}
