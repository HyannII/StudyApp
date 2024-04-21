package com.example.myapplication.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ResultListActivity extends AppCompatActivity {
    ArrayList<ResultModel> results = new ArrayList<>();
    ArrayList<ResultModel> filteredList = new ArrayList<>();
    SearchView searchView;
    ResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();

        ArrayAdapter<CharSequence> sortOptionAdapter = ArrayAdapter.createFromResource(this, R.array.sort_option, R.layout.dropdown_sortby_list);
        sortOptionAdapter.setDropDownViewResource(R.layout.dropdown_sortby_list);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(sortOptionAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference resultsRef = db.collection("Results");

        resultsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        String userId = document.getString("user_id");
                        String userName = document.getString("user_name");
                        long correctLong = document.getLong("correct");
                        long wrongLong = document.getLong("wrong");
                        int correct = (int) correctLong;
                        int wrong = (int) wrongLong;
                        Timestamp startTime = document.getTimestamp("start_time");
                        String timeLeft = document.getString("timeLeft");

                        ResultModel result = new ResultModel(userId, userName, correct, wrong, startTime, timeLeft);
                        results.add(result);
                    }

                    // Hiển thị hoặc sử dụng resultList ở đây
                    for (ResultModel result : results) {
                        Log.d(TAG, "User ID: " + result.getUserId() + ", User Name: " + result.getUserName() +
                                ", Correct: " + result.getCorrect() + ", Wrong: " + result.getWrong() +
                                ", Start Time: " + result.getStartTime() + ", Time Left: " + result.getTimeLeft());
                    }

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

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        RecyclerView recyclerView = findViewById(R.id.resultList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ResultAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    private void filterList(String searchText, String filterCondition) {
        filteredList.clear();
        for (ResultModel result : results) {
            if (result.getUserName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(result);
            }
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
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
