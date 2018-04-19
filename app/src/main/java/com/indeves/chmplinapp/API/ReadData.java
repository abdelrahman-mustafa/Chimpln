package com.indeves.chmplinapp.API;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.indeves.chmplinapp.Models.CityLookUpModel;
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

    public void readUserInfo(final String userId) {

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            //changed it to single value event to disable firing this code again if user's data is changed in edit profile
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("DataSnap", dataSnapshot.toString());
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

    public void getUserInfoById(String userId) {
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.keepSynced(true);
        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            //changed it to single value event to disable firing this code again if user's data is changed in edit profile
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("returnedUser", dataSnapshot.toString());
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
                        if (userData.type != null && !userData.type.equals("user")) {

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
                Log.v("LookupsResponse", dataSnapshot.toString());
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

    //to get All cities, pass -1 in country id
    public void getCitiesLookUpsWithCountryId(final int countryId, final CityLookUpsListener cityLookUpsListener) {
        mDatabase = FirebaseDatabase.getInstance().getReference("citiesLookups");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("CityLookups", dataSnapshot.toString());
                if (dataSnapshot.getValue() != null) {
                    List<CityLookUpModel> lookUpModels = new ArrayList<CityLookUpModel>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        CityLookUpModel cityLookUpModel = dataSnapshot1.getValue(CityLookUpModel.class);
                        if (cityLookUpModel != null) {
                            if (countryId == -1 || cityLookUpModel.getCountryId() == countryId) {
                                lookUpModels.add(cityLookUpModel);
                            }
                        }
                    }
                    if (cityLookUpsListener != null) {
                        cityLookUpsListener.onLookUpsResponse(lookUpModels);
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.v("DB_error", error.toString());
                if (cityLookUpsListener != null) {
                    cityLookUpsListener.onLookUpsResponse(null);
                }
            }
        });
    }

    public void searchPros() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("users").orderByChild("city").equalTo("cairo");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.v("ggg", dataSnapshot.toString());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            // ...
        });

    }

    public void getEventsByProId(String proId) {
        mDatabase = FirebaseDatabase.getInstance().getReference("events");
        mDatabase.orderByChild("photographerId").equalTo(proId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("AllProEvents", dataSnapshot.toString());
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

    public interface AllProsListener {
        void onProsResponse(ArrayList<ProUserModel> pros);
    }

    public interface LookUpsListener {
        void onLookUpsResponse(List<LookUpModel> lookups);
    }

    public interface CityLookUpsListener {
        void onLookUpsResponse(List<CityLookUpModel> citiesList);
    }
}
