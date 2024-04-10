package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.Adapters.CourseAdapter;
import com.example.myapplication.Models.CourseModel;
import com.example.myapplication.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Objects;

public class ListActivity extends AppCompatActivity {

    ArrayList<CourseModel> cours = new ArrayList<>();

    int [] images ={R.drawable.ly_thuyet, R.drawable.youtube};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        RecyclerView recyclerView = findViewById(R.id.MainList);

        MaterialToolbar materialToolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String action = intent.getStringExtra("action");
        setCourse(action);
        CourseAdapter adapter = new CourseAdapter(this, cours, action);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setCourse(String action){
        String [] chapters = getResources().getStringArray(R.array.chapter);
        String [] title = getResources().getStringArray(R.array.name_of_course);
        String [] exercises = getResources().getStringArray(R.array.name_of_exercise);
        String [] introductions = getResources().getStringArray(R.array.name_of_introduction);

        if(action.equals("read")){
            for (int i = 0; i < chapters.length; i++) {
                cours.add(new CourseModel(chapters[i], title[i], images[0]));
            }
        }
        else if (action.equals("video")){
            for (int i = 0; i < chapters.length; i++) {
                cours.add(new CourseModel(chapters[i], title[i], images[1]));
            }
        }else if (action.equals("exercise")){
            for(int i = 0;i < exercises.length; i++){
                cours.add(new CourseModel("Exercise "+(i+1),exercises[i],images[0]));
            }
        }else if (action.equals("introduction")){
            for(int i = 0;i < introductions.length; i++){
                cours.add(new CourseModel("Introduction "+(i+1),introductions[i],images[0]));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}