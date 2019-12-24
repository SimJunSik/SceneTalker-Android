package com.android.yapp.scenetalker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Search1 extends Fragment {
    static RecyclerView recyclerView;
    static Fragment_Search1_Adapter searchAdapter;
    LinearLayoutManager layoutManager;
    ImageButton b;
    Button recentSearchesDeleteAllButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_search1,container,false);
        initUi(rootView);

        return rootView;
    }

    private void initUi(ViewGroup rootView){
        recyclerView = rootView.findViewById(R.id.search1_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        searchAdapter = new Fragment_Search1_Adapter();
        recentSearchesDeleteAllButton = rootView.findViewById(R.id.recent_searches_all_delete_button);

        recentSearchesDeleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchWordInfo searchWordInfo = new SearchWordInfo("");
                Call<JsonObject> service = NetRetrofit.getInstance().deleteUserRecentSearches(searchWordInfo);
                service.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Gson gson = new Gson();
                        if(response.body() == null){
                            return;
                        }
                        Log.i("코드",""+response.code());

                        Log.i("결과",response.body().toString());
                        JSONParser parser = new JSONParser();
                        Object obj = null;
                        try {
                            obj = parser.parse(response.body().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        JSONObject jsonObj = (JSONObject) obj;

                        String result = jsonObj.get("result").toString();
                        if(result.equals("OK")){
                            recyclerView.setAdapter(null);
                            searchAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });


        Call<JsonObject> service = NetRetrofit.getInstance().getUserRecentSearches();
        service.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Gson gson = new Gson();
                if(response.body() == null){
                    return;
                }
                Log.i("코드",""+response.code());
                JsonArray array = response.body().getAsJsonArray("recent_searches");

                for(int i=0;i<array.size();i++) {
                    searchAdapter.addItem(new SearchInfo(array.get(i).toString().replaceAll("\"",""), b));
                }

                recyclerView.setAdapter(searchAdapter);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}