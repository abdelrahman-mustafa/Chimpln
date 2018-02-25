package com.indeves.chmplinapp.API;

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

/**
 * Created by boda on 2/25/18.
 */

public class ReadData {
    UserData user;
    FirebaseAuth mAuth;

    private UserData readUserInfo(String userId) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserData.class);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        return user;
    }

}
