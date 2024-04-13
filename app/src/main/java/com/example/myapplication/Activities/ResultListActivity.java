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

import com.example.myapplication.Adapters.ResultAdapter;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Models.ResultModel;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ResultListActivity extends AppCompatActivity {
    ArrayList<ResultModel> results;
    ArrayList<ResultModel> filteredList;
    SearchView searchView;
    ResultAdapter adapter;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        databaseHelper = new DatabaseHelper(this);
        results = databaseHelper.getAllResult();
        filteredList = new ArrayList<>();

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ArrayAdapter<CharSequence> sortOptionAdapter = ArrayAdapter.createFromResource(this, R.array.sort_option, R.layout.dropdown_sortby_list);
        sortOptionAdapter.setDropDownViewResource(R.layout.dropdown_sortby_list);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(sortOptionAdapter);

        RecyclerView recyclerView = findViewById(R.id.resultList);
        adapter = new ResultAdapter(this,filteredList);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText, spinner.getSelectedItem().toString());
                return false;
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterList(searchView.getQuery().toString(), parent.getItemAtPosition(position).toString());
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

    private void filterList(String searchText, String filterCondition) {
        filteredList.clear();
        for (ResultModel result : results) {
            if(result.getExaminee().toLowerCase().contains(searchText.toLowerCase())) filteredList.add(result);
        }
        switch (filterCondition) {
            case "Correct answer":
                filteredList.sort(ResultModel.SortByCorrect);
                break;
            case "Wrong answer":
                filteredList.sort(ResultModel.SortByWrong);
                break;
            case "Newest":
                filteredList.sort(ResultModel.SortByNewest);
                break;
            case "Oldest":
                filteredList.sort(ResultModel.SortByOldest);
                break;
        }
        adapter.setResults(filteredList);
        adapter.notifyDataSetChanged();
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