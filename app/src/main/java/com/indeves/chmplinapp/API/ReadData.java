package com.indeves.chmplinapp.API;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.indeves.chmplinapp.Models.LookUpModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.Models.UserData;
import com.indeves.chmplinapp.PrefsManager.PrefSave;
import com.indeves.chmplinapp.PrefsManager.PrefsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boda on 2/25/18.
 */

public class ReadData {
    DatabaseReference mDatabase;
    private UserData user;
    private FirebaseEventsListener firebaseEventsListener;

    public ReadData() {

    }

    public ReadData(FirebaseEventsListener firebaseEventsListener) {
        this.firebaseEventsListener = firebaseEventsListener;
    }

    public void readUserInfo(final String userId, final Context context) {

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            //changed it to single value event to disable firing this code again if user's data is changed in edit profile
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("DataSnap", dataSnapshot.toString());
                user = dataSnapshot.getValue(UserData.class);
                if (user != null) {
                    Log.i("MyUserData", user.toString());
                    PrefSave prefSave = new PrefSave(context);
                    if (user.type != null) {
                        prefSave.saveUserType(user.type.trim());
                        PrefsManager prefsManager = new PrefsManager(context);
                        prefsManager.goMainProfile(context);
                    }
                } else {
                    //TODO delete the account in the auth
                    if (firebaseEventsListener != null) {
                        firebaseEventsListener.onReadDataResponse(null);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void getUserInfoById(String userId) {
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
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


    public void getAllPros(final AllProsListener allProsListener) {
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("AllUsers", dataSnapshot.toString());
                ArrayList<ProUserModel> pros = new ArrayList<ProUserModel>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    UserData userData = dataSnapshot1.getValue(UserData.class);
                    if (userData != null) {
                        if (!userData.type.equals("user")) {
                            pros.add(dataSnapshot1.getValue(ProUserModel.class));
                        }
                    }
                }
                if (allProsListener != null) {
                    allProsListener.onProsResponse(pros);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.v("AllUsers_error", error.toString());
                if (allProsListener != null) {
                    allProsListener.onProsResponse(null);
                }
            }
        });
    }

    public void getAllEvents() {
        mDatabase = FirebaseDatabase.getInstance().getReference("events");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("AllEvents", dataSnapshot.toString());
                if (firebaseEventsListener != null) {
                    firebaseEventsListener.onReadDataResponse(dataSnapshot);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.v("AllEvents_error", error.toString());
                if (firebaseEventsListener != null) {
                    firebaseEventsListener.onReadDataResponse(null);
                }
            }
        });

    }

    public void getLookupsByType(String type, final LookUpsListener lookUpsListener) {
        mDatabase = FirebaseDatabase.getInstance().getReference(type);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("EventTypesResponse", dataSnapshot.toString());
                if (dataSnapshot.getValue() != null) {
                    GenericTypeIndicator<List<LookUpModel>> t = new GenericTypeIndicator<List<LookUpModel>>() {
                    };
                    List<LookUpModel> lookUpModels = dataSnapshot.getValue(t);
                    if (lookUpsListener != null) {
                        lookUpsListener.onLookUpsResponse(lookUpModels);
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.v("DB_error", error.toString());
                if (lookUpsListener != null) {
                    lookUpsListener.onLookUpsResponse(null);
                }
            }
        });

    }

    public interface AllProsListener {
        void onProsResponse(ArrayList<ProUserModel> pros);
    }

    public interface LookUpsListener {
        void onLookUpsResponse(List<LookUpModel> lookups);
    }


}
