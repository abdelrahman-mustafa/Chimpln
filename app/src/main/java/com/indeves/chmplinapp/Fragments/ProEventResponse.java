package com.indeves.chmplinapp.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
    ArrayList<EventModel> proEvents;

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
        getProEvents();
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
        if (v == accept) {
            int eventsCount = 0;
            for (EventModel eventModel : proEvents) {
                if (eventModel.getEventDate().equals(selectedEvent.getEventDate())) {
                    eventsCount++;
                }
            }
            if (eventsCount > 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Reminder");
                alertDialog.setMessage("You have " + String.valueOf(eventsCount) + "accepted events in the same day of this event!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressDialog.show();
                                WriteData writeData = new WriteData(ProEventResponse.this);
                                try {
                                    writeData.respondToEvent("accepted", selectedEvent.getEventId());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                alertDialog.show();

            } else {
                progressDialog.show();
                WriteData writeData = new WriteData(this);
                try {
                    writeData.respondToEvent("accepted", selectedEvent.getEventId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else if (v == reject) {
            progressDialog.show();
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
            if (selectedEvent.getStartTime() != null && !selectedEvent.getStartTime().isEmpty() && selectedEvent.getEndTime() != null && !selectedEvent.getEndTime().isEmpty()) {
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
                                            displayEventData();
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

    void getProEvents() {
        ReadData readData = new ReadData(this);
        readData.getEventsByProId(FirebaseAuth.getInstance().getCurrentUser().getUid());
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
        if (dataSnapshot != null) {
            proEvents = new ArrayList<>();
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                EventModel eventModel = dataSnapshot1.getValue(EventModel.class);
                if (eventModel != null && eventModel.getEventStatus().equals("accepted")) {
                    proEvents.add(eventModel);
                }
            }
        }
    }
}
