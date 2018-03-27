package com.indeves.chmplinapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Activities.ProLandingPage;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProProfile extends Fragment implements FirebaseEventsListener {
    Context attachedActivityContext;
    TextView proName, photosCount, eventsCount, subInfoRow, experience, workHours;
    RatingBar rate;
    ImageView profileImage;

    public ProProfile() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pro_profile, container, false);
        proName = rootView.findViewById(R.id.stu_profile_name);
        photosCount = rootView.findViewById(R.id.pro_profile_photos);
        eventsCount = rootView.findViewById(R.id.pro__profile_events);
        subInfoRow = rootView.findViewById(R.id.stu_profile_about);
        rate = rootView.findViewById(R.id.stu_profile_rating);
        experience = rootView.findViewById(R.id.pro_profile_experience);
        workHours = rootView.findViewById(R.id.pro_profile_work_hours);
        profileImage = rootView.findViewById(R.id.pro_profile_pic);
        setHasOptionsMenu(true);
        if (attachedActivityContext != null && ((ProLandingPage) attachedActivityContext).getSupportActionBar() != null) {
            ((ProLandingPage) attachedActivityContext).getSupportActionBar().setDisplayShowHomeEnabled(false);
            ((ProLandingPage) attachedActivityContext).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((ProLandingPage) attachedActivityContext).getSupportActionBar().setTitle(getResources().getString(R.string.fragment_title_my_profile));
        }
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ReadData readData = new ReadData(this);
            readData.getUserInfoById(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        ReadData readData2 = new ReadData(new FirebaseEventsListener() {
            @Override
            public void onWriteDataCompleted(boolean writeSuccessful) {

            }

            @Override
            public void onReadDataResponse(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    ArrayList<String> images = new ArrayList<>();
                    ArrayList<EventModel> eventModels = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        EventModel eventModel = dataSnapshot1.getValue(EventModel.class);
                        if (eventModel != null && eventModel.getPhotographerId() != null && eventModel.getPhotographerId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && eventModel.getEventStatus() != null && eventModel.getEventStatus().equals("finished")) {
                            eventModels.add(eventModel);
                            if (eventModel.getEventImagesUrls() != null) {
                                images.addAll(eventModel.getEventImagesUrls());
                            }

                        }
                    }
                    photosCount.setText(String.valueOf(images.size()));
                    eventsCount.setText(String.valueOf(eventModels.size()));

                }
            }
        });
        readData2.getAllEvents();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pro_profile_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.pro_profile_menu_edit) {
            //Go to edit screen
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.main_container, new ProEditProfileFragment());
            ft.addToBackStack(null);
            ft.commit();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.attachedActivityContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {

    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            ProUserModel proUserModel = dataSnapshot.getValue(ProUserModel.class);
            displayUserInfo(proUserModel);
        }

    }

    void displayUserInfo(ProUserModel proUserModel) {
        if (proUserModel != null) {
            if (proUserModel.getName() != null)
                proName.setText(proUserModel.getName());
            if (proUserModel.getEventsIds() != null)
                eventsCount.setText(String.valueOf(proUserModel.getEventsIds().size()));
            String about = "";
            if (proUserModel.getGender() != null)
                about = about + proUserModel.getGender();
            if (proUserModel.getArea() != null)
                about = about + ", " + proUserModel.getArea();
            subInfoRow.setText(about);
            if (proUserModel.getExperience() != null) {
                String experienceRow = proUserModel.getExperience() + " years of experience";
                experience.setText(experienceRow);
            }
            if (proUserModel.getWorkDayStart() != null && proUserModel.getWorkDayEnd() != null) {
                String workHoursRow = "Working hours      " + proUserModel.getWorkDayStart() + "    " + proUserModel.getWorkDayEnd();
                workHours.setText(workHoursRow);
            }
            if (proUserModel.getProfilePicUrl() != null) {
                Picasso.with(getContext()).load(proUserModel.getProfilePicUrl()).resize(300, 300).placeholder(R.drawable.user).transform(new CircleTransform()).error(R.drawable.user).into(profileImage);
            }
        }
    }
}
