package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myapplication.R;
import com.github.barteksc.pdfviewer.PDFView;

public class ReadDocumentActivity extends AppCompatActivity {

    PDFView pdfView;
    TextView chapterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_document);

        pdfView = findViewById(R.id.pdfView);
        chapterName = findViewById(R.id.chapterName);

        int position = getIntent().getIntExtra("position", 0);
        String name = getIntent().getStringExtra("name");
        String content = getIntent().getStringExtra("content");
        String action = getIntent().getStringExtra("action");


        if(action.equals("read")){
            for (int i =0;i<8;i++){
                if(position == i){
                    pdfView.fromAsset("Chuong"+(i+1)+".pdf").load();
                    chapterName.setText(name);
                }
            }
        }else if (action.equals("exercise")){
            pdfView.fromAsset(content + " exercise.pdf").load();
            chapterName.setText(content);
        }else if (action.equals("introduction")){
            pdfView.fromAsset(content + ".pdf").load();
            chapterName.setText(content);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
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