package com.indeves.chmplinapp.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.indeves.chmplinapp.R;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = findViewById(R.id.splash);
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(imageView, "translationY", -1000, 0),
                ObjectAnimator.ofFloat(imageView, "alpha", 0, 1)
        );
        animatorSet.setDuration(2000);
        animatorSet.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // find the  loggin status vis sharedpreferences

            }
        }, 1000);

    }
}

