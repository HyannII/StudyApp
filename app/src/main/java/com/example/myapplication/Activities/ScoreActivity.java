package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityScoreBinding;
import com.google.android.material.appbar.MaterialToolbar;

public class ScoreActivity extends AppCompatActivity {

    ActivityScoreBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int total = getIntent().getIntExtra("total",0);
        int correct = getIntent().getIntExtra("score",0);
        String timeLeftFormatted = getIntent().getStringExtra("time");
        int wrong = total - correct;

        binding.txtTime.setText(timeLeftFormatted);
        binding.txtTotal.setText(String.valueOf(total));
        binding.txtCorrect.setText(String.valueOf(correct));
        binding.txtWrong.setText(String.valueOf(wrong));

        MaterialToolbar materialToolbar = findViewById(R.id.materialToolbar2);
        materialToolbar.setTitle("");

        binding.btnRestart.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreActivity.this, QuizActivity.class);
            startActivity(intent);
            finish();
        });
        binding.btnQuit.setOnClickListener(v -> finish());
        binding.btnShowList.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreActivity.this, ResultListActivity.class);
            startActivity(intent);
        });
    }

}