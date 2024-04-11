package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Models.UserModel;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityProfileBinding;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    private String username, password, fullname, birthday, gender, email, phonenumber;
    private byte[] avatar; // Sửa avatar thành byte array
    DatabaseHelper databaseHelper;
    private final int GALLERY_REQ_CODE = 1000;
    private byte[] avatarBytes; // Lưu trữ byte array của avatar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedUsername = prefs.getString("username", null);
        binding.txtUserProfile.setText(savedUsername);

        UserModel user = databaseHelper.getUser(savedUsername);

        binding.txtFullnameProfile.setText(user.getFullname());
        binding.txtBirthdayProfile.setText(user.getBirthday());
        binding.txtGenderProfile.setText(user.getGender());
        binding.txtEmailProfile.setText(user.getEmail());
        avatarBytes = user.getAvatar();

        // Chuyển đổi byte array thành Bitmap và hiển thị trên profileImg
        if (avatarBytes != null) {
            InputStream inputStream = new ByteArrayInputStream(avatarBytes);
            Bitmap avatarBitmap = BitmapFactory.decodeStream(inputStream);
            binding.profileImg.setImageBitmap(avatarBitmap);
        } else {
            // Xử lý trường hợp avatar là null (ví dụ: hiển thị hình ảnh mặc định)
            Toast.makeText(ProfileActivity.this, "No image", Toast.LENGTH_SHORT).show();
        }

        binding.txtProfilePhoneNum.setText(user.getPhonenumber());

        // Xử lý sự kiện khi nhấp vào profileImg
        binding.profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

        // Xử lý sự kiện khi nhấp vào nút Save
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setAvatar(avatarBytes);
                user.setFullname(binding.txtFullnameProfile.getText().toString());
                user.setBirthday(binding.txtBirthdayProfile.getText().toString());
                user.setGender(binding.txtGenderProfile.getText().toString());
                user.setEmail(binding.txtEmailProfile.getText().toString());
                user.setPhonenumber(binding.txtProfilePhoneNum.getText().toString());

                databaseHelper.updateUser(user);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE) {
                Uri selectedImageUri = data.getData();

                try {
                    // Lấy InputStream từ URI của hình ảnh
                    InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);

                    // Chuyển đổi InputStream thành Bitmap
                    Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream);

                    // Chuyển đổi Bitmap thành byte array
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    avatarBytes = outputStream.toByteArray();

                    // Hiển thị Bitmap trên profileImg
                    binding.profileImg.setImageBitmap(selectedBitmap);

                    // Đóng InputStream
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}