package com.indeves.chmplinapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.Auth;
import com.indeves.chmplinapp.API.AuthenticationInterface;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.PrefsManager.PrefGet;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.AppController;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class RespondToServerActivity extends AppCompatActivity implements FirebaseEventsListener, AuthenticationInterface.DelUserListner {

    Button check;
    JSONObject jsonObject;
    Bitmap b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respond_to_server);
        check = findViewById(R.id.check);

        //  String id = getIntent().getStringExtra("id");
        SharedPreferences editor = getSharedPreferences("checkDate", MODE_PRIVATE);
        String id = editor.getString("id", "0");

        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(RespondToServerActivity.this);
        SharedPreferences.Editor edit = shre.edit();
        edit.putBoolean("proToServ", true);
        edit.apply();
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ll", "2222222");
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {/*
                    ReadData readData = new ReadData(RespondToServerActivity.this);
                    readData.getUserInfoById(FirebaseAuth.getInstance().getCurrentUser().getUid());*/
                }
                // String s = FirebaseAuth.getInstance().getCurrentUser().getUid();
                //    ReadData readData = new ReadData();
                //  readData.readUserInfo(s);
                SharedPreferences prefs = getSharedPreferences("checkDate", MODE_PRIVATE);

                String id = prefs.getString("id", "k");
                // /"No name defined" is the default value.


                String url = "http://206.189.96.67/v1/request/" + id;
                JsonObjectRequest stringRequest = new JsonObjectRequest(GET, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getString("state").toLowerCase().equals("approved")) {
                                        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(RespondToServerActivity.this);
                                        SharedPreferences.Editor edit = shre.edit();
                                        edit.putBoolean("proToServ", false);
                                        edit.apply();
                                        PrefGet prefGet = new PrefGet(RespondToServerActivity.this);
                                        switch (prefGet.getUserType()) {
                                            case "stu":
                                                startActivity(new Intent(RespondToServerActivity.this, StuLandingPage.class));
                                                RespondToServerActivity.this.finish();
                                                break;
                                            case "user":
                                                startActivity(new Intent(RespondToServerActivity.this, UserProfileMain.class));
                                                RespondToServerActivity.this.finish();
                                                break;
                                            case "pro":
                                                startActivity(new Intent(RespondToServerActivity.this, ProLandingPage.class));
                                                RespondToServerActivity.this.finish();
                                                break;
                                        }
                                    } else if (response.getString("state").toLowerCase().equals("rejected")) {
                                        Auth auth = new Auth(RespondToServerActivity.this);
                                        auth.deleteAcount();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });

                AppController.getInstance().addToRequestQueue(stringRequest);

            }
        });
    }


    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {

    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {


        Log.i("lll", dataSnapshot.toString());

        if (dataSnapshot != null) {
            // we need to send the data of user to the url
            ProUserModel proUserModel = dataSnapshot.getValue(ProUserModel.class);

            String encoded = proUserModel.getProfilePicUrl();
            String params_Date =
                    ("{" + " \"name\":" + "\"" + proUserModel.getName() + "\"" + ","
                            + " \"description\":" + "\"" + proUserModel.getGender() + "," + proUserModel.getCity() + "," + proUserModel.getExperience() + "," + proUserModel.getEmail() + "\"" + ","
                            + "\"image\":" + "\"" + encoded + "\""
                            + "}");
            try {
                jsonObject = new JSONObject(params_Date);

                Log.i("request", jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = "http://206.189.96.67/v1/request";
            JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                Toast.makeText(RespondToServerActivity.this, response.getString("id"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });

            AppController.getInstance().addToRequestQueue(stringRequest);


        } else {
            Toast.makeText(RespondToServerActivity.this, "Failed to load your data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDelUserListner(boolean delSuccessful) {
        if (delSuccessful){
            Toast.makeText(RespondToServerActivity.this, "Your Account is rejected", Toast.LENGTH_SHORT).show();
            SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(RespondToServerActivity.this);
            SharedPreferences.Editor edit = shre.edit();
            edit.putBoolean("proToServ", false);
            edit.apply();
            startActivity(new Intent(RespondToServerActivity.this,LogIn.class));
        }
    }
}
