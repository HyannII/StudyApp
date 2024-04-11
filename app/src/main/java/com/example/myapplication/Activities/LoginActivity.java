package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.txtUserName.getText().toString();
                String password = binding.txtPassword.getText().toString();

                if(username.equals("") || password.equals(""))
                    Toast.makeText(LoginActivity.this,"Missing username or password",Toast.LENGTH_SHORT).show();
                else {
                    binding.btnLogin.setEnabled(true);
                    Boolean checkAccount = databaseHelper.checkAccount(username,password);

                    if(checkAccount){
                        Toast.makeText(LoginActivity.this,"Login Sucessfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                        editor.putString("username", username);
                        editor.apply();
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this,"Wrong username or password",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        binding.txtSwitchToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

}