package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myapplication.Adapters.ResultAdapter;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Models.CourseModel;
import com.example.myapplication.Models.ResultModel;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;

public class ResultListActivity extends AppCompatActivity {
    ArrayList<ResultModel> results = new ArrayList<>();
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        databaseHelper = new DatabaseHelper(this);
        results = databaseHelper.getAllResult();


        RecyclerView recyclerView = findViewById(R.id.resultList);
        ResultAdapter adapter = new ResultAdapter(this,results);
        recyclerView.setAdapter(adapter);

//        results.sort(ResultModel.SortByOldest);
//        adapter.notifyDataSetChanged();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}