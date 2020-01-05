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
import android.widget.Button;
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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.yapp.scenetalker.FeedPage.drama_id;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private List<GetCommentInfo> dataList;

    private Button delete_button;
    // private Button edit_button;

    public CommentAdapter(Context context,List<GetCommentInfo>dataList){
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

    public void addItem(GetCommentInfo info){
        dataList.add(info);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.onBind(dataList.get(position));
            final GetCommentInfo data = dataList.get(position);
            delete_button = ((ItemViewHolder) holder).delete_button;
            // edit_button = ((ItemViewHolder) holder).edit_button;

            boolean is_mine = data.isIs_mine();
            if(!is_mine){
                delete_button.setVisibility(View.INVISIBLE);
            }
            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("버튼클릭2222", data.getId());
                    Call<JsonObject> service = NetRetrofit.getInstance().deletePostComment(data.getFeed_id(), data.getPost(), data.getId());
                    service.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Gson gson = new Gson();
                            if(response.body() == null){
                                return;
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                    dataList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, dataList.size());
                }
            });
//            edit_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView comment;
        Button delete_button;
        CircleImageView profile_img;
        // Button edit_button;

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
            delete_button = itemView.findViewById(R.id.comment_delete_button);
            profile_img=itemView.findViewById(R.id.profileImg);

            // edit_button = itemView.findViewById(R.id.comment_edit_button);
        }

        void onBind(final GetCommentInfo data) {
            name.setText(data.getAuthor_name());
            comment.setText(data.getContent());
            Uri uri = Uri.parse(data.getUser_profile_image());

            Glide.with(context).load(uri).error(R.drawable.default_image).into(profile_img);

        }

    }

}