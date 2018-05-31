package com.indeves.chmplinapp.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.API.WriteData;
import com.indeves.chmplinapp.Activities.Booking;
import com.indeves.chmplinapp.Adapters.LastWorkImagesAdapter;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.LookUpModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class UserProfilePhotographersTabSearchOutputSelectPhotographer extends android.support.v4.app.Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    ProUserModel pros = new ProUserModel();
    //    ProgressDialog progressDialog;
    //  Spinner eventTypeSpinner;
    List<LookUpModel> eventTypesList = new ArrayList<>();
    ArrayAdapter<LookUpModel> eventTypeArrayAdapter;
    Button createEvent;
    TextView name, about, events, photos;
    ImageView imageView;
    ArrayList<String> images;
    LastWorkImagesAdapter lastWorkImagesAdapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;

    @SuppressLint("ValidFragment")
    public UserProfilePhotographersTabSearchOutputSelectPhotographer(ProUserModel pros) {
        this.pros = pros;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment__user_photographer_profile, container, false);
        //eventTypeSpinner = rootView.findViewById(R.id.userProfile_phot_spinner_type);
        createEvent = rootView.findViewById(R.id.userProfile_button_create);
        name = rootView.findViewById(R.id.userProfile_pro_name);
        imageView = rootView.findViewById(R.id.pro_profile_pic);
        // recyclerView = rootView.findViewById(R.id.selected_pro_images);
        about = rootView.findViewById(R.id.userProfile_pro_about);
        events = rootView.findViewById(R.id.userProfile_pro_events);
        photos = rootView.findViewById(R.id.userProfile_pro_photos);
        //    recyclerView.setHasFixedSize(true);
        // RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        //  recyclerView.setLayoutManager(layoutManager);
        name.setText(pros.getName());
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        images = new ArrayList<>();
        lastWorkImagesAdapter = new LastWorkImagesAdapter(getContext(), images);
        viewPager = (ViewPager) rootView.findViewById(R.id.container_special);
        setupViewPager(viewPager);
        tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorHeight(25);
        tabLayout.setSelectedTabIndicatorColor(R.color.peach);
        tabLayout.setupWithViewPager(viewPager);


        about.setText(pros.getGender() + ", " + pros.getCity() + ", " + pros.getExperience());
        about.setGravity(Gravity.CENTER_HORIZONTAL);


        // set the number of events and photos to the selected pro
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
                        if (eventModel != null && eventModel.getPhotographerId() != null && eventModel.getPhotographerId().equals(pros.getUid()) && eventModel.getEventStatus() != null && eventModel.getEventStatus().equals("finished")) {
                            eventModels.add(eventModel);
                            if (eventModel.getEventImagesUrls() != null) {
                                images.addAll(eventModel.getEventImagesUrls());
                            }

                        }
                    }
                    photos.setText(String.valueOf(images.size()));
                    events.setText(String.valueOf(eventModels.size()));

                }
            }
        });
        readData2.getAllEvents();


        Picasso.with(getContext()).load(pros.getProfilePicUrl()).resize(200, 200).transform(new CircleTransform()).into(imageView);


        eventTypesList.add(0, new LookUpModel(0, getResources().getString(R.string.selectPackTy)));
        // Creating adapter for spinner
        eventTypeArrayAdapter = new ArrayAdapter<LookUpModel>(getContext(), android.R.layout.simple_spinner_item, eventTypesList);
        // Drop down layout style - list view with radio button
        eventTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

/*
        eventTypeSpinner.setAdapter(eventTypeArrayAdapter);
        ReadData readData = new ReadData();
        readData.getLookupsByType("eventTypesLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> eventTypeLookups) {
                Log.v("EventTypeLookupsArr", eventTypeLookups.toString());
                eventTypeArrayAdapter.addAll(eventTypeLookups);

            }
        });*/


/*
        eventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
               *//* progressDialog = new ProgressDialog(getContext());
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Please wait.. ");
                progressDialog.show();
                ReadData readDat = new ReadData(firebaseEventsListener);
                readDat.getEventsByProId(pros.getUid());*//*
               if (position!=0){
                Toast.makeText(getContext(), "Hi",Toast.LENGTH_SHORT).show();
                   ReadData readDat = new ReadData(firebaseEventsListener);
                   readDat.getEventsByProId(pros.getUid());
               }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });*/
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog settingsDialog = new Dialog(getContext());
                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout
                        , null));
                ImageView imageVie = settingsDialog.findViewById(R.id.photo_pic_image);
                Picasso.with(getContext()).load(pros.getProfilePicUrl()).into(imageVie);
                settingsDialog.show();
            }
        });


        createEvent.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                getActivity().findViewById(R.id.userProfile_LinearLayout).setVisibility(View.GONE);
                //  go to booking  activity
                Booking output = new Booking(pros, null);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.container_o, output).addToBackStack("tag").commit();


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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new User_photographer_LastWork(pros.getUid()), "Last Work");
        adapter.addFragment(new Usere_photographer_Events(pros.getUid()), "Events");
        adapter.addFragment(new User_photographer_Packages(pros.getUid()), "Packages");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
/*
    private FirebaseEventsListener firebaseEventsListener = new FirebaseEventsListener() {
        @Override
        public void onWriteDataCompleted(boolean writeSuccessful) {

        }

        @Override
        public void onReadDataResponse(DataSnapshot dataSnapshot) {
            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                Log.v("AllProEvents", dataSnapshot.getValue().toString());

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    EventModel eventModel = dataSnapshot1.getValue(EventModel.class);
                    if (eventModel.getEventImagesUrls() != null && eventTypeSpinner.getSelectedItemPosition() == (eventModel.getTypeId())) {
                        images.addAll(eventModel.getEventImagesUrls());
                    }

                }
                progressDialog.dismiss();
                recyclerView.setAdapter(lastWorkImagesAdapter);
                lastWorkImagesAdapter.notifyDataSetChanged();
            }
        }


    };*/
}
