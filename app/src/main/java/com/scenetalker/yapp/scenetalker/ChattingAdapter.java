package com.scenetalker.yapp.scenetalker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.yapp.scenetalker.R;

import java.util.List;



public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.ViewHolder> {

    private Context context;
    private int resourceId;
    private List<ChattingInfo> dataList;

    public ChattingAdapter(Context context,int resourceId,List<ChattingInfo>dataList) {
        this.context = context;
        this.resourceId = resourceId;
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(resourceId,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChattingInfo chattingInfo = dataList.get(position);
        holder.name.setText(chattingInfo.getName());
        holder.talk.setText(chattingInfo.getTalk());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView talk;

        public ViewHolder(View itemView){
            super(itemView);
            name=itemView.findViewById(R.id.name);
            talk=itemView.findViewById(R.id.talk);
        }

    }
}
