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
        materialToolbar.setTitle(null);
        setSupportActionBar(materialToolbar);

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
        if(action.equals("read")){
            for (int i = 0; i < chapters.length; i++) {
                cours.add(new CourseModel(chapters[i], title[i], images[0]));
            }
        }
        else if (action.equals("video")){
            for (int i = 0; i < chapters.length; i++) {
                cours.add(new CourseModel(chapters[i], title[i], images[1]));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}