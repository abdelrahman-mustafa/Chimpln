package com.indeves.chmplinapp.API;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.Models.UserData;

/**
 * Created by boda on 2/25/18.
 */

public class WriteData {
    private FirebaseEventsListener firebaseEventsListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUserReference = FirebaseDatabase.getInstance().getReference("users");
    private String userId;

    public WriteData(FirebaseEventsListener firebaseEventsListener) {
        this.firebaseEventsListener = firebaseEventsListener;
        this.mAuth = FirebaseAuth.getInstance();
    }

    public void writeNewUserInfo(UserData userData) {
        userId = mAuth.getCurrentUser().getUid();
        Log.d("user_mail", userData.email);
        Log.d("user_phone", userData.phone);
        Log.d("user_type", userData.type);
        Log.d("userID", userId);
        mDatabaseUserReference.child(userId).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    firebaseEventsListener.onWriteDataCompleted(true);
                } else {
                    firebaseEventsListener.onWriteDataCompleted(false);
                }
            }
        });
    }

    public void updateUserProfileData(UserData userData) throws Exception {
        if (mAuth.getCurrentUser() != null) {
            mDatabaseUserReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userData.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        firebaseEventsListener.onWriteDataCompleted(true);
                    } else {
                        firebaseEventsListener.onWriteDataCompleted(false);
                    }
                }
            });
        } else {
            throw new Exception("user is not authenticated");
        }


    }

    public void updateUserProfileData(ProUserModel userData) throws Exception {
        if (mAuth.getCurrentUser() != null) {
            mDatabaseUserReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userData.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        firebaseEventsListener.onWriteDataCompleted(true);
                    } else {
                        firebaseEventsListener.onWriteDataCompleted(false);
                    }
                }
            });
        } else {
            throw new Exception("user is not authenticated");
        }


    }


}
