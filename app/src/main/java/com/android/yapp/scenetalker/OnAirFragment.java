package com.android.yapp.scenetalker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnAirFragment extends Fragment {

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    PageFragment pf;
    int page;
    String nextPage;
    DramaInfo dramaInfo;
    Button livetalk;
    int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        this.mPagerAdapter = new PagerAdapter(getChildFragmentManager());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_onair, container, false);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        this.mViewPager = (ViewPager) view.findViewById(R.id.pager);
        this.mViewPager.setAdapter(mPagerAdapter);
        this.mViewPager.setClipToPadding(false);
        this.mViewPager.setPageMargin(60);
        page = 1;
        getItems(page);

       livetalk = view.findViewById(R.id.livetalk);
       livetalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChattingActivity.class);
                String id=Integer.toString(mPagerAdapter.items.get(mViewPager.getCurrentItem()).getId());
                String episode=mPagerAdapter.items.get(mViewPager.getCurrentItem()).getEpisode();
                String title=mPagerAdapter.items.get(mViewPager.getCurrentItem()).getTitle();
                intent.putExtra("drama_id",id);
                intent.putExtra("episode",episode);
                intent.putExtra("drama_title",title);
                System.out.println("보냄"+id+" "+episode);

                startActivity(intent);
            }
        });
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        PageFragment adapter;
        ArrayList<DramaInfo> items;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            items = new ArrayList<>();
        }

        public void setItems(ArrayList<DramaInfo> items) {
            for(int i=0;i<items.size();i++){
                this.items.add(items.get(i));
            }
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            adapter = PageFragment.create(position);
            adapter.setItem(items.get(position));
            // 해당하는 page의 Fragment 생성
            if(position == items.size()-1 && nextPage != null){
                getItems(page);
            }
            return adapter;
        }

        @Override
        public int getCount() {
            return items.size();  // 보여지는 페이지 개수
        }

        @Override
        public float getPageWidth (int position) {
            return 0.85f;
        }
    }

    public void getItems(int itempage){
        Call<JsonObject> service = NetRetrofit.getInstance().getDramaList(true,itempage);
        service.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Gson gson = new Gson();
                if(response.body()==null)
                    return;
                Log.i("답장",response.body().toString());
                JsonArray array = response.body().getAsJsonArray("results");
                try {
                    nextPage = response.body().get("next").getAsString();
                }catch (Exception e){
                    nextPage = null;
                }
                ArrayList<DramaInfo> dramas = new ArrayList<>();
                for(int i=0;i<array.size();i++){
                    DramaInfo info = gson.fromJson(array.get(i),DramaInfo.class);
                    if(info != null){
                        id=info.getId();
                        dramas.add(info);

                    }

                }

                page++;

                mPagerAdapter.setItems(dramas);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }
}
