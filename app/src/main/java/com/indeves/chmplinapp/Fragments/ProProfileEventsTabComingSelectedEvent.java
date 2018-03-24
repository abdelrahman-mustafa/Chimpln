package com.indeves.chmplinapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.API.WriteData;
import com.indeves.chmplinapp.Activities.Booking;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.LookUpModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.Toasts;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class ProProfileEventsTabComingSelectedEvent extends android.support.v4.app.Fragment implements View.OnClickListener, FirebaseEventsListener {
    TextView name, type, time, day, month, share, location, eventType, notes, fees, locationString;
    Button endEvent;
    EventModel eventModel;
    FragmentManager fragmentManager;

    @SuppressLint("ValidFragment")
    public ProProfileEventsTabComingSelectedEvent(EventModel eventModel) {
        this.eventModel = eventModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__pro_upcoming_event_selected, container, false);
        name = view.findViewById(R.id.userProfile_event_photographer_name);
        time = view.findViewById(R.id.userProfile_event_time);
        day = view.findViewById(R.id.userProfile_event_day);
        endEvent = view.findViewById(R.id.selected_event_end);
        month = view.findViewById(R.id.userProfile_event_month);
        share = view.findViewById(R.id.userProfile_event_shareOption);
        location = view.findViewById(R.id.userProfile_event_location);
        eventType = view.findViewById(R.id.userProfile_event_type);
        fees = view.findViewById(R.id.selected_event_fees);
        notes = view.findViewById(R.id.selected_event_notes);
        locationString = view.findViewById(R.id.selected_event_location);
        endEvent.setOnClickListener(this);
        initializeData();

        return view;
    }

    public void initializeData() {
        name.setText(eventModel.getBookerUserName());
        notes.setText(eventModel.getNoteToPro());
        time.setText(eventModel.getEventDate());
        location.setText(eventModel.getEventCity());
        locationString.setText(eventModel.getEventCity());
        share.setText(String.valueOf(eventModel.getSharingOptionId()));
        fees.setText("xxxxxxx");

    }

    @Override
    public void onClick(View v) {
        if (v == endEvent) {
            WriteData finishEvent = new WriteData(this);
            String eventId = eventModel.getEventId();

            try {
                finishEvent.respondToEvent("finished", eventId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {
        if (writeSuccessful) {
            fragmentManager = getActivity().getSupportFragmentManager();
            ProProfileEventsTabUpComming frag = new ProProfileEventsTabUpComming();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, frag).commit();
        } else {
            Toasts toasts = new Toasts(getContext());
            toasts.wrongMe();
        }
    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {

    }
}
