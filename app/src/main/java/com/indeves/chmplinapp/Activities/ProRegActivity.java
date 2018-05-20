package com.indeves.chmplinapp.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.CloudStorageAPI;
import com.indeves.chmplinapp.API.CloudStorageListener;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.API.WriteData;
import com.indeves.chmplinapp.Models.CityLookUpModel;
import com.indeves.chmplinapp.Models.LookUpModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.PrefsManager.PrefGet;
import com.indeves.chmplinapp.PrefsManager.PrefsManager;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.CircleTransform;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ProRegActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnTouchListener, FirebaseEventsListener {

    //    private DatePicker datePicker;
    private static int RESULT_LOAD_IMAGE = 1;
    EditText firstName, lastName, expYears, from, to, area;
    TextView mail, phone, gender, country, city, age;
    Spinner sGender, sCountry, sCity, sDate, sDate1, birthPicker;
    Button saveData;
    ImageView pic;
    Bitmap selectImage;
    CheckBox full_day, half_day, per_hour;
    String selectedGender, selectedCountry, selectedCity, selectedWorkingHoursStart, selectedWorkingHoursEnd, selectedBirthDate;
    List<CityLookUpModel> citiesList;
    List<LookUpModel> countriesList;
    List<String> genderCategories;
    List<String> dayTimeCategories;
    ArrayAdapter<LookUpModel> countriesArrayAdapter;
    ArrayAdapter<CityLookUpModel> citiesArrayAdapter;
    LinearLayout ageLayout;
    ImageButton getFrontId, getBackId;
    ProgressDialog progressDialog;
    private Calendar calendar;
    private int year, month, day;
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
//                    String ageString = String.valueOf(arg3) + "-" + String.valueOf(arg2) + "-" + String.valueOf(arg1);
                    selectedBirthDate = String.valueOf(arg3) + "-" + String.valueOf(arg2 + 1) + "-" + String.valueOf(arg1);
                    age.setText(selectedBirthDate);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_registration);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //-----------------------------------------------
        //event availablity for the pro

        full_day = findViewById(R.id.pro_reg_avail_full);
        half_day = findViewById(R.id.pro_reg_avail_half);
        per_hour = findViewById(R.id.pro_reg_avail_hour);
        //-----------------------------------------------
        getFrontId = findViewById(R.id.pro_reg_get_id_front);
        getFrontId.setOnClickListener(this);
        getBackId = findViewById(R.id.pro_reg_get_id_back);
        getBackId.setOnClickListener(this);
        pic = findViewById(R.id.pro_reg_pic);
        firstName = findViewById(R.id.pro_reg_first_name);
        lastName = findViewById(R.id.pro_reg_last_name);
        expYears = findViewById(R.id.pro_reg_edit_years_exp);
        area = findViewById(R.id.pro_reg_edit_area);
        to = findViewById(R.id.pro_reg_edit_to);
        saveData = findViewById(R.id.pro_reg_complete);
        saveData.setOnClickListener(this);
        from = findViewById(R.id.pro_reg_edit_from);
        mail = findViewById(R.id.pro_reg_mail);
        phone = findViewById(R.id.pro_reg_phone);
        gender = findViewById(R.id.pro_reg_gender);
        city = findViewById(R.id.pro_reg_city);
        age = findViewById(R.id.pro_reg_age);
        age.setOnClickListener(this);
        country = findViewById(R.id.pro_reg_country);
        sGender = findViewById(R.id.pro_reg_spinner_gender);
        sGender.setOnItemSelectedListener(this);
        sCountry = findViewById(R.id.pro_reg_spinner_country);
        sCountry.setOnItemSelectedListener(this);
        sCity = findViewById(R.id.pro_reg_spinner_city);
        sCity.setOnItemSelectedListener(this);
        sDate = findViewById(R.id.pro_reg_spinner_date);
        sDate.setOnItemSelectedListener(this);
        sDate1 = findViewById(R.id.pro_reg_spinner_date1);
        sDate1.setOnItemSelectedListener(this);
        birthPicker = findViewById(R.id.pro_reg_spinner_age);
        birthPicker.setOnItemSelectedListener(this);
        ageLayout = findViewById(R.id.linearLayout6);
        ageLayout.setOnClickListener(this);
        birthPicker.setOnTouchListener(this);
        pic.setOnClickListener(this);
        sGender.getBackground().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        sDate.getBackground().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        sCountry.getBackground().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        sCity.getBackground().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        sDate1.getBackground().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        birthPicker.getBackground().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        // Spinner Drop down elements
        genderCategories = new ArrayList<String>();
        genderCategories.add(getResources().getString(R.string.gender));
        genderCategories.add("Male");
        genderCategories.add("Female");
        // Creating adapter for spinner
        ArrayAdapter<String> genderArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, genderCategories);
        // Drop down layout style - list view with radio button
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sGender.setAdapter(genderArrayAdapter);

        // Spinner Drop down elements
        dayTimeCategories = new ArrayList<String>();
        dayTimeCategories.add("AM");
        dayTimeCategories.add("PM");
        // Creating adapter for spinner
        ArrayAdapter<String> dayTimeArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dayTimeCategories);
        // Drop down layout style - list view with radio button
        dayTimeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //the time picker whether A.M or P.M
        sDate1.setAdapter(dayTimeArrayAdapter);
        sDate.setAdapter(dayTimeArrayAdapter);

        countriesList = new ArrayList<LookUpModel>();
        countriesList.add(new LookUpModel(0, "Country"));
        countriesArrayAdapter = new ArrayAdapter<LookUpModel>(this, R.layout.spinner_item, countriesList);
        countriesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCountry.setAdapter(countriesArrayAdapter);
        citiesList = new ArrayList<CityLookUpModel>();
        citiesList.add(new CityLookUpModel(0, 0, "City"));
        citiesArrayAdapter = new ArrayAdapter<CityLookUpModel>(this, R.layout.spinner_item, citiesList);
        citiesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCity.setAdapter(citiesArrayAdapter);
        ReadData readData = new ReadData();
        readData.getLookupsByType("counrtiesLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> lookups) {
                countriesList.addAll(lookups);
                countriesArrayAdapter.notifyDataSetChanged();
            }
        });
        try {

            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null)
                    mail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                if (FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null)
                    phone.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait.. ");
        progressDialog.setCanceledOnTouchOutside(false);


    }

    @Override
    public void onClick(View v) {
        if (v == ageLayout || v == age) {
            showDialog(999);
        } else if (v == pic) {
            getImage(1);
        } else if (v == getFrontId) {
            getImage(2);
        } else if (v == getBackId) {
            getImage(3);
        } else if (v == saveData) {
            progressDialog.show();
            if (ValidateRegistrationForm()) {
                CloudStorageAPI cloudStorageAPI = new CloudStorageAPI();
                cloudStorageAPI.UploadImage(selectImage, true, new CloudStorageListener.UploadUserImageListener() {
                    @Override
                    public void onImageUpload(String downloadUrl) {
                        if (downloadUrl != null) {
                            ProUserModel proUserModel = new ProUserModel();

                            ArrayList<String> list = new ArrayList<>();
                            if (full_day.isClickable()) {
                                list.add("Full day");
                            }
                            if (half_day.isClickable()) {
                                list.add("Half day");
                            }
                            if (per_hour.isClickable()) {
                                list.add("Per hour");
                            }
                            proUserModel.setEventAvailablity(list);
                            proUserModel.setName(firstName.getText().toString());
                            proUserModel.setLastName(lastName.getText().toString());
                            proUserModel.setBirthDate(selectedBirthDate);
                            proUserModel.setGender(selectedGender);
                            proUserModel.setExperience(expYears.getText().toString());
                            proUserModel.setCountry(selectedCountry);
                            proUserModel.setCity(selectedCity);
                            proUserModel.setArea(area.getText().toString());
                            proUserModel.setWorkDayStart(from.getText().toString() + " " + selectedWorkingHoursStart);
                            proUserModel.setWorkDayEnd(to.getText().toString() + " " + selectedWorkingHoursEnd);
                            proUserModel.setProfilePicUrl(downloadUrl);
                            WriteData writeData = new WriteData(ProRegActivity.this);
                            try {
                                writeData.updateUserProfileData(proUserModel);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(ProRegActivity.this, "Failed to save your data", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private void getImage(int RESULT_LOAD) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK && reqCode == 1) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectImage = BitmapFactory.decodeStream(imageStream);
//                pic.setImageBitmap(Bitmap.createScaledBitmap(selectImage, 300, 300, false));
                Picasso.with(this).load(imageUri).resize(300, 300).transform(new CircleTransform()).into(pic);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ProRegActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else if (resultCode == RESULT_OK && reqCode == 2) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectImage = BitmapFactory.decodeStream(imageStream);
//                pic.setImageBitmap(Bitmap.createScaledBitmap(selectImage, 300, 300, false));
                Picasso.with(this).load(imageUri).resize(50, 50).transform(new CircleTransform()).into(getFrontId);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ProRegActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else if (resultCode == RESULT_OK && reqCode == 3) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectImage = BitmapFactory.decodeStream(imageStream);
//                pic.setImageBitmap(Bitmap.createScaledBitmap(selectImage, 300, 300, false));
                Picasso.with(this).load(imageUri).resize(50, 50).transform(new CircleTransform()).into(getBackId);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ProRegActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(ProRegActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    public boolean ValidateRegistrationForm() {
        boolean isFormValid = true;
        if (selectImage == null) {
            isFormValid = false;
            Toast.makeText(this, "Please set your image ", Toast.LENGTH_SHORT).show();
        } else if (firstName.getText().toString().isEmpty()) {
            firstName.setError(getResources().getString(R.string.error_field_missing));
            isFormValid = false;
        } else if (lastName.getText().toString().isEmpty()) {
            lastName.setError(getResources().getString(R.string.error_field_missing));
            isFormValid = false;
        } else if (selectedBirthDate == null) {
            isFormValid = false;
            Toast.makeText(this, "Please set your birth date", Toast.LENGTH_SHORT).show();
        } else if (selectedGender.equals(getResources().getString(R.string.gender))) {
            isFormValid = false;
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
        } else if (expYears.getText().toString().isEmpty()) {
            expYears.setError(getResources().getString(R.string.error_field_missing));
            isFormValid = false;
        } else if (country.getText().equals("Country")) {
            isFormValid = false;
            Toast.makeText(this, "Please select your country", Toast.LENGTH_SHORT).show();
        } else if (city.getText().equals("City")) {
            isFormValid = false;
            Toast.makeText(this, "Please select your city", Toast.LENGTH_SHORT).show();
        } else if (area.getText().toString().isEmpty()) {
            isFormValid = false;
            area.setError(getResources().getString(R.string.error_field_missing));
        } else if (from.getText().toString().isEmpty()) {
            isFormValid = false;
            from.setError(getResources().getString(R.string.error_field_missing));
        } else if (to.getText().toString().isEmpty()) {
            to.setError(getResources().getString(R.string.error_field_missing));
            isFormValid = false;
        }


        return isFormValid;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == birthPicker.getId()) {
            showDialog(999);
        } else if (parent.getId() == sGender.getId()) {
            selectedGender = genderCategories.get(position);
            gender.setText(selectedGender);

        } else if (parent.getId() == sCity.getId()) {
            selectedCity = citiesList.get(position).getEnglishName();
            city.setText(selectedCity);

        } else if (parent.getId() == sCountry.getId()) {
            selectedCountry = countriesList.get(position).getEnglishName();
            country.setText(selectedCountry);
            ReadData readData = new ReadData();
            //TODO: issue occurs in reselecting multiple times
            readData.getCitiesLookUpsWithCountryId(countriesList.get(position).getId(), new ReadData.CityLookUpsListener() {
                @Override
                public void onLookUpsResponse(List<CityLookUpModel> newCitiesList) {
                    Log.v("CitiesReturned", newCitiesList.toString());
                    citiesList.clear();
                    citiesList.addAll(newCitiesList);
                    citiesArrayAdapter.notifyDataSetChanged();
                }
            });


        } else if (parent.getId() == sDate.getId()) {
            selectedWorkingHoursStart = dayTimeCategories.get(position);

        } else if (parent.getId() == sDate1.getId()) {
            selectedWorkingHoursEnd = dayTimeCategories.get(position);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == birthPicker) {
            showDialog(999);
        }
        return false;
    }

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {
        progressDialog.dismiss();
        if (writeSuccessful) {
            PrefGet prefGet = new PrefGet(ProRegActivity.this);
            switch (prefGet.getUserType()) {
                case "stu":
                    startActivity(new Intent(ProRegActivity.this, StuLandingPage.class));
                    ProRegActivity.this.finish();
                    break;
                case "user":
                    startActivity(new Intent(ProRegActivity.this, UserProfileMain.class));
                    ProRegActivity.this.finish();
                    break;
                case "pro":
                    startActivity(new Intent(ProRegActivity.this, ProLandingPage.class));
                    ProRegActivity.this.finish();
                    break;
            }
        } else {
            Toast.makeText(this, "Error in saving your data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
