package com.indeves.chmplinapp.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.API.WriteData;

import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.LookUpModel;

import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.Toasts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressLint("ValidFragment")
public class ProProfileEventsTabComingSelectedEvent extends android.support.v4.app.Fragment implements View.OnClickListener, FirebaseEventsListener {
    TextView name, type, time, day, month, share, location, eventType, notes, fees, locationString, eventPackage, eventTypeAgainWithoutReason, sharingOptionAgainwithNoReason;
    Button endEvent;
    EventModel eventModel;
    List<LookUpModel> eventTypes, eventTimes, sharingOptions;
    ReadData readData;
    ProgressDialog progressDialog;

    @SuppressLint("ValidFragment")
    public ProProfileEventsTabComingSelectedEvent(EventModel eventModel) {
        this.eventModel = eventModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventTimes = new ArrayList<>();
        eventTypes = new ArrayList<>();
        sharingOptions = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please wait.. ");
        progressDialog.show();
        loadLookups();
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
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
        eventPackage = view.findViewById(R.id.selected_event_package);
        eventTypeAgainWithoutReason = view.findViewById(R.id.selected_event_type);
        sharingOptionAgainwithNoReason = view.findViewById(R.id.selected_event_sharing_option);
        endEvent.setOnClickListener(this);
        return view;
    }

    public void initializeData() {

        if (eventModel != null) {
            name.setText(eventModel.getBookerUserName());
            notes.setText(eventModel.getNoteToPro());
            locationString.setText(eventModel.getEventCity());
            String[] eventDateParts = eventModel.getEventDate().split("-");
            day.setText(eventDateParts[0]);
            month.setText(eventDateParts[1]);

            if (eventTypes != null) {
                for (LookUpModel eventTypeElement : eventTypes) {
                    if (eventModel.getTypeId() == eventTypeElement.getId()) {
                        eventType.setText(eventTypeElement.getEnglishName());
                        eventTypeAgainWithoutReason.setText(eventTypeElement.getEnglishName());

                    }
                }

            }
            if (sharingOptions != null) {
                for (LookUpModel sharingOptionElement : sharingOptions) {
                    if (eventModel.getSharingOptionId() == sharingOptionElement.getId()) {
                        share.setText(sharingOptionElement.getEnglishName());
                        sharingOptionAgainwithNoReason.setText(sharingOptionElement.getEnglishName());
                    }
                }

            }
            if (eventModel.getStartTime() != null && !eventModel.getStartTime().isEmpty() && eventModel.getEndTime() != null && !eventModel.getEndTime().isEmpty()) {
                String timeString = eventModel.getStartTime() + " - " + eventModel.getEndTime();
                time.setText(timeString);

            } else {
                if (eventTimes != null) {
                    for (LookUpModel eventTimesElement : eventTimes) {
                        if (eventTimesElement.getId() == eventModel.getTimeId()) {
                            time.setText(eventTimesElement.getEnglishName());

                        }
                    }
                }
            }
            location.setText(eventModel.getEventCity());

            if (eventModel.getSelectedPackage() != null) {
                eventPackage.setText(eventModel.getSelectedPackage().getPackageTitle());
            }
            try {
                if (checkIfUserCanEndEvent(eventModel.getEventDate())) {
                    endEvent.setText(getResources().getString(R.string.end_event_button));
                } else {
                    endEvent.setText(getResources().getString(R.string.cancel_event_button));
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error parsing event date", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onClick(View v) {
        if (v == endEvent) {
            try {
                final WriteData finishEvent = new WriteData(this);
                final String eventId = eventModel.getEventId();
                if (checkIfUserCanEndEvent(eventModel.getEventDate())) {
                    finishEvent.respondToEvent("finished", eventId);
                } else {
                    finishEvent.respondToEvent("cancelled", eventId);

                  /*  AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle(getResources().getString(R.string.cancel_event_button))
                            .setMessage(getResources().getString(R.string.cancel_event_confirmation))
                            .setPositiveButton(getResources().getString(R.string.cancel_event_button), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        finishEvent.respondToEvent("cancelled", eventId);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).setCancelable(false).create();
                    dialog.show();*/
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error parsing event date", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "You are not logged in", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {
        if (writeSuccessful) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                fragmentManager.popBackStack();
            }
        } else {
            Toasts toasts = new Toasts(getContext());
            toasts.wrongMe();
        }
    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {

    }

    public void loadLookups() {
        readData = new ReadData();
        //using nested calls is to make sure that all lookups are loaded
        readData.getLookupsByType("eventTypesLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> eventTypeLookups) {
                if (eventTypeLookups != null) {
                    eventTypes.addAll(eventTypeLookups);
                    readData.getLookupsByType("eventTimesLookups", new ReadData.LookUpsListener() {
                        @Override
                        public void onLookUpsResponse(List<LookUpModel> lookups) {
                            if (lookups != null) {
                                eventTimes.addAll(lookups);
                                readData.getLookupsByType("shringOptionLookups", new ReadData.LookUpsListener() {
                                    @Override
                                    public void onLookUpsResponse(List<LookUpModel> lookups) {
                                        progressDialog.dismiss();
                                        if (lookups != null) {
                                            sharingOptions.addAll(lookups);
                                            initializeData();
                                        } else {
                                            Toast.makeText(getContext(), "Failed to load event data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Failed to load event data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failed to load event data", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    boolean checkIfUserCanEndEvent(String eventDateString) throws ParseException {

        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yyyy");
            Date eventDate = formatter2.parse(eventDateString);
            Date today = new Date();
            long oneDayDifference = 1000 * 60 * 60 * 24;
            return ((today.getTime() - eventDate.getTime()) > oneDayDifference);
        } catch (ParseException e) {
            e.printStackTrace();
            throw e;
        }

    }
}
