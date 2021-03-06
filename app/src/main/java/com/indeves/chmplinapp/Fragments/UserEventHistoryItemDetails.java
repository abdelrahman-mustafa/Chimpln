package com.indeves.chmplinapp.Fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.core.executor.DefaultTaskExecutor;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.CloudStorageAPI;
import com.indeves.chmplinapp.API.CloudStorageListener;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.API.WriteData;
import com.indeves.chmplinapp.Adapters.AddImagesArrayAdapter;
import com.indeves.chmplinapp.Adapters.UserProLastWorkImagesAdapter;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.LookUpModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.ClickListener;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class UserEventHistoryItemDetails extends Fragment implements FirebaseEventsListener, View.OnClickListener {
    private static final String ARG_PARAM1 = "SelectedEvent";
    private static int RESULT_LOAD_IMAGE = 1;
    List<LookUpModel> eventTypes, eventTimes, sharingOptions;
    ReadData readData;
    ArrayList<String> images;
    ProgressDialog progressDialog;
    //    RelativeLayout backButtonLayout;
    UserProLastWorkImagesAdapter lastWorkImagesAdapter;
    private RatingBar ratingBar;
    private Button btnSubmit;
    RecyclerView recyclerView;
    private EventModel selectedEvent;
    private TextView name, time, day, month, share, location, eventType;

    public UserEventHistoryItemDetails() {
    }

    public static UserEventHistoryItemDetails newInstance(EventModel selectedEvent) {
        UserEventHistoryItemDetails fragment = new UserEventHistoryItemDetails();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, selectedEvent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedEvent = (EventModel) getArguments().getSerializable(ARG_PARAM1);
        }
        getActivity().setTitle("Event history");
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
        View rootView = inflater.inflate(R.layout.fragment_user_event_history_item_details, container, false);
        name = rootView.findViewById(R.id.userProfile_event_photographer_name);
        time = rootView.findViewById(R.id.userProfile_event_time);
        day = rootView.findViewById(R.id.userProfile_event_day);
        month = rootView.findViewById(R.id.userProfile_event_month);
        share = rootView.findViewById(R.id.userProfile_event_shareOption);
        location = rootView.findViewById(R.id.userProfile_event_location);
        eventType = rootView.findViewById(R.id.userProfile_event_type);
//        backButtonLayout = rootView.findViewById(R.id.backButtonLayout);
//        backButtonLayout.setOnClickListener(this);

        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (selectedEvent.isIsRated()) {

                    Toast.makeText(getContext(), "you can't rate more than one time", Toast.LENGTH_SHORT).show();

                } else {
                    // upload rate
                    // get data of photographer tp get his rates
                    ReadData readData = new ReadData(new FirebaseEventsListener() {
                        @Override
                        public void onWriteDataCompleted(boolean writeSuccessful) {

                        }

                        @Override
                        public void onReadDataResponse(DataSnapshot dataSnapshot) {

                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                ProUserModel proUserModel = dataSnapshot.getValue(ProUserModel.class);
                                // here retuturned all rates of this photographer
                                List<Integer> rates = new ArrayList<>();
                                if (proUserModel.getRates() == null) {
                                    proUserModel.setRate(Math.round(ratingBar.getRating()));
                                    Log.d("ds", String.valueOf(Math.round(ratingBar.getRating())));
                                    rates.add(Math.round(ratingBar.getRating()));
                                    proUserModel.setRates(rates);

                                } else {
                                    rates.addAll(proUserModel.getRates());
                                    double totalRate = calculateAverage(rates);
                                    proUserModel.setRate(totalRate);
                                    Log.d("tota", String.valueOf(totalRate));
                                    rates.add(Math.round(ratingBar.getRating()));
                                    proUserModel.setRates(rates);

                                }
                                // update the list of rate with new rate

                                WriteData writeData = new WriteData(new FirebaseEventsListener() {
                                    @Override
                                    public void onWriteDataCompleted(boolean writeSuccessful) {
                                        if (writeSuccessful) {
                                            Toast.makeText(getContext(), "Thanks for rating", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void onReadDataResponse(DataSnapshot dataSnapshot) {

                                    }
                                });
                                try {
                                    writeData.updateProData(proUserModel);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // here update the event (is rated ) to be true to prevent multiple ratings
                                WriteData writeData1 = new WriteData(new FirebaseEventsListener() {
                                    @Override
                                    public void onWriteDataCompleted(boolean writeSuccessful) {
                                        if (writeSuccessful) {
                                            Toast.makeText(getContext(), "Thanks for rating1", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void onReadDataResponse(DataSnapshot dataSnapshot) {

                                    }
                                });
                                try {
                                    selectedEvent.setIsRated(true);
                                    writeData1.updateEventData(selectedEvent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getContext(), String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(getContext(), "Failed to load your data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Log.i("id", selectedEvent.getPhotographerId());
                    Log.i("id", selectedEvent.getEventId());

                    readData.readUserInfo(selectedEvent.getPhotographerId());

                }

            }

        });

        recyclerView = rootView.findViewById(R.id.card_recycler_view);
        int numberOfColumns = 4;
//        eventImages = new ArrayList<>();
//        eventImages.add(null);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                // inlarge the view of the selected image from last work
                Dialog settingsDialog = new Dialog(getContext());
                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout
                        , null));
                ImageView imageVie = settingsDialog.findViewById(R.id.photo_pic_image);
                Picasso.with(getContext()).load(images.get(position)).into(imageVie);
                settingsDialog.show();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        images = new ArrayList<>();
        lastWorkImagesAdapter = new UserProLastWorkImagesAdapter(getContext(), images);
        recyclerView.setAdapter(lastWorkImagesAdapter);
        return rootView;
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

    private double calculateAverage(List<Integer> marks) {
        Integer sum = 0;
        if (!marks.isEmpty()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }

    private void displayEventData() {

        if (selectedEvent != null) {
            if (selectedEvent.getEventImagesUrls() != null) {
                Log.v("EventHistoryImages", selectedEvent.getEventImagesUrls().toString());
                images.addAll(selectedEvent.getEventImagesUrls());
                lastWorkImagesAdapter.notifyDataSetChanged();
            }
            name.setText(selectedEvent.getPhotographerName());
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


    @Override
    public void onClick(View v) {
//        if (v == backButtonLayout) {
//            FragmentManager fragmentManager = getFragmentManager();
//            if (fragmentManager != null) {
//                fragmentManager.popBackStack();
//            }
//        }

    }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


}
