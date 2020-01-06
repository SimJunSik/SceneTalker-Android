package com.scenetalker.yapp.scenetalker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.yapp.scenetalker.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import androidx.fragment.app.Fragment;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PageFragment extends Fragment {

    private int mPageNumber;
    DramaInfo item;
    String drama_title;
    int drama_id;
    String episode;
    ImageButton bookmark_button;

    public static PageFragment create(int pageNumber) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt("page", pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt("page");
    }

    public void setItem(DramaInfo item) {
        this.item = item;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_page, container, false);
        if(item.getPoster_url() != null&& !item.getPoster_url().equals("")){
            //image.setVisibility(View.VISIBLE);
            //image.setImageURI(Uri.parse("file://"+picturepath));
            Glide.with(getContext()).load(item.getPoster_url()).into((ImageView)rootView.findViewById(R.id.poster));
        }
        ((TextView)rootView.findViewById(R.id.episodeIndex)).setText(item.getEpisode()+"회 방영중");
        ((ImageView)rootView.findViewById(R.id.poster)).setClipToOutline(true);
        if(item.isIs_bookmarked_by_me()){
            ((ImageButton)rootView.findViewById(R.id.bookmark_button)).setImageResource(R.drawable.bookmark_full);
        }
        else {
            ((ImageButton)rootView.findViewById(R.id.bookmark_button)).setImageResource(R.drawable.bookmark_empty);
        }
        drama_title = item.getTitle();
        drama_id = item.getId();
        episode = item.getEpisode();

        Button btn_to_feed=rootView.findViewById(R.id.feed_button);
        btn_to_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),FeedPage.class);

                intent.putExtra("id",drama_id);
                intent.putExtra("episode",episode);
                intent.putExtra("name",drama_title);

                startActivity(intent);
            }
        });

        bookmark_button = rootView.findViewById(R.id.bookmark_button);
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

        return rootView;
    }


}