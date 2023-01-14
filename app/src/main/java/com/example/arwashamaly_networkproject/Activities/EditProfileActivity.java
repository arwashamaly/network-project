package com.example.arwashamaly_networkproject.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.arwashamaly_networkproject.Utility.BaseActivity;
import com.example.arwashamaly_networkproject.Utility.User;
import com.example.arwashamaly_networkproject.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class EditProfileActivity extends BaseActivity {
    ActivityEditProfileBinding binding;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null) {
            binding.etName.setText(intent.getStringExtra("name"));
        }

        //اختيار صورة
        ActivityResultLauncher<String> al1 = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        uri = result;
                        Glide.with(EditProfileActivity.this).load(result).circleCrop()
                                .into(binding.imgUserPhoto);
                    }
                });
        binding.imgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al1.launch("image/*");
            }
        });
        binding.imgCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al1.launch("image/*");
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.etName.getText().toString();
                String city = binding.spCountry.getSelectedItem().toString();
                if (uri != null) {
                    StorageReference reference = firebaseStorage.getReference("users/" + user.getUid() + "/" + uri.getLastPathSegment());
                    UploadTask uploadTask = reference.putFile(uri);
                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return reference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                String gender = "";
                                if (binding.rbFemale.isChecked()) {
                                    gender = "Female";
                                } else if (binding.rbMale.isChecked()) {
                                    gender = "Male";
                                }

                                String uriString = task.getResult().toString();

                                if (!name.isEmpty() && !city.isEmpty() && !gender.isEmpty() &&
                                        !uriString.isEmpty()) {
                                    User user = new User(name, city, gender, uriString);
                                    updateUserInfo(user);
                                    finish();
                                }

                            } else {
                                Toast.makeText(EditProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateUserInfo(User user1) {
        DocumentReference document = firebaseFirestore.collection("Users").document(user.getUid());
        document.set(user1);
    }
}