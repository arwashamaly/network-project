package com.example.arwashamaly_networkproject.Activities;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.arwashamaly_networkproject.Utility.BaseActivity;
import com.example.arwashamaly_networkproject.databinding.ActivityChangePasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;

public class ChangePasswordActivity extends BaseActivity {
    ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSaveNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = binding.etOldPass.getText().toString();
                String newPass = binding.etNewPass.getText().toString();
                if (!oldPass.isEmpty() && !newPass.isEmpty()) {
                    updatePassword(oldPass, newPass);
                    finish();
                }
            }
        });
    }

    private void updatePassword(String oldPassword, String newPassword) {
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    user.updatePassword(newPassword);
                    Toast.makeText(ChangePasswordActivity.this, "Change password successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        user.reload();
    }

}