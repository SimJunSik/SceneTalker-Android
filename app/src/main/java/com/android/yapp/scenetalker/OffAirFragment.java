package com.android.yapp.scenetalker;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

public class OffAirFragment extends Fragment {

    RecyclerView recyclerView;
    OffAirAdapter adapter;
    Context context;
    OnTabItemSelectedListener listener;
    ArrayList<DramaInfo> dramas = new ArrayList<>();
    public static Handler itemHandler;
    int page;
    String nextPage;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
        if(context instanceof OnTabItemSelectedListener){
            listener = (OnTabItemSelectedListener)context;
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();

        if(context != null) {
            context = null;
            listener = null;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_offair, container, false);
        page = 1;
        itemHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(nextPage != null)
                    getItems(page);
                return false;
            }
        });
        initUi(rootView);
        return rootView;
    }
    private void initUi(ViewGroup rootView) {
        recyclerView = rootView.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new OffAirAdapter();
        recyclerView.setAdapter(adapter);
        getItems(page);
    }

    public void getItems(int itempage){
        if(itempage == 1)
            dramas = new ArrayList<>();

        Call<JsonObject> service = NetRetrofit.getInstance().getDramaList(itempage);
        service.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Gson gson = new Gson();
                if(response.body() == null){
                    return;
                }
                Log.i("코드",""+response.code());
                JsonArray array = response.body().getAsJsonArray("results");
                try {
                    nextPage = response.body().get("next").getAsString();
                }catch (Exception e){
                    nextPage = null;
                }
                for(int i=0;i<array.size();i++){
                    DramaInfo info = gson.fromJson(array.get(i),DramaInfo.class);
                    if(info != null)
                        dramas.add(info);
                }
                page++;
                adapter.setItems(dramas);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
