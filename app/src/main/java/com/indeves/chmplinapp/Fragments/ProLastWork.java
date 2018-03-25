package com.indeves.chmplinapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Activities.ProLandingPage;
import com.indeves.chmplinapp.Adapters.LastWorkImagesAdapter;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.List;


public class ProLastWork extends Fragment implements FirebaseEventsListener {
    Context attachedActivityContext;
    ArrayList<String> images;
    LastWorkImagesAdapter lastWorkImagesAdapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;

    public ProLastWork() {
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
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        if (attachedActivityContext != null && ((ProLandingPage) attachedActivityContext).getSupportActionBar() != null) {
            ((ProLandingPage) attachedActivityContext).getSupportActionBar().setDisplayShowHomeEnabled(false);
            ((ProLandingPage) attachedActivityContext).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((ProLandingPage) attachedActivityContext).getSupportActionBar().setTitle(getResources().getString(R.string.fragment_title_last_work));
        }

        images = new ArrayList<>();
        lastWorkImagesAdapter = new LastWorkImagesAdapter(getContext(), images);
//        recyclerView.setAdapter(lastWorkImagesAdapter);
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
                if (eventModel != null && eventModel.getPhotographerId() != null && eventModel.getPhotographerId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
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
}
