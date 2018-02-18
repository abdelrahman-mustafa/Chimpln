package com.indeves.chmplinapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.indeves.chmplinapp.Models.ProEventHistoryItem;
import com.indeves.chmplinapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by khalid on 18/02/18.
 */

public class ProEventHistoryAdapter extends RecyclerView.Adapter<ProEventHistoryAdapter.MyViewHolder> {
    private List<ProEventHistoryItem> list;
    private Context mContext;

    public ProEventHistoryAdapter(List<ProEventHistoryItem> list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pro_profile_history_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProEventHistoryItem pData = list.get(position);
        holder.eventTitle.setText(pData.getTitle());
        holder.eventLocation.setText(pData.getLocation());
        holder.eventDescription.setText(pData.getDescription());
        Picasso.with(mContext).load(pData.getImageUrl()).placeholder(mContext.getResources().getDrawable(R.drawable.test_image)).into(holder.eventImage);

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView eventTitle, eventLocation, eventDescription;
        ImageView eventImage;

        public MyViewHolder(View view) {
            super(view);
            eventTitle = view.findViewById(R.id.proProfile_eventTitle_textView);
            eventLocation = view.findViewById(R.id.proProfile_eventLocation_textView);
            eventDescription = view.findViewById(R.id.proProfile_eventDescription_textView);
            eventImage = view.findViewById(R.id.proProfile_eventImage_imageView);

        }
    }
}
