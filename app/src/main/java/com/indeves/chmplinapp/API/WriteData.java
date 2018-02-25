package com.indeves.chmplinapp.API;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.indeves.chmplinapp.Models.UserData;

/**
 * Created by boda on 2/25/18.
 */

public class WriteData {

    FirebaseAuth mAuth;
    private void writeNewUserInfo(UserData userData) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        String userId = mAuth.getCurrentUser().getUid().toString();
        mDatabase.child(userId).setValue(userData);
    }

}
