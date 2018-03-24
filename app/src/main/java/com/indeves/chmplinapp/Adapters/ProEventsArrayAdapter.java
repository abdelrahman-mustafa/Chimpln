package com.indeves.chmplinapp.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.LookUpModel;
import com.indeves.chmplinapp.Models.PhotographerData;
import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProEventsArrayAdapter extends RecyclerView.Adapter<ProEventsArrayAdapter.MyViewHolder> {
    private List<LookUpModel> eventTypes, eventTimes, sharingOptions;
    private List<EventModel> list;
    private String displayType;

    public ProEventsArrayAdapter(List<EventModel> list, String displayType) {
        this.list = list;
        eventTypes = new ArrayList<>();
        eventTimes = new ArrayList<>();
        sharingOptions = new ArrayList<>();
        this.displayType = displayType;
        loadLookups();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_user_profile_tab_events_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EventModel pData = list.get(position);
        if (displayType.equals("user")) {
            holder.name.setText(pData.getPhotographerName());
        } else {
            holder.name.setText(pData.getBookerUserName());
        }

        String[] eventDateParts = pData.getEventDate().split("-");
        holder.day.setText(eventDateParts[0]);
//        holder.month.setText(eventDateParts[1]);
        if (eventTypes != null) {
            for (LookUpModel eventTypeElement : eventTypes) {
                if (pData.getTypeId() == eventTypeElement.getId()) {
                    holder.eventType.setText(eventTypeElement.getEnglishName());

                }
            }

        }
        if (sharingOptions != null) {
            for (LookUpModel sharingOptionElement : sharingOptions) {
                if (pData.getSharingOptionId() == sharingOptionElement.getId()) {
                    holder.share.setText(sharingOptionElement.getEnglishName());

                }
            }

        }
        if (pData.getStartTime() != null && !pData.getStartTime().isEmpty() && pData.getEndTime() != null && !pData.getEndTime().isEmpty()) {
            String timeString = pData.getStartTime() + " - " + pData.getEndTime();
            holder.time.setText(timeString);

        } else {
            if (eventTimes != null) {
                for (LookUpModel eventTimesElement : eventTimes) {
                    if (eventTimesElement.getId() == pData.getTimeId()) {
                        holder.time.setText(eventTimesElement.getEnglishName());

                    }
                }
            }
        }
        holder.location.setText(pData.getEventCity());
//        String[] monthsArray = {"Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
        holder.month.setText(eventDateParts[1]);
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public void loadLookups() {
        ReadData readData = new ReadData();
        readData.getLookupsByType("eventTypesLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> eventTypeLookups) {
                Log.v("EventTypeLookupsArr", eventTypeLookups.toString());
                eventTypes.addAll(eventTypeLookups);
            }
        });
        readData.getLookupsByType("eventTimesLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> lookups) {
                eventTimes.addAll(lookups);
            }
        });
        readData.getLookupsByType("shringOptionLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> lookups) {
                sharingOptions.addAll(lookups);
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, type, time, day, month, share, location, eventType;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.userProfile_event_photographer_name);
            time = view.findViewById(R.id.userProfile_event_time);
            day = view.findViewById(R.id.userProfile_event_day);
            month = view.findViewById(R.id.userProfile_event_month);
            share = view.findViewById(R.id.userProfile_event_shareOption);
            location = view.findViewById(R.id.userProfile_event_location);
            eventType = view.findViewById(R.id.userProfile_event_type);

        }
    }


}

