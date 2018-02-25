package com.indeves.chmplinapp.PrefsManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.indeves.chmplinapp.Activities.LogIn;

/**
 * Created by boda on 2/25/18.
 */

public class PrefSave {
    Context context;

    public PrefSave(Context context) {
        this.context = context;
    }

    private void saveId(String userId){
        SharedPreferences mypreference = PreferenceManager.getDefaultSharedPreferences(context);
        mypreference.edit().putString("user id", userId).apply();
    }
    private void saveLogStatus(Boolean status){
        SharedPreferences mypreference = PreferenceManager.getDefaultSharedPreferences(context);
        mypreference.edit().putBoolean("Log", status).apply();
    }
    private void saveUserType(String type){
        SharedPreferences mypreference = PreferenceManager.getDefaultSharedPreferences(context);
        mypreference.edit().putString("user type", type).apply();
    }
}
