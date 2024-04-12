package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Adapters.ResultAdapter;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Models.CourseModel;
import com.example.myapplication.Models.ResultModel;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultListActivity extends AppCompatActivity {
    ArrayList<ResultModel> results = new ArrayList<>();
    SearchView searchView;
    ResultAdapter adapter;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        databaseHelper = new DatabaseHelper(this);
        results = databaseHelper.getAllResult();

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ArrayAdapter<CharSequence> sortOptionAdapter = ArrayAdapter.createFromResource(this, R.array.sort_option, R.layout.dropdown_sortby_list);
        sortOptionAdapter.setDropDownViewResource(R.layout.dropdown_sortby_list);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(sortOptionAdapter);

        RecyclerView recyclerView = findViewById(R.id.resultList);
        adapter = new ResultAdapter(this,results);
        recyclerView.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Get the selected item
                String selectedItem = parent.getItemAtPosition(position).toString();

                // Handle the click event of the selected item here
                switch(selectedItem){
                    case "Correct answer":
                        results.sort(ResultModel.SortByCorrect);
                        adapter.notifyDataSetChanged();
                        break;
                    case "Wrong answer":
                        results.sort(ResultModel.SortByWrong);
                        adapter.notifyDataSetChanged();
                        break;
                    case "Newest":
                        results.sort(ResultModel.SortByNewest);
                        adapter.notifyDataSetChanged();
                        break;
                    case "Oldest":
                        results.sort(ResultModel.SortByOldest);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected (optional)
            }
        });


//        results.sort(ResultModel.SortByOldest);
//        adapter.notifyDataSetChanged();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void filterList(String text) {
        ArrayList<ResultModel> filteredList = new ArrayList<>();
        for (ResultModel result : results){
            if(result.getExaminee().toLowerCase().contains(text.toLowerCase())) filteredList.add(result);
        }

        if(filteredList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else{
            adapter.setFilteredList(filteredList);
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