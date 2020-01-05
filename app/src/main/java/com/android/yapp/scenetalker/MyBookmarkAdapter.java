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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookmarkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {

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
        String bookmark;


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





        }

        void onBind(final DramaInfo item, final int position){
            if(item.getPoster_url() != null&& !item.getPoster_url().equals("")){
                //image.setVisibility(View.VISIBLE);
                //image.setImageURI(Uri.parse("file://"+picturepath));
                Glide.with(context).load(item.getPoster_url()).into(image);
            }
            final String feedname=item.getTitle();
            final String episode_num=item.getEpisode();
            final int drama_id=item.getId();
            gotofeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,FeedPage.class);
                    intent.putExtra("name",feedname);
                    intent.putExtra("episode",episode_num);
                    intent.putExtra("id",drama_id);

                    context.startActivity(intent);
                }
            });
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
            bookmark_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("북마크", Integer.toString(drama_id));
                    Call<JsonObject> service = NetRetrofit.getInstance().toggleUserDramaBookmark(Integer.toString(drama_id));
                    service.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Gson gson = new Gson();
                            if(response.message() != null) {
                                Log.i("에러 결과", response.toString());
                            }
                            if(response.body() == null){
                                return;
                            }
                            Log.i("북마크",response.body().toString());
                            JSONParser parser = new JSONParser();
                            Object obj = null;
                            try {
                                obj = parser.parse(response.body().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            JSONObject jsonObj = (JSONObject) obj;

                            String description;
                            description = jsonObj.get("description").toString();

                            String result;
                            result = jsonObj.get("result").toString();

                            Log.i("북마크", result + " " + description);
                            if(result.equals("OK")){
                                if(description.equals("add")) {
                                    bookmark_button.setImageResource(R.drawable.bookmark_full);
                                }
                                else if(description.equals("remove")){
                                    bookmark_button.setImageResource(R.drawable.bookmark_empty);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            });
        }

    }


}
