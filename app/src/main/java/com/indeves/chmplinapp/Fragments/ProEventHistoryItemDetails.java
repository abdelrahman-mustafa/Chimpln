package com.indeves.chmplinapp.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.CloudStorageAPI;
import com.indeves.chmplinapp.API.CloudStorageListener;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.API.WriteData;
import com.indeves.chmplinapp.Activities.ProRegActivity;
import com.indeves.chmplinapp.Adapters.AddImagesArrayAdapter;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.LookUpModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ClipData;

import static android.app.Activity.RESULT_OK;

public class ProEventHistoryItemDetails extends Fragment implements FirebaseEventsListener, View.OnClickListener, AddImagesArrayAdapter.ItemClickListener {
    private static final String ARG_PARAM1 = "SelectedEvent";
    private static int RESULT_LOAD_IMAGE = 1;
    List<LookUpModel> eventTypes, eventTimes, sharingOptions;
    ReadData readData;
    ProgressDialog progressDialog;
    RecyclerView imagesRecycleView;
    Button saveImages;
    AddImagesArrayAdapter addImagesArrayAdapter;
    ArrayList<Bitmap> eventImages;
    private EventModel selectedEvent;
    private TextView name, time, day, month, share, location, eventType;

    public ProEventHistoryItemDetails() {
    }

    public static ProEventHistoryItemDetails newInstance(EventModel selectedEvent) {
        ProEventHistoryItemDetails fragment = new ProEventHistoryItemDetails();
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
        View rootView = inflater.inflate(R.layout.fragment_pro_event_history_item_details, container, false);
        name = rootView.findViewById(R.id.userProfile_event_photographer_name);
        time = rootView.findViewById(R.id.userProfile_event_time);
        day = rootView.findViewById(R.id.userProfile_event_day);
        month = rootView.findViewById(R.id.userProfile_event_month);
        share = rootView.findViewById(R.id.userProfile_event_shareOption);
        location = rootView.findViewById(R.id.userProfile_event_location);
        eventType = rootView.findViewById(R.id.userProfile_event_type);
        saveImages = rootView.findViewById(R.id.save_event_photos_button);
        saveImages.setOnClickListener(this);
        imagesRecycleView = rootView.findViewById(R.id.card_recycler_view);
        int numberOfColumns = 4;
        eventImages = new ArrayList<>();
        eventImages.add(null);
        imagesRecycleView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        addImagesArrayAdapter = new AddImagesArrayAdapter(getContext(), eventImages);
        addImagesArrayAdapter.setClickListener(this);
        imagesRecycleView.setAdapter(addImagesArrayAdapter);
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


    @Override
    public void onClick(View v) {
        if (v == saveImages) {
            if (selectedEvent.getEventImagesUrls().size() < 10) {
                if (eventImages != null && eventImages.size() > 1 && (eventImages.size() + selectedEvent.getEventImagesUrls().size()) < 10) {
                    //TODO: @khalid This is wrong, upload icon should be isolated from the array from the beginning, let this go now and modify it ASAP
                    ArrayList<Bitmap> imagesToSend = eventImages;
                    imagesToSend.remove(eventImages.size() - 1);
                    progressDialog.show();
                    CloudStorageAPI cloudStorageAPI = new CloudStorageAPI();
                    cloudStorageAPI.UploadEventImages(selectedEvent.getEventId(), imagesToSend, new CloudStorageListener.UploadEventImagesListener() {
                        @Override
                        public void onImagesUploaded(ArrayList<String> imagesUrls) {
                            progressDialog.dismiss();
                            if (imagesUrls != null) {
                                Log.v("uploadImages", "Images uploaded successfully");
                                Log.v("UrlsArr", imagesUrls.toString());
                                EventModel eventModel = new EventModel();
                                eventModel.setEventImagesUrls(imagesUrls);
                                eventModel.setEventId(selectedEvent.getEventId());
                                WriteData writeData = new WriteData(ProEventHistoryItemDetails.this);
                                try {
                                    writeData.updateEventData(eventModel);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "You are not authenticated or your session is expired", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getContext(), "oops! Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Please pick the images you want to upload", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "you have already upload images before ", Toast.LENGTH_SHORT).show();

            }
        }

    }

    @Override
    public void onItemClick(View view, final int position) {
        if (eventImages.get(position) == null) {
            if (eventImages.size() < 11) {
                getImage();
            } else {
                Toast.makeText(getContext(), "You are not allowed to add more than 10 pics", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), RESULT_LOAD_IMAGE);

//        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK && reqCode == RESULT_LOAD_IMAGE) {
            try {
                if (data.getData() != null) {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                Bitmap.createScaledBitmap(selectedImage, 120, 120, false);
                    eventImages.add(0, selectedImage);
                    addImagesArrayAdapter.notifyDataSetChanged();
                } else if (data.getClipData() != null) {
                    ClipData clip = data.getClipData();

                    for (int i = 0; i < clip.getItemCount(); i++) {
                        ClipData.Item item = clip.getItemAt(i);
                        Uri uri = item.getUri();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(uri);
                        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        eventImages.add(0, selectedImage);
                        addImagesArrayAdapter.notifyDataSetChanged();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }
    }
}
