package com.indeves.chmplinapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indeves.chmplinapp.Models.PhotographerData;
import com.indeves.chmplinapp.R;

import java.util.List;

public class UserProfEventsAdaptor extends RecyclerView.Adapter<UserProfEventsAdaptor.MyViewHolder> {
    List<PhotographerData> list;

    public UserProfEventsAdaptor(List<PhotographerData> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_user_profile_tab_events_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PhotographerData pData = list.get(position);
        holder.name.setText(pData.getName());
        holder.day.setText(pData.getDateDay());
        holder.month.setText(pData.getDateMonth());
        holder.time.setText(pData.getTime());
        holder.share.setText(pData.getShareOption());
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, type, time, day, month, share;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.userProfile_event_photographer_name);
            time = view.findViewById(R.id.userProfile_event_time);
            day = view.findViewById(R.id.userProfile_event_day);
            month = view.findViewById(R.id.userProfile_event_month);
            share = view.findViewById(R.id.userProfile_event_shareOption);

        }
    }
}
