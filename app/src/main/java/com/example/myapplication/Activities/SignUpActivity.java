package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.txtUserName.getText().toString();
                String password = binding.txtPassword.getText().toString();
                String confirmPassword = binding.txtConfirmPass.getText().toString();

                if(username.equals("") || password.equals("")||confirmPassword.equals(""))
                    Toast.makeText(SignUpActivity.this,"Missing username or password",Toast.LENGTH_SHORT).show();
                else{
                    binding.btnSignUp.setEnabled(true);
                    if(password.equals(confirmPassword)){
                        Boolean checkUsername = databaseHelper.checkUser(username);

                        if(!checkUsername){
                            Boolean insert = databaseHelper.addUser(username,password);

                            if (insert){
                                Toast.makeText(SignUpActivity.this,"Signup Sucessfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(SignUpActivity.this,"Signup Failed",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(SignUpActivity.this,"User already exists",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(SignUpActivity.this,"Password is not the same",Toast.LENGTH_LONG).show();
                        binding.txtConfirmPass.setText("");
                    }
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