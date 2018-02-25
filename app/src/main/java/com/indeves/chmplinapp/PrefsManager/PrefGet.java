package com.indeves.chmplinapp.PrefsManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by boda on 2/25/18.
 */

public class PrefGet {
    Context context;

    public PrefGet(Context context) {
        this.context = context;
    }

    public String getId (){
        SharedPreferences mypreference = PreferenceManager.getDefaultSharedPreferences(context);
        String userId = mypreference.getString("user id", "");
        return userId;
    }
    public Boolean getLogStatus(){
        SharedPreferences mypreference = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean status = mypreference.getBoolean("user id", false);
        return status;
    }
    public String getUserType(){
        SharedPreferences mypreference = PreferenceManager.getDefaultSharedPreferences(context);
        String type = mypreference.getString("user type", "");
        return type;
    }
}
