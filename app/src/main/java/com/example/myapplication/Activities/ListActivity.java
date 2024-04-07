package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.Adapters.CourseAdapter;
import com.example.myapplication.Models.CourseModel;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ArrayList<CourseModel> cours = new ArrayList<>();

    int [] images ={R.drawable.ic_title};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = findViewById(R.id.MainList);

        setCourse();

        Intent intent = getIntent();
        String action = intent.getStringExtra("action");
        if (action != null && action.equals("read")) {
            CourseAdapter adapter = new CourseAdapter(this, cours, action);
            recyclerView.setAdapter(adapter);
        } else if (action != null && action.equals("video")){
            CourseAdapter adapter = new CourseAdapter(this, cours, action);
            recyclerView.setAdapter(adapter);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setCourse(){
        String [] courseNames = getResources().getStringArray(R.array.course_name);

        for (String courseName : courseNames) {
            cours.add(new CourseModel(courseName, images[0]));
        }
    }
}