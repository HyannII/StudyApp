package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        TextView errorUsername = findViewById(R.id.errorUsername);
        TextView errorEmail = findViewById(R.id.errorEmail);
        TextView errorPassword = findViewById(R.id.errorPassword);
        TextView errorRetypePW = findViewById(R.id.errorRetypePW);
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.txtUserName.getText().toString();
                String password = binding.txtPassword.getText().toString();
                String email = binding.txtEmail.getText().toString();
                String confirmPassword = binding.txtConfirmPass.getText().toString();


                Boolean isValidUsername = username.matches("^[a-zA-Z0-9_-]{3,16}$");
                Boolean isValidPassword = password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
                Boolean isValidEmail = email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

                if (!isValidUsername) {
                    errorUsername.setText("Username only allows alphabet characters, numbers, underscores, and hyphens, with a length between 3 and 16 characters");

                } else {
                    errorUsername.setText("");
                }

                if (!isValidPassword){
                    errorPassword.setText("Password must have at least 8 characters, with at least one alphabet character, one digit, and one special character");
                    binding.txtPassword.setText("");
                } else {
                    errorPassword.setText("");
                }

                if(!isValidEmail) {
                    errorEmail.setText("Not a valid email address");

                } else {
                    errorEmail.setText("");
                }

                // Kiểm tra sự tồn tại của email và username trước khi tạo tài khoản
                if(isValidEmail && isValidUsername && isValidPassword){
                fStore.collection("users")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                if (!task1.getResult().isEmpty()) {
                                    errorEmail.setText("Email already exists");
                                    return;
                                }

                                // Kiểm tra sự tồn tại của username trong Firestore
                                fStore.collection("users")
                                        .whereEqualTo("fname", username)
                                        .get()
                                        .addOnCompleteListener(task3 -> {
                                            if (task3.isSuccessful()) {
                                                if (!task3.getResult().isEmpty()) {
                                                    errorUsername.setText("Username already exists");
                                                    return;
                                                }

                                                // Tiếp tục xử lý tạo tài khoản nếu email và username hợp lệ và không tồn tại
                                                if (password.equals(confirmPassword)) {
                                                    fAuth.createUserWithEmailAndPassword(email, password)
                                                            .addOnCompleteListener(task2 -> {
                                                                if (task2.isSuccessful()) {
                                                                    Toast.makeText(SignUpActivity.this, "Signup Successfully", Toast.LENGTH_SHORT).show();
                                                                    userID = fAuth.getCurrentUser().getUid();
                                                                    DocumentReference documentReference = fStore.collection("users").document(userID);
                                                                    Map<String, Object> user = new HashMap<>();
                                                                    user.put("fname", username);
                                                                    user.put("email", email);
                                                                    documentReference.set(user)
                                                                            .addOnSuccessListener(aVoid -> {
                                                                                Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_LONG).show();
                                                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                                                startActivity(intent);
                                                                                finish();
                                                                            })
                                                                            .addOnFailureListener(e -> {
                                                                                Log.e("Firebase Firestore", "Error adding document", e);
                                                                                Toast.makeText(SignUpActivity.this, "Error adding document", Toast.LENGTH_SHORT).show();
                                                                            });
                                                                } else {
                                                                    Toast.makeText(SignUpActivity.this, "Signup Failed", Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                } else {
                                                    errorRetypePW.setText("Password is not the same");
                                                    binding.txtConfirmPass.setText("");
                                                }
                                            } else {
                                                Log.e("Firebase Firestore", "Error checking username existence", task1.getException());
                                                Toast.makeText(SignUpActivity.this, "Error checking username existence", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Log.e("Firebase Auth", "Error checking email existence", task1.getException());
                                Toast.makeText(SignUpActivity.this, "Error checking email existence", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
            }
        });

        binding.txtSwitchToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}