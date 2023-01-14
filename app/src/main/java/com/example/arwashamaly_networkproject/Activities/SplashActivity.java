package com.example.arwashamaly_networkproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;

import com.example.arwashamaly_networkproject.R;
import com.example.arwashamaly_networkproject.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageView.setAnimation(
                AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoomout));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getBaseContext(), HomeActivity.class));
                finish();
            }
        }, 2000);

    }
}