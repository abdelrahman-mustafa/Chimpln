package com.indeves.chmplinapp.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.API.WriteData;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.LookUpModel;
import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.List;


public class ProEventResponse extends Fragment implements View.OnClickListener, FirebaseEventsListener {
    private static final String ARG_PARAM1 = "selectedEvent";
    List<LookUpModel> eventTypes, eventTimes, sharingOptions;
    ReadData readData;
    ProgressDialog progressDialog;
    private EventModel selectedEvent;
    private Button accept, reject;
    private TextView name, time, day, month, share, location, eventType;

    public ProEventResponse() {
        // Required empty public constructor
    }

    public static ProEventResponse newInstance(EventModel eventModel) {
        ProEventResponse fragment = new ProEventResponse();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, eventModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedEvent = (EventModel) getArguments().getSerializable(ARG_PARAM1);
        }
        eventTimes = new ArrayList<>();
        eventTypes = new ArrayList<>();
        sharingOptions = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please wait.. ");
        progressDialog.show();
        loadLookups();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pro_event_response, container, false);
        name = rootView.findViewById(R.id.userProfile_event_photographer_name);
        time = rootView.findViewById(R.id.userProfile_event_time);
        day = rootView.findViewById(R.id.userProfile_event_day);
        month = rootView.findViewById(R.id.userProfile_event_month);
        share = rootView.findViewById(R.id.userProfile_event_shareOption);
        location = rootView.findViewById(R.id.userProfile_event_location);
        eventType = rootView.findViewById(R.id.userProfile_event_type);
        accept = rootView.findViewById(R.id.pro_Accept_button);
        accept.setOnClickListener(this);
        reject = rootView.findViewById(R.id.pro_Reject_button);
        reject.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        progressDialog.show();
        if (v == accept) {
            WriteData writeData = new WriteData(this);
            try {
                writeData.respondToEvent("accepted", selectedEvent.getEventId());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (v == reject) {
            WriteData writeData = new WriteData(this);
            try {
                writeData.respondToEvent("rejected", selectedEvent.getEventId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void displayEventData() {
        if (selectedEvent != null) {
            name.setText(selectedEvent.getBookerUserName());
            String[] eventDateParts = selectedEvent.getEventDate().split("-");
            day.setText(eventDateParts[0]);
//        month.setText(eventDateParts[1]);
            if (eventTypes != null) {
                for (LookUpModel eventTypeElement : eventTypes) {
                    if (selectedEvent.getTypeId() == eventTypeElement.getId()) {
                        eventType.setText(eventTypeElement.getEnglishName());

                    }
                }

            }
            if (sharingOptions != null) {
                for (LookUpModel sharingOptionElement : sharingOptions) {
                    if (selectedEvent.getSharingOptionId() == sharingOptionElement.getId()) {
                        share.setText(sharingOptionElement.getEnglishName());

                    }
                }

            }
            if (selectedEvent.getStartTime() != null && selectedEvent.getEndTime() != null) {
                String timeString = selectedEvent.getStartTime() + " - " + selectedEvent.getEndTime();
                time.setText(timeString);

            } else {
                if (eventTimes != null) {
                    for (LookUpModel eventTimesElement : eventTimes) {
                        if (eventTimesElement.getId() == selectedEvent.getTimeId()) {
                            time.setText(eventTimesElement.getEnglishName());

                        }
                    }
                }
            }
            location.setText(selectedEvent.getEventCity());
            month.setText(eventDateParts[1]);

        }
    }

    public void loadLookups() {
        readData = new ReadData();
        //using nested calls is to make sure that all lookups are loaded
        readData.getLookupsByType("eventTypesLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> eventTypeLookups) {
                eventTypes.addAll(eventTypeLookups);
                readData.getLookupsByType("eventTimesLookups", new ReadData.LookUpsListener() {
                    @Override
                    public void onLookUpsResponse(List<LookUpModel> lookups) {
                        eventTimes.addAll(lookups);
                        readData.getLookupsByType("shringOptionLookups", new ReadData.LookUpsListener() {
                            @Override
                            public void onLookUpsResponse(List<LookUpModel> lookups) {
                                progressDialog.dismiss();
                                sharingOptions.addAll(lookups);
                                displayEventData();
                            }
                        });
                    }
                });
            }
        });


    }

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {
        progressDialog.dismiss();
        if (writeSuccessful) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                fragmentManager.popBackStack();
            }
        }

    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {

    }
}
