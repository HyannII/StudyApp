package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageButton drawerToggle;
    private CardView introduction, readDocument,exercise, test, videoPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = findViewById(R.id.drawer_toggle);
        introduction = findViewById(R.id.introductionBtn);
        readDocument = findViewById(R.id.readDocumentBtn);
        exercise = findViewById(R.id.exerciseBtn);
        test = findViewById(R.id.testBtn);
        videoPlayer = findViewById(R.id.videoBtn);

        // Set click listener for drawer toggle button
        drawerToggle.setOnClickListener(v -> drawerLayout.open());
        introduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("action", "introduction");
                startActivity(intent);
            }
        });
        readDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("action", "read");
                startActivity(intent);
            }
        });
        exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("action", "exercise");
                startActivity(intent);
            }
        });
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });
        videoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("action", "video");
                startActivity(intent);
            }
        });
    }
}