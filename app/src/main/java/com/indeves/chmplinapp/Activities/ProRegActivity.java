package com.indeves.chmplinapp.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.PrefsManager.PrefsManager;
import com.indeves.chmplinapp.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ProRegActivity extends AppCompatActivity implements View.OnClickListener {

    EditText firstName, lastName, expYears, from, to, area;
    TextView mail, phone, gender, country, city, age;
    Spinner sGender, sCountry, sCity, sDate, sDate1, birthPicker;
    Button idPicker, saveData;
    ImageView pic;
    private DatePicker datePicker;
    private static int RESULT_LOAD_IMAGE = 1;
    private Calendar calendar;
    private int year, month, day;
    Bitmap selectImage;
    ProUserModel proUserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_registration);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //-----------------------------------------------
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
        country = findViewById(R.id.pro_reg_country);
        sGender = findViewById(R.id.pro_reg_spinner_gender);
        sCountry = findViewById(R.id.pro_reg_spinner_country);
        sCity = findViewById(R.id.pro_reg_spinner_city);
        sDate = findViewById(R.id.pro_reg_spinner_date);
        sDate1 = findViewById(R.id.pro_reg_spinner_date1);
        birthPicker = findViewById(R.id.pro_reg_spinner_age);
        birthPicker.setOnClickListener(this);
        pic.setOnClickListener(this);
        sGender.getBackground().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        sDate.getBackground().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        sCountry.getBackground().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        sCity.getBackground().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        sDate1.getBackground().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        birthPicker.getBackground().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add(getResources().getString(R.string.gender));
        categories.add("Male");
        categories.add("Female");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sGender.setAdapter(dataAdapter);

        // Spinner Drop down elements
        List<String> categories1 = new ArrayList<String>();
        categories.add("From");
        categories.add("AM");
        categories.add("PM");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sDate1.setAdapter(dataAdapter1);
        sDate.setAdapter(dataAdapter1);

    }

    @Override
    public void onClick(View v) {
        if (v == birthPicker) {
            showDialog(999);
        } else if (v == pic) {
            getImage();
        }else if (v == saveData){
            PrefsManager prefsManager = new PrefsManager(this);
            prefsManager.goMainProfile(this);
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

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    age.setText(String.valueOf(arg1) + "/" + String.valueOf(arg2) + "/" + String.valueOf(arg3));
                }
            };

    private void getImage() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectImage = BitmapFactory.decodeStream(imageStream);
                pic.setImageBitmap(Bitmap.createScaledBitmap(selectImage, selectImage.getWidth(), selectImage.getHeight(), false));


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ProRegActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(ProRegActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }


}
