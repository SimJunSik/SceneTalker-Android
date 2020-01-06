package com.scenetalker.yapp.scenetalker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.yapp.scenetalker.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Search1_Adapter extends RecyclerView.Adapter<Fragment_Search1_Adapter.ViewHolder>implements  OnsearchItemClickListener{
    private ArrayList<SearchInfo> items = new ArrayList<>();
    private OnsearchItemClickListener listener;

    @NonNull
    @Override
    public Fragment_Search1_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_recentword,parent,false);
        return new ViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull final Fragment_Search1_Adapter.ViewHolder viewHolder, final int position) {
        SearchInfo item = items.get(position);
        viewHolder.setItem(item);

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = viewHolder.word.getText().toString();
                SearchWordInfo searchWordInfo = new SearchWordInfo(text);
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
                            items.remove(position);
                            notifyItemRemoved(position);
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

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(SearchInfo item){
        items.add(item);
    }
    public void setItems(ArrayList<SearchInfo> items) {
        this.items = items;
    }

    public SearchInfo getItem(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(OnsearchItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView word;
        ImageButton cancel;
        public final View mView;

        public ViewHolder(@NonNull View itemView, final  OnsearchItemClickListener listener) {
            super(itemView);
            mView = itemView;
            word = itemView.findViewById(R.id.word);
            cancel = itemView.findViewById(R.id.search_cancel);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this,view,position);
                    }
                }
            });
        }

        public void setItem(final SearchInfo item){
            cancel.setImageResource(R.drawable.x);
            word.setText(item.word);

            final String text = item.word;
        }
    }
}