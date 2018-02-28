package com.indeves.chmplinapp.API;

import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.Models.UserData;

/**
 * Created by khalid on 27/02/18.
 */

public interface FirebaseEventsListener {

    void onWriteDataCompleted(boolean writeSuccessful);

    void onReadDataResponse(DataSnapshot dataSnapshot);


}
