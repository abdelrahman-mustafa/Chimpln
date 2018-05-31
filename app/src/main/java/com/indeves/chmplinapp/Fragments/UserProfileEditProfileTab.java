package com.indeves.chmplinapp.Fragments;

import android.app.ActionBar;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.API.WriteData;
import com.indeves.chmplinapp.Models.UserData;
import com.indeves.chmplinapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class UserProfileEditProfileTab extends android.support.v4.app.Fragment implements FirebaseEventsListener, View.OnClickListener {
    TextView email, mobileNumber, editFirstName, editLastName, editLocation, editGender;
    EditText firstName, lastName, gender, location;
    Button saveChanges;
    //   ImageView image;
    com.joooonho.SelectableRoundedImageView image;
    Bitmap selectedImage;
    private static int RESULT_LOAD_IMAGE = 1;

    public UserProfileEditProfileTab() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_profile_edit, container, false);
        email = rootView.findViewById(R.id.email);
        mobileNumber = rootView.findViewById(R.id.userMobileNumber_textView);
        firstName = rootView.findViewById(R.id.editProf_TextView_first_name);
        // firstName.setEnabled(false);
        //editFirstName = rootView.findViewById(R.id.editFirstName_textView);
        //editFirstName.setOnClickListener(this);
        lastName = rootView.findViewById(R.id.editProf_TextView_last_name);
        // lastName.setEnabled(false);
        //editLastName = rootView.findViewById(R.id.editLastName_textView);
        //editLastName.setOnClickListener(this);
        location = rootView.findViewById(R.id.editProf_TextView_location);
        // location.setEnabled(false);
       // editLocation = rootView.findViewById(R.id.editLocation_textView);
        //editLocation.setOnClickListener(this);
        gender = rootView.findViewById(R.id.editProf_TextView_gender);
        // gender.setEnabled(false);
        //editGender = rootView.findViewById(R.id.editGender_textView);
        //editGender.setOnClickListener(this);
        image = rootView.findViewById(R.id.user_image);
        image.setOnClickListener(this);
        saveChanges = rootView.findViewById(R.id.userEditProfile_save_button);
        saveChanges.setOnClickListener(this);
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ReadData readData = new ReadData(this);
            readData.getUserInfoById(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }


        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(getContext());
        String imagePath = shre.getString("imagepath", "0");

        if (!imagePath.equals("0")) {
            File fp = new File(imagePath);
            Bitmap b = null;
            try {
                b = BitmapFactory.decodeStream(new FileInputStream(fp));
                image.setImageBitmap(b);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return rootView;

    }

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {
        if (writeSuccessful) {
            Toast.makeText(getContext(), "Your profile is updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "oops! something wrong happened!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            UserData userData = dataSnapshot.getValue(UserData.class);
            displayUserData(userData);

        } else {
            Toast.makeText(getContext(), "Error loading your data", Toast.LENGTH_SHORT).show();
        }

    }

    public void displayUserData(UserData userData) {
        email.setText(userData.getEmail() != null ? userData.getEmail() : "");
        mobileNumber.setText(userData.getPhone() != null ? userData.getPhone() : "");
        if (userData.getName() != null)
            firstName.setText(userData.getName());
        if (userData.getLastName() != null)
            lastName.setText(userData.getLastName());
        if (userData.getLocation() != null)
            location.setText(userData.getLocation());
        if (userData.getGender() != null)
            gender.setText(userData.getGender());

    }

    @Override
    public void onClick(View v) {
       /* if (v == editFirstName) {
            firstName.setEnabled(true);
            firstName.requestFocus();

        } else if (v == editLastName) {
            lastName.setEnabled(true);
            lastName.requestFocus();

        } else if (v == editLocation) {
            location.setEnabled(true);
            location.requestFocus();

        } else if (v == editGender) {
            gender.setEnabled(true);
            gender.requestFocus();
        } else*/
        if (v == saveChanges) {

            Log.v("Edit profile", "Edit profile save button clicked");
            WriteData writeData = new WriteData(this);
            UserData userData = new UserData();
            userData.setName(firstName.getText().toString());
            userData.setLastName(lastName.getText().toString());
            userData.setLocation(location.getText().toString());
            userData.setGender(gender.getText().toString());
            try {
                writeData.updateUserProfileData(userData);
            } catch (Exception e) {
                Toast.makeText(getContext(), getResources().getString(R.string.error_not_authenticated), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (v == image) {
            getImage();
        }

    }

    private void getImage() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK && reqCode == RESULT_LOAD_IMAGE) {
            try {

                Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                image.setImageBitmap(Bitmap.createScaledBitmap(selectedImage, 300, 500, false));
                image.setImageBitmap(selectedImage);

                String s = saveInInternalStorage(selectedImage);
                SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor edit = shre.edit();
                edit.putString("imagepath", s);
                edit.apply();
                //   Picasso.with(getContext()).load(imageUri).resize(300, 300).transform(new CircleTransform()).into(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }


    private String saveInInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        Date date = new Date();
        File mypath = new File(directory, String.valueOf(date.getDate()) + "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();


    }
}