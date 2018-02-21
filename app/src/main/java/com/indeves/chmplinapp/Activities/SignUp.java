package com.indeves.chmplinapp.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.indeves.chmplinapp.R;

public class SignUp extends AppCompatActivity {
    RadioButton radioButton,radioButton2,radioButton3;
    RadioGroup radioGroup;
    Button createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        createAccount = findViewById(R.id.signUp_button_createAccount);
        radioButton = findViewById(R.id.signUp_radio_user_account);
        radioButton2 = findViewById(R.id.signUp_radio_pro_account);
        radioButton3 = findViewById(R.id.signUp_radio_stu_account);

        final ImageView splash = (ImageView) findViewById(R.id.splash);
        //splash.startAnimation(anim);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(splash,"translationY",-1000,0),
                ObjectAnimator.ofFloat(splash,"alpha",0,1)
        );
        animatorSet.setDuration(2000);
       /* animatorSet.addListener(new AnimatorListenerAdapter(){
            @Override public void onAnimationEnd(Animator animation) {

                AnimatorSet animatorSet2 = new AnimatorSet();
                animatorSet2.playTogether(
                        ObjectAnimator.ofFloat(splash,"scaleX", 1f, 0.5f, 1f),
                        ObjectAnimator.ofFloat(splash,"scaleY", 1f, 0.5f, 1f)
                );
                animatorSet2.setInterpolator(new AccelerateInterpolator());
                animatorSet2.setDuration(1000);
                animatorSet2.start();

            }
        });
       */ animatorSet.start();
        // get selected radio button from radioGroup

        // find the radiobutton by returned id
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (radioButton.isChecked()) {
                    Intent intent = new Intent(SignUp.this, SignUpConfirmCode.class);
                    intent.putExtra("accountType","user");
                    startActivity(intent);
                }else if (radioButton2.isChecked()) {
                    Intent intent = new Intent(SignUp.this, SignUpConfirmCode.class);
                    intent.putExtra("accountType","pro");
                    startActivity(intent);

                }else {
                    Intent intent = new Intent(SignUp.this, SignUpConfirmCode.class);
                    intent.putExtra("accountType","stu");
                    startActivity(intent);
                }
            }
        });
    }
}
