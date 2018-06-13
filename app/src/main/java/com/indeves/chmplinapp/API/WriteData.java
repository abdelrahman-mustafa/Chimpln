package com.indeves.chmplinapp.API;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.PackageModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.Models.UserData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boda on 2/25/18.
 */

public class WriteData {
    private FirebaseEventsListener firebaseEventsListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUserReference;
    private DatabaseReference eventsDatabaseReference;
    private String userId;

    public WriteData(FirebaseEventsListener firebaseEventsListener) {
        this.firebaseEventsListener = firebaseEventsListener;
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabaseUserReference = FirebaseDatabase.getInstance().getReference("users");
        this.eventsDatabaseReference = FirebaseDatabase.getInstance().getReference("events");
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

    public void updateProData(ProUserModel proUserModel) {
        mDatabaseUserReference.child(proUserModel.getUid()).updateChildren(proUserModel.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public void setProIdImagesUrls(String frontImageUrl, String backImageUrl) throws Exception {
        if (mAuth.getCurrentUser() != null) {
            ProUserModel userData = new ProUserModel();
            userData.setIdFrontImageUrl(frontImageUrl);
            userData.setIdBackImageUrl(backImageUrl);
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

    public void respondToEvent(String status, String eventId) throws Exception {
        EventModel eventModel = new EventModel();
        eventModel.setEventStatus(status);
        if (mAuth.getCurrentUser() != null) {
            eventsDatabaseReference.child(eventId).updateChildren(eventModel.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public void updateEventStatusAfterPayment(String status, String eventId, double paymentAmount) throws Exception {
        EventModel eventModel = new EventModel();
        eventModel.setEventStatus(status);
        eventModel.setPaidAmount(paymentAmount);
        if (mAuth.getCurrentUser() != null) {
            eventsDatabaseReference.child(eventId).updateChildren(eventModel.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public void addNewProPackage(final PackageModel packageModel) throws Exception {
        //TODO: use push with key to handle the packages better
        //firstly, get pro data to check if it has packages
        if (mAuth.getCurrentUser() != null) {
            mDatabaseUserReference.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        ProUserModel proUserModel = dataSnapshot.getValue(ProUserModel.class);
                        if (proUserModel != null) {
                            List<PackageModel> packageModelList;
                            if (proUserModel.getPackages() == null) {
                                packageModelList = new ArrayList<>();
                            } else {
                                packageModelList = proUserModel.getPackages();
                            }
                            packageModelList.add(packageModel);
                            mDatabaseUserReference.child(mAuth.getCurrentUser().getUid()).child("packages").setValue(packageModelList).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    try {
                        throw new Exception(databaseError.toException());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } else {
            throw new Exception("user is not authenticated");
        }
    }

    public void bookNewEvent(final EventModel eventModel) throws Exception {
        if (mAuth.getCurrentUser() != null) {
            String eventId = eventsDatabaseReference.push().getKey();
            eventModel.setEventId(eventId);
            eventsDatabaseReference.child(eventId).setValue(eventModel).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public void updateEventData(EventModel eventModel) throws Exception {
        if (mAuth.getCurrentUser() != null) {
            if (eventModel.getEventId() != null) {
                eventsDatabaseReference.child(eventModel.getEventId()).updateChildren(eventModel.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                throw new Exception("Selected event has no ID");
            }
        } else {
            throw new Exception("user is not authenticated");
        }
    }


}
