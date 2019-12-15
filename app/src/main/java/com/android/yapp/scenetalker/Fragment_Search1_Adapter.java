package com.android.yapp.scenetalker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Fragment_Search1_Adapter extends RecyclerView.Adapter<Fragment_Search1_Adapter.ViewHolder>implements  OnsearchItemClickListener{
    ArrayList<SearchInfo> items = new ArrayList<>();
    OnsearchItemClickListener listener;

    @NonNull
    @Override
    public Fragment_Search1_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_recentword,parent,false);
        return new ViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull Fragment_Search1_Adapter.ViewHolder holder, int position) {
        SearchInfo item = items.get(position);
        holder.setItem(item);
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
        public ViewHolder(@NonNull View itemView, final  OnsearchItemClickListener listener) {
            super(itemView);
            word =itemView.findViewById(R.id.word);
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

        public void setItem(SearchInfo item){

            cancel.setImageResource(R.drawable.x);
            word.setText(item.word);
        }
    }
}