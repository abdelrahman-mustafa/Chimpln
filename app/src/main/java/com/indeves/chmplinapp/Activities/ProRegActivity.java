package com.indeves.chmplinapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.indeves.chmplinapp.R;

public class ProRegActivity extends AppCompatActivity implements View.OnClickListener {

    EditText firstName, lastName, expYears, from, to, area;
    TextView mail, phone, gender, country, city, age;
    Spinner sGender, sCountry, sCity, sDate, sDate1;
    Button birthPicker, idPicker, saveData;
    ImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_registration);
        pic = findViewById(R.id.pro_reg_pic);
        firstName = findViewById(R.id.pro_reg_first_name);
        lastName = findViewById(R.id.pro_reg_last_name);
        expYears = findViewById(R.id.pro_reg_edit_years_exp);
        area = findViewById(R.id.pro_reg_edit_area);
        to = findViewById(R.id.pro_reg_edit_to);
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
        birthPicker = findViewById(R.id.pro_reg_button_age);

        birthPicker.setOnClickListener(this);
        pic.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

    }
}
