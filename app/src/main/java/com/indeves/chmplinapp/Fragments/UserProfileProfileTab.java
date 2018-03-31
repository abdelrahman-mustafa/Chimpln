package com.indeves.chmplinapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.API.WriteData;
import com.indeves.chmplinapp.Activities.LogIn;
import com.indeves.chmplinapp.Models.UserData;
import com.indeves.chmplinapp.PrefsManager.PrefSave;
import com.indeves.chmplinapp.R;

public class UserProfileProfileTab extends android.support.v4.app.Fragment implements FirebaseEventsListener, View.OnClickListener {

    TextView email, mobileNumber;
    Button logout;

    public UserProfileProfileTab() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        email = rootView.findViewById(R.id.email);
        mobileNumber = rootView.findViewById(R.id.userMobileNumber_textView);
        logout = rootView.findViewById(R.id.userProfile_button_logout);
        logout.setOnClickListener(this);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ReadData readData = new ReadData(this);
            readData.getUserInfoById(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        return rootView;

    }

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {
        if (writeSuccessful) {
            Toast.makeText(getContext(), "Your profile is updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "oops! something wrong happened!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            UserData userData = dataSnapshot.getValue(UserData.class);
            displayUserData(userData);

        } else {
            Toast.makeText(getContext(), "Error loading your data", Toast.LENGTH_SHORT).show();
        }

    }

    public void displayUserData(UserData userData) {
        email.setText(userData.getEmail() != null ? userData.getEmail() : "");
        mobileNumber.setText(userData.getPhone() != null ? userData.getPhone() : "");


    }

    @Override
    public void onClick(View v) {
        if (v == logout) {
            PrefSave prefSave = new PrefSave(getContext());
            prefSave.saveLogInStatus(false);

            startActivity(new Intent(getActivity(), LogIn.class));
            getActivity().finish();

        }


    }

}
