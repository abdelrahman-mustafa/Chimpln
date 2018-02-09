package com.indeves.chmplinapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.indeves.chmplinapp.R;

public class SignUp extends AppCompatActivity {
    RadioButton radioButton,radioButton2;
    RadioGroup radioGroup;
    Button createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        createAccount = findViewById(R.id.signUp_button_createAccount);
        radioButton = findViewById(R.id.signUp_radio_user_account);
        radioButton2 = findViewById(R.id.signUp_radio_pro_account);
        // get selected radio button from radioGroup


        // find the radiobutton by returned id
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (radioButton.isChecked()) {
                    Intent intent = new Intent(SignUp.this, SignUpConfirmCode.class);
                    intent.putExtra("accountType","user");
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SignUp.this, SignUpConfirmCode.class);
                    intent.putExtra("accountType","pro");
                    startActivity(intent);

                }
            }
        });
    }
}
