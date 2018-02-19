package com.indeves.chmplinapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.indeves.chmplinapp.Adapters.GPSTracker;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.StepProgressBar;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends StepProgressBar implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int REQUEST_TAKE_poster_PHOTO = 1;


    private GoogleMap mMap;
    String label = null;
    double latitude = 0, longitude = 0;
    Button button, j;
    LocationManager locationManager;
    String provider;
    GPSTracker gps = new GPSTracker(this);
    Geocoder geocoder;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Map");
        toolbar.setTitleTextColor(getColor(R.color.colorWhite));
  stateprogressbar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
            stateprogressbar.setAllStatesCompleted(false);
        //      button = findViewById(R.id.btnNext);
//        button.setOnClickListener(this);
        checkLocationPermission();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //mMap.setOnMapLongClickListener(this);
        geocoder = new Geocoder(this, Locale.getDefault());


    }

    public void onMapLongClick(final LatLng point) {
        if (mMap != null) {
            List<Address> addresses = null;
            if (isNetworkAvailable()) {

                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                final String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                final Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title(address + "   |||| " + city)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                for (int x = 0; x < 3000; x++) {
                }
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Write your message here.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("result", address);
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                marker.remove();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            } else {
                Toast.makeText(this, "Check Intnet ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        GPSTracker gps = new GPSTracker(this);
        if (gps.isGPSEnabled) { latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            LatLng currentCoordinates = new LatLng(
                    latitude,
                    longitude);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentCoordinates, 10));


        } else {
            gps.showSettingsAlert();
        }


    }





    //your code




    // Add a marker in Sydney and move the camera
    // LatLng sydney = new LatLng(-34, 151);
    //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    @Override
    public void onClick(View v) {
        if (v==button) {
            stateprogressbar.checkStateCompleted(true);
            Toast.makeText(this, label, Toast.LENGTH_SHORT).show();

        }
        if (v==j) {

        }

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Get Location ")
                        .setMessage("Kindly, we need your location to give an easy contact with photographeris")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    return;


                }
                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates:
                    locationManager.requestLocationUpdates(provider, 400, 1, (android.location.LocationListener) this);
                    return;
                }


            }

        }



    }
}

