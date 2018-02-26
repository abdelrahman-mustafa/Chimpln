package com.indeves.chmplinapp.API;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.indeves.chmplinapp.Models.UserData;

/**
 * Created by boda on 2/25/18.
 */

public class WriteData {

    public void writeNewUserInfo(UserData userData,FirebaseAuth mAuth) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        String userId = mAuth.getCurrentUser().getUid();
        Log.d("user",userData.email);
        Log.d("user",userData.phone);
        Log.d("user",userData.type);
        Log.d("user",userId);
        mDatabase.child(userId).setValue(userData);
    }

}
