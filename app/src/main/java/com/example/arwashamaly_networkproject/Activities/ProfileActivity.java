package com.example.arwashamaly_networkproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arwashamaly_networkproject.Utility.BaseActivity;
import com.example.arwashamaly_networkproject.Utility.User;
import com.example.arwashamaly_networkproject.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProfileActivity extends BaseActivity {
    ActivityProfileBinding binding;
    User user1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getUserInfo();

        binding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EditProfileActivity.class);
                if (user1 != null) {
                    intent.putExtra("name", user1.getName());
                }
                startActivity(intent);
            }
        });
        binding.tvChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(),ChangePasswordActivity.class));
            }
        });
        binding.tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Logout")
                        .setMessage("You are about to log out of your account!")
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                firebaseAuth.signOut();
                                finish();
                            }
                        })
                        .create().show();
            }
        });
    }

    private void getUserInfo() {
        DocumentReference document = firebaseFirestore.collection("Users").document(user.getUid());
        document.get().addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    user1 = task.getResult().toObject(User.class);
                    if (user1 != null) {
                        binding.tvUsername.setText("Name : " + user1.getName());
                        binding.tvUserCity.setText("City : " + user1.getCity());
                        binding.tvUserGender.setText("Gender : " + user1.getGender());
                        Glide.with(ProfileActivity.this).load(user1.getPhoto())
                                .circleCrop().into(binding.imgUser);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        getUserInfo();
        super.onResume();
    }
}