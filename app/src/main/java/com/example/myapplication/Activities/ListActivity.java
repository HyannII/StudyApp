package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myapplication.Adapters.DocumentAdapter;
import com.example.myapplication.Models.DocumentModel;
import com.example.myapplication.R;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class ListActivity extends AppCompatActivity {

    ArrayList<DocumentModel> cours = new ArrayList<>();
    TextView actionName;
    RecyclerView recyclerView;
    DocumentAdapter adapter;
    StorageReference storageReference;

    int [] images ={R.drawable.ly_thuyet, R.drawable.youtube, R.drawable.write, R.drawable.question};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        storageReference = FirebaseStorage.getInstance().getReference();

        recyclerView = findViewById(R.id.MainList);
        actionName = findViewById(R.id.actionName);
        MaterialToolbar materialToolbar = findViewById(R.id.materialToolbar);
        materialToolbar.setTitle("");
        setSupportActionBar(materialToolbar);

        Intent intent = getIntent();
        String action = intent.getStringExtra("action");
        setCourse(action);
        adapter = new DocumentAdapter(this, cours, action);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setCourse(String action) {
        StorageReference storageRef = null;
        String title;
        int icon;
        switch (action) {
            case "read":
                storageRef = storageReference.child("material/");
                title = "Chapter ";
                icon = images[0];
                actionName.setText("Material");
                break;
            case "exercise":
                storageRef = storageReference.child("exercise/");
                title = "Exercise ";
                icon = images[2];
                actionName.setText("Exercise");
                break;
            case "introduction":
                storageRef = storageReference.child("introduction/");
                title = "Introduction ";
                icon = images[3];
                actionName.setText("Introduction");
                break;
            case "video":
                storageRef = storageReference.child("videoId/");
                title = "Video ";
                icon = images[1];
                actionName.setText("Video");
                break;
            default:
                icon = images[0];
                title = "";
                break;
        }
        Comparator<StorageReference> comparator = (file1, file2) -> {
            String name1 = file1.getName();
            String name2 = file2.getName();
            return name1.compareTo(name2); // So sánh các tên file
        };

        storageRef.listAll()
                .addOnSuccessListener(listResult -> {
                    // Khởi tạo danh sách tệp tạm thời
                    List<StorageReference> tempFiles = new ArrayList<>();
                    for (StorageReference item : listResult.getItems()) {
                        tempFiles.add(item); // Thêm tệp vào danh sách tạm thời
                    }
                    // Sắp xếp danh sách tệp tạm thời theo thứ tự tăng dần của tên file
                    tempFiles.sort(comparator);

                    // Khởi tạo biến đếm
                    AtomicInteger index = new AtomicInteger(1);
                    // Thực hiện việc tải xuống và tạo DocumentModel cho mỗi tệp
                    for (StorageReference item : tempFiles) {
                        // Sử dụng AsyncTask để tải xuống một cách bất đồng bộ
                        new DownloadTask(title, icon).execute(item, index.getAndIncrement());
                    }
                })
                .addOnFailureListener(exception -> {
                    Log.e("Firebase Storage", "Error getting file list", exception);
                });

// AsyncTask để thực hiện việc tải xuống một cách bất đồng bộ

    }
    class DownloadTask extends AsyncTask<Object, Void, DocumentModel> {
        private String title;
        private int icon;

        // Constructor để truyền title và icon
        public DownloadTask(String title, int icon) {
            this.title = title;
            this.icon = icon;
        }
        @Override
        protected DocumentModel doInBackground(Object... params) {
            StorageReference item = (StorageReference) params[0];
            int index = (int) params[1];
            try {
                // Đợi cho đến khi tải xuống hoàn tất
                Uri uri = Tasks.await(item.getDownloadUrl());
                String fileName = item.getName().replaceFirst("[.][^.]+$", "");
                String fileUri = uri.toString();
                // Tạo DocumentModel cho tệp đã tải xuống và trả về
                return new DocumentModel(title + index, fileName, fileUri, icon);
            } catch (ExecutionException | InterruptedException e) {
                Log.e("Firebase Storage", "Error getting file URI", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(DocumentModel documentModel) {
            if (documentModel != null) {
                // Thêm DocumentModel vào danh sách và thông báo cho Adapter cập nhật
                cours.add(documentModel);
                adapter.notifyDataSetChanged();
                Log.d("File Info", "Name: " + documentModel.getTitleContent() + ", URI: " + documentModel.getDocumentUri());
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