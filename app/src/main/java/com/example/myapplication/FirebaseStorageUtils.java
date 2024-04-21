package com.example.myapplication;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FirebaseStorageUtils {
    public interface StringDownloadListener {
        void onSuccess(String result);
        void onFailure(Exception e);
    }

    public static void downloadStringFromUrl(String url, StringDownloadListener listener) {
        // Tạo một tham chiếu tới Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(url);

        // Tải nội dung từ URL
        StreamDownloadTask task = storageRef.getStream();
        task.addOnSuccessListener(taskSnapshot -> {
            // Lấy InputStream từ taskSnapshot
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(taskSnapshot.getStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();
                String result = stringBuilder.toString();
                listener.onSuccess(result);
            } catch (IOException e) {
                listener.onFailure(e);
            }
        }).addOnFailureListener(exception -> {
            listener.onFailure(exception);
        });
    }
}
