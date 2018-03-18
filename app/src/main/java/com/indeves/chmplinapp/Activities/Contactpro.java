package com.indeves.chmplinapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.Models.UserData;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.StepProgressBar;
import com.kofigyan.stateprogressbar.StateProgressBar;

public class Contactpro extends StepProgressBar implements View.OnClickListener, FirebaseEventsListener {
    Button button;
    String proId;
    ProUserModel proUserModel;


    public Contactpro() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public Contactpro(String id) {
        this.proId = id;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.activity_contactpro, container, false);
        button = (Button) rootview.findViewById(R.id.button4);
        button.setOnClickListener(this);
        ReadData readData = new ReadData(this);
        readData.getUserInfoById(proId);


        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stateprogressbar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        stateprogressbar.setAllStatesCompleted(true);


    }

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {

    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            proUserModel = dataSnapshot.getValue(ProUserModel.class);
        } else {
            Toast.makeText(getContext(), "Error loading your data", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "well done", Toast.LENGTH_LONG).show();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + proUserModel.getPhone()));
        startActivity(callIntent);
    }


}
