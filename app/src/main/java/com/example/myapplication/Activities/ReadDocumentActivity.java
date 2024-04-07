package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
        String chapter = getIntent().getStringExtra("name");

        for (int i =0;i<8;i++){
            if(position == i){
                pdfView.fromAsset("Chuong"+(i+1)+".pdf").load();
                chapterName.setText(chapter);
            }
        }

    }
}