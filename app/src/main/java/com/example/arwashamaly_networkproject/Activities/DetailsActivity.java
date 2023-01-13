package com.example.arwashamaly_networkproject.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.arwashamaly_networkproject.R;
import com.example.arwashamaly_networkproject.Utility.BaseActivity;
import com.example.arwashamaly_networkproject.databinding.ActivityDetailsBinding;

public class DetailsActivity extends BaseActivity {
    ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        setTitle("Marilyn perfumes");

        Intent intent = getIntent();
        if (intent != null) {
            binding.tvName.setText(intent.getStringExtra("name"));
            Glide.with(this).load(intent.getStringExtra("photo")).into(binding.imgPhoto);
            binding.tvPrice.setText(intent.getIntExtra("price", 0) + " $");
            binding.tvDetails.append(intent.getStringExtra("details"));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profileIcon:
                if (user != null) {
                    startActivity(new Intent(getBaseContext(), ProfileActivity.class));
                } else {
                    new AlertDialog.Builder(DetailsActivity.this)
                            .setTitle("Sign in")
                            .setMessage("You must log in first!")
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("sign in", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                                    finish();
                                }
                            })
                            .create().show();
                }
                return true;
        }
        return false;
    }
}