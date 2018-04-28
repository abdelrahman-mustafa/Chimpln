package com.indeves.chmplinapp.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private static final int PERMISSION_REQUEST_CODE = 8;
    Button button;
    String proId;
    ProUserModel proUserModel;
    double paymentAmount;
    String paymentMethod;
    TextView paymentDetailsTextView;

    public Contactpro() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public Contactpro(String id, double paymentAmount, String paymentMethod) {
        this.proId = id;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
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
        paymentDetailsTextView = rootview.findViewById(R.id.contactPro_paymentDetails_textView);
        if (paymentMethod.equals("booked-credit")) {
            double paymentAmountInLE = paymentAmount / 100;
            String paymentDetailText = "Your credit card will be charged for " + String.valueOf(paymentAmountInLE) + " L.E. to confirm your request";
            paymentDetailsTextView.setText(paymentDetailText);
        }

        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stateprogressbar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
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
        if (v == button) {
            makeCall();
        }

        //callIntent.setData(Uri.parse("tel:" + proUserModel.getPhone()));
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();
                }
                break;
        }
    }

    public void makeCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + proUserModel.getPhone()));
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {

            startActivity(intent);

        } else {
            Toast.makeText(getContext(), "We need call permission", Toast.LENGTH_SHORT).show();
        }
    }


}




