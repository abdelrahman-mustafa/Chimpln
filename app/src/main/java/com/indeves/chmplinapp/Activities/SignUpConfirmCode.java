package com.indeves.chmplinapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.indeves.chmplinapp.R;

public class SignUpConfirmCode extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_confirm_code);
        button = findViewById(R.id.confirm_code);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getStringExtra("accountType").equals("user")) {
                    startActivity(new Intent(SignUpConfirmCode.this, UserProfileMain.class));
                } else if (getIntent().getStringExtra("accountType").equals("pro")) {
                    startActivity(new Intent(SignUpConfirmCode.this, ProLandingPage.class));
                } else {
                    startActivity(new Intent(SignUpConfirmCode.this, StuLandingPage.class));
                }
            }
        });
    }
}
