package com.indeves.chmplinapp.PrefsManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.indeves.chmplinapp.Activities.LogIn;

/**
 * Created by boda on 2/25/18.
 */

public class PrefSave {
    private SharedPreferences mypreference;

    public PrefSave(Context context) {
        mypreference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveId(String userId) {
        mypreference.edit().putString("user_id", userId).apply();
    }

    public void saveLogInStatus(Boolean status) {
        mypreference.edit().putBoolean("LoggedIn", status).apply();
    }

    public void saveUserType(String type) {
        Log.d("user type",type);
        mypreference.edit().putString("user_type", type).apply();
    }
}
