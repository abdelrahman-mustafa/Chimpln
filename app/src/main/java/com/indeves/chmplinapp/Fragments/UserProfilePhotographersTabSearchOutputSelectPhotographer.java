package com.indeves.chmplinapp.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.API.WriteData;
import com.indeves.chmplinapp.Activities.Booking;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.LookUpModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class UserProfilePhotographersTabSearchOutputSelectPhotographer extends android.support.v4.app.Fragment {


    ProUserModel pros = new ProUserModel();
    //    ProgressDialog progressDialog;
    Spinner eventTypeSpinner;
    List<LookUpModel> eventTypesList = new ArrayList<>();
    ArrayAdapter<LookUpModel> eventTypeArrayAdapter;
    Button createEvent;
    TextView name, about, events, photos;

    @SuppressLint("ValidFragment")
    public UserProfilePhotographersTabSearchOutputSelectPhotographer(ProUserModel pros) {
        this.pros = pros;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment__user_photographer_profile, container, false);
        eventTypeSpinner = rootView.findViewById(R.id.userProfile_phot_spinner_type);
        createEvent = rootView.findViewById(R.id.userProfile_button_create);
        name = rootView.findViewById(R.id.userProfile_pro_name);
        about = rootView.findViewById(R.id.userProfile_pro_about);
        events = rootView.findViewById(R.id.userProfile_pro_events);
        photos = rootView.findViewById(R.id.userProfile_pro_photos);

        name.setText(pros.getName());
        name.setGravity(Gravity.CENTER_HORIZONTAL);

        about.setText(pros.getGender() + ", " + pros.getCity() + ", " + pros.getExperience());
        about.setGravity(Gravity.CENTER_HORIZONTAL);
    /*    if (pros.getEventsIds() != null) {
            events.setText(pros.getEventsIds().size());
        }else {
            events.setText(0);
        }*/
        photos.setText("0");
        events.setText("0");

        eventTypesList.add(0, new LookUpModel(0, getResources().getString(R.string.selectPackTy)));
        // Creating adapter for spinner
        eventTypeArrayAdapter = new ArrayAdapter<LookUpModel>(getContext(), android.R.layout.simple_spinner_item, eventTypesList);
        // Drop down layout style - list view with radio button
        eventTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        eventTypeSpinner.setAdapter(eventTypeArrayAdapter);
        ReadData readData = new ReadData();
        readData.getLookupsByType("eventTypesLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> eventTypeLookups) {
                Log.v("EventTypeLookupsArr", eventTypeLookups.toString());
                eventTypeArrayAdapter.addAll(eventTypeLookups);
            }
        });


        createEvent.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                getActivity().findViewById(R.id.userProfile_LinearLayout).setVisibility(View.GONE);
                //  go to booking  activity
                Booking output = new Booking(pros, eventTypeSpinner.getSelectedItem().toString());
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.container_o, output).commit();


                //Khalid, example of creating booking event
//                progressDialog = new ProgressDialog(getContext());
//                progressDialog.setMessage("Please wait...");
//                progressDialog.setCanceledOnTouchOutside(false);
//                progressDialog.show();
//                EventModel eventModel = new EventModel(FirebaseAuth.getInstance().getCurrentUser().getUid(), pros.getUid(), "19-11-2018", "Mostafa", pros.getName(), null, null, "Hey", "Giza", 35, 35, "pending", 1, 1, 1);
//                WriteData writeData = new WriteData(firebaseEventsListener);
//                try {
//                    writeData.bookNewEvent(eventModel);
//                } catch (Exception e) {
//                    //User is not authenticated
//                    e.printStackTrace();
//                }

            }
        });
        return rootView;
    }

    //    private FirebaseEventsListener firebaseEventsListener = new FirebaseEventsListener() {
    //        @Override
    //        public void onWriteDataCompleted(boolean writeSuccessful) {
    //            progressDialog.dismiss();
    //            if (writeSuccessful) {
    //                Toast.makeText(getContext(), "Done", Toast.LENGTH_LONG).show();
    //            }
    //        }
    //
    //        @Override
    //        public void onReadDataResponse(DataSnapshot dataSnapshot) {
    //
    //        }
    //    };
}
