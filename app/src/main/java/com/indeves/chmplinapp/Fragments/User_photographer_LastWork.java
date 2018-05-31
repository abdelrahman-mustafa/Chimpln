package com.indeves.chmplinapp.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Activities.StuLandingPage;
import com.indeves.chmplinapp.Activities.UserProfileMain;
import com.indeves.chmplinapp.Adapters.LastWorkImagesAdapter;
import com.indeves.chmplinapp.Adapters.UserProLastWorkImagesAdapter;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.ClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.view.View.GONE;


public class User_photographer_LastWork extends Fragment implements FirebaseEventsListener {
    Context attachedActivityContext;
    ArrayList<String> images;
    UserProLastWorkImagesAdapter lastWorkImagesAdapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;

    String Uid;

    @SuppressLint("ValidFragment")
    public User_photographer_LastWork(String Uid) {

        this.Uid = Uid;
    }


    public User_photographer_LastWork() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please wait.. ");
        progressDialog.show();
        ReadData readData = new ReadData(this);
        readData.getAllEvents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pro_last_work, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new User_photographer_LastWork.RecyclerTouchListener(
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
        return rootView;

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
        if (dataSnapshot != null) {
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                EventModel eventModel = dataSnapshot1.getValue(EventModel.class);
                if (eventModel != null && eventModel.getPhotographerId() != null && eventModel.getPhotographerId().equals(Uid) && eventModel.getEventStatus().equals("finished")) {
                    if (eventModel.getEventImagesUrls() != null) {
                        images.addAll(eventModel.getEventImagesUrls());
                    }

                }
            }
            progressDialog.dismiss();
            recyclerView.setAdapter(lastWorkImagesAdapter);
            lastWorkImagesAdapter.notifyDataSetChanged();
        }
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
