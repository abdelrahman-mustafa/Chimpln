package com.indeves.chmplinapp.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.LookUpModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.StepProgressBar;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.List;

public class Rejected extends StepProgressBar implements FirebaseEventsListener {
    Button newEvent;
    EventModel model;
    ProUserModel proUserModel;
    public Rejected() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public Rejected(EventModel model) {
        this.model = model;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.activity_rejected, container, false);
        newEvent=(Button)rootview.findViewById(R.id.rejected_button);
        newEvent.setOnClickListener(this);
        ReadData readData = new ReadData(this);
        readData.getUserInfoById(model.getPhotographerId());



        return rootview;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //stateprogressbar.setTransitionName("Rejected");
        stateprogressbar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        stateprogressbar.setAllStatesCompleted(false);





    }

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {

    }

    public void onReadDataResponse(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null)
        { proUserModel = dataSnapshot.getValue(ProUserModel.class);
        } else {
            Toast.makeText(getContext(), "Error loading your data", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onClick(View v) {
        if (v==newEvent){

            Booking output = new Booking(proUserModel," ");
            android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.container_o, output).commit();
        }


    }


}
