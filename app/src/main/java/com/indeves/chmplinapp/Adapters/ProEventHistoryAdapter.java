package com.indeves.chmplinapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.ProEventHistoryItem;
import com.indeves.chmplinapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by khalid on 18/02/18.
 */

public class ProEventHistoryAdapter extends RecyclerView.Adapter<ProEventHistoryAdapter.MyViewHolder> {
    private List<EventModel> list;
    private Context mContext;
    private boolean displayedInUser;

    public ProEventHistoryAdapter(List<EventModel> list, Context context) {
        this.list = list;
        this.mContext = context;
        this.displayedInUser = false;
    }

    public ProEventHistoryAdapter(List<EventModel> list, Context context, boolean displayedInUser) {
        this.list = list;
        this.mContext = context;
        this.displayedInUser = displayedInUser;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pro_profile_history_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EventModel pData = list.get(position);
        if (displayedInUser) {
            holder.eventTitle.setText(pData.getPhotographerName());
        } else {
            holder.eventTitle.setText(pData.getBookerUserName());
        }
        holder.eventLocation.setText(pData.getEventCity());
        holder.eventDescription.setText(pData.getNoteToPro());
        if (pData.getEventImagesUrls() != null) {
            Picasso.with(mContext).load(pData.getEventImagesUrls().get(0)).placeholder(mContext.getResources().getDrawable(R.drawable.test_image)).into(holder.eventImage);
        }
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
