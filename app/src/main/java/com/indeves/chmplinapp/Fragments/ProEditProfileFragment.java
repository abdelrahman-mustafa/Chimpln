package com.indeves.chmplinapp.Fragments;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.CloudStorageAPI;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.API.WriteData;
import com.indeves.chmplinapp.Activities.ProLandingPage;
import com.indeves.chmplinapp.Activities.ProRegActivity;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.Models.UserData;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.CircleTransform;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class ProEditProfileFragment extends Fragment implements FirebaseEventsListener, View.OnClickListener {

    Context attachedActivityContext;
    EditText firstName, lastName, birthDate, gender, experience, country, city, area;
    TextView editFirstName, editLastName, editBirthDate, editGender, editExperience, editCountry, editCity, editArea, email, mobileNumber;
    ProgressDialog progressDialog;
    ImageView profileImage;
    private int year;
    private int month;
    private int day;
    Button saveChanges;
    Bitmap selectImage;

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            birthDate.setText(new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year)
                    .append(" "));


        }
    };


    public ProEditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pro_edit_profile, container, false);
        setHasOptionsMenu(true);
        if (attachedActivityContext != null && ((ProLandingPage) attachedActivityContext).getSupportActionBar() != null) {
            ((ProLandingPage) attachedActivityContext).getSupportActionBar().setDisplayShowHomeEnabled(true);
            ((ProLandingPage) attachedActivityContext).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((ProLandingPage) attachedActivityContext).getSupportActionBar().setTitle(getResources().getString(R.string.fragment_title_edit_profile));

        }
        email = rootView.findViewById(R.id.proProfile_mail);
        profileImage = rootView.findViewById(R.id.pro_profile_pic);
        profileImage.setOnClickListener(this);
        mobileNumber = rootView.findViewById(R.id.proProfile_mobileNumber);
        firstName = rootView.findViewById(R.id.editProfile_user_firstName_textView);
        //firstName.setEnabled(false);
        lastName = rootView.findViewById(R.id.editProfile_user_lastName_textView);
        //lastName.setEnabled(false);
        birthDate = rootView.findViewById(R.id.editProfile_user_birthDate_textView);
        //birthDate.setEnabled(false);
        gender = rootView.findViewById(R.id.editProfile_user_gender);
        //gender.setEnabled(false);
        experience = rootView.findViewById(R.id.editProfile_user_experience);
        //experience.setEnabled(false);
        country = rootView.findViewById(R.id.editProfile_user_country);
        //country.setEnabled(false);
        city = rootView.findViewById(R.id.editProfile_user_City);
        //city.setEnabled(false);
        area = rootView.findViewById(R.id.editProfile_user_state);
        //area.setEnabled(false);
   /*     editFirstName = rootView.findViewById(R.id.editProfile_edit_firstName_textView);
        editLastName = rootView.findViewById(R.id.editProfile_edit_lastName_textView);
        editBirthDate = rootView.findViewById(R.id.editProfile_edit_birthDate_textView);
        editGender = rootView.findViewById(R.id.editProfile_edit_gender_textView);
        editExperience = rootView.findViewById(R.id.editProfile_edit_experience_textView);
        editCountry = rootView.findViewById(R.id.editProfile_edit_country_textView);
        editCity = rootView.findViewById(R.id.editProfile_edit_city_textView);
        editArea = rootView.findViewById(R.id.editProfile_edit_state_textView);
        editFirstName.setOnClickListener(this);
        editLastName.setOnClickListener(this);
        birthDate.setOnClickListener(this);
        editExperience.setOnClickListener(this);
        editCountry.setOnClickListener(this);
        editCity.setOnClickListener(this);
        editArea.setOnClickListener(this);
        editGender.setOnClickListener(this);*/
        saveChanges = rootView.findViewById(R.id.userEditProfile_save_button);
        saveChanges.setOnClickListener(this);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading profile data");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
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


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pro_edit_profile_save, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.pro_profile_menu_save) {

            WriteData writeData = new WriteData(ProEditProfileFragment.this);
            ProUserModel userData = new ProUserModel();
            userData.setName(firstName.getText().toString());
            userData.setLastName(lastName.getText().toString());
            userData.setBirthDate(birthDate.getText().toString());
            userData.setGender(gender.getText().toString());
            userData.setExperience(experience.getText().toString());
            userData.setCountry(country.getText().toString());
            userData.setCity(city.getText().toString());
            userData.setArea(area.getText().toString());
            try {
                writeData.updateUserProfileData(userData);
            } catch (Exception e) {
                Toast.makeText(attachedActivityContext, getResources().getString(R.string.error_not_authenticated), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.attachedActivityContext = context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {
        if (writeSuccessful) {
            Toast.makeText(attachedActivityContext, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                fragmentManager.popBackStack();
            }

        } else {
            Toast.makeText(attachedActivityContext, "Profile is not updated ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {
        progressDialog.dismiss();
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            ProUserModel proUserModel = dataSnapshot.getValue(ProUserModel.class);
            displayUserInfo(proUserModel);
        } else {
            Toast.makeText(getContext(), "Failed to load your data", Toast.LENGTH_SHORT).show();
        }

    }

    void displayUserInfo(ProUserModel proUserModel) {
        if (proUserModel != null) {
            if (proUserModel.getName() != null)
                firstName.setText(proUserModel.getName());
            if (proUserModel.getEmail() != null)
                email.setText(proUserModel.getEmail());
            if (proUserModel.getPhone() != null)
                mobileNumber.setText(proUserModel.getPhone());
            if (proUserModel.getLastName() != null)
                lastName.setText(proUserModel.getLastName());
            if (proUserModel.getBirthDate() != null)
                birthDate.setText(proUserModel.getBirthDate());
            if (proUserModel.getGender() != null)
                gender.setText(proUserModel.getGender());
            if (proUserModel.getExperience() != null)
                experience.setText(proUserModel.getExperience());
            if (proUserModel.getCountry() != null)
                country.setText(proUserModel.getCountry());
            if (proUserModel.getCity() != null)
                city.setText(proUserModel.getCity());
            if (proUserModel.getArea() != null)
                area.setText(proUserModel.getArea());

            if (proUserModel.getProfilePicUrl() != null) {
                Picasso.with(getContext()).load(proUserModel.getProfilePicUrl()).resize(300, 300).placeholder(R.drawable.user).transform(new CircleTransform()).error(R.drawable.user).into(profileImage);
            }

        }
    }

    @Override
    public void onClick(View v) {
        if (v == birthDate) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), datePickerListener, year, month, day);
            datePickerDialog.show();

        }
        else if (v == saveChanges) {

            Log.v("Edit profile", "Edit profile save button clicked");
            WriteData writeData = new WriteData(this);
            ProUserModel userData = new ProUserModel();
            userData.setName(firstName.getText().toString());
            userData.setLastName(lastName.getText().toString());
            userData.setGender(gender.getText().toString());
            userData.setCity(city.getText().toString());
            userData.setCity(country.getText().toString());
            userData.setCity(area.getText().toString());
            userData.setCity(experience.getText().toString());
            userData.setCity(birthDate.getText().toString());

            try {
             //   writeData.updateUserProfileData(userData);
                writeData.updateUserProfileData(userData);
            } catch (Exception e) {
                Toast.makeText(getContext(), getResources().getString(R.string.error_not_authenticated), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }else  if (v==profileImage){

            getImage(0);
        }

    }
    private void getImage(int RESULT_LOAD) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK && reqCode == 0) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                selectImage = BitmapFactory.decodeStream(imageStream);
//                pic.setImageBitmap(Bitmap.createScaledBitmap(selectImage, 300, 300, false));
                Picasso.with(getContext()).load(imageUri).resize(300, 300).transform(new CircleTransform()).into(profileImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

}
