package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    DatabaseHelper databaseHelper;
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
        databaseHelper = new DatabaseHelper(this);

//        if(fAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//            finish();
//        }

        TextView errorUsername = findViewById(R.id.errorUsername);
        TextView errorPassword = findViewById(R.id.errorPassword);
        TextView errorRetypePW = findViewById(R.id.errorRetypePW);
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.txtUserName.getText().toString();
                String password = binding.txtPassword.getText().toString();
                String email = binding.txtEmail.getText().toString();
                String confirmPassword = binding.txtConfirmPass.getText().toString();

                Boolean checkUsername = databaseHelper.checkUser(username);
                Boolean isValidUsername = username.matches("^[a-zA-Z0-9_-]{3,16}$");
                Boolean isValidPassword = password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

                if (!isValidUsername) errorUsername.setText("Username only allows alphabet characters, numbers, underscores, and hyphens, with a length between 3 and 16 characters");
                else errorUsername.setText("");

                if (!isValidPassword){
                    errorPassword.setText("Password must have at least 8 characters, with at least one alphabet character, one digit, and one special character");
                    binding.txtPassword.setText("");
                }
                else errorPassword.setText("");

                if(password.equals(confirmPassword)){
                    if(!checkUsername){
                        if( isValidPassword){
//                            Boolean insert = databaseHelper.addUser(username,password,"","","","","",null);
                            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SignUpActivity.this,"Signup Sucessfully",Toast.LENGTH_SHORT).show();
                                        userID = fAuth.getCurrentUser().getUid();
                                        DocumentReference documentReference = fStore.collection("users").document(userID);
                                        Map<String,Object> user = new HashMap<>();
                                        user.put("fname",username);
                                        user.put("email",email);
                                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(SignUpActivity.this,"User Created",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(SignUpActivity.this,"Signup Failed",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }else{
                        errorUsername.setText("Username already exist");
                    }
                }else{
                    errorRetypePW.setText("Password is not the same");
                    binding.txtConfirmPass.setText("");
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