package com.indeves.chmplinapp.API;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indeves.chmplinapp.Activities.LogIn;
import com.indeves.chmplinapp.Activities.ProLandingPage;
import com.indeves.chmplinapp.Activities.StuLandingPage;
import com.indeves.chmplinapp.Activities.UserProfileMain;
import com.indeves.chmplinapp.Models.UserData;
import com.indeves.chmplinapp.PrefsManager.PrefSave;
import com.indeves.chmplinapp.PrefsManager.PrefsManager;

/**
 * Created by boda on 2/25/18.
 */

public class ReadData {
    private UserData user;
    private FirebaseEventsListener firebaseEventsListener;

    public ReadData() {

    }

    public ReadData(FirebaseEventsListener firebaseEventsListener) {
        this.firebaseEventsListener = firebaseEventsListener;
    }

    public void readUserInfo(final String userId, final Context context) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            //changed it to single value event to disable firing this code again if user's data is changed in edit profile
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserData.class);
                if (user != null)
                    Log.i("MyUserData", user.toString());
                PrefSave prefSave = new PrefSave(context);
                prefSave.saveUserType(user.type.trim());
                PrefsManager prefsManager = new PrefsManager(context);
                prefsManager.goMainProfile(context);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void getUserInfoById(String userId) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            //changed it to single value event to disable firing this code again if user's data is changed in edit profile
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (firebaseEventsListener != null) {
                    firebaseEventsListener.onReadDataResponse(dataSnapshot);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                if (firebaseEventsListener != null) {
                    firebaseEventsListener.onReadDataResponse(null);
                }
            }
        });
    }


    //K.A: just for testing
    public void getAllUsers() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("type");
        mDatabase.equalTo("pro").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("AllUsers", dataSnapshot.toString());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.v("AllUsers_error", error.toString());
            }
        });
    }


}
