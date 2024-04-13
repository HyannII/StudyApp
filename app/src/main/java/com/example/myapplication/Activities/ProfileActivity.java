package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Models.UserModel;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityProfileBinding;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    DatabaseHelper databaseHelper;
    private final int GALLERY_REQ_CODE = 1000;
    private byte[] avatarBytes;

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
        binding.birthdayPickerText.setText(user.getBirthday());
        binding.txtGenderProfile.setText(user.getGender());
        binding.txtEmailProfile.setText(user.getEmail());
        avatarBytes = user.getAvatar();

        if (avatarBytes != null) {
            InputStream inputStream = new ByteArrayInputStream(avatarBytes);
            Bitmap avatarBitmap = BitmapFactory.decodeStream(inputStream);
            binding.profileImg.setImageBitmap(avatarBitmap);
        } else {
            binding.profileImg.setImageResource(R.drawable.account_person);
            Toast.makeText(ProfileActivity.this, "No image", Toast.LENGTH_SHORT).show();
        }

        binding.txtProfilePhoneNum.setText(user.getPhonenumber());

        binding.profileImg.setOnClickListener(v -> {
            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGallery, GALLERY_REQ_CODE);
        });
        binding.birthdayPickerBtn.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ProfileActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> binding.birthdayPickerText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1),
                    year, month, day);
            datePickerDialog.show();
        });
        binding.btnSave.setOnClickListener(v -> {
            user.setAvatar(avatarBytes);
            user.setFullname(binding.txtFullnameProfile.getText().toString());
            user.setBirthday(binding.birthdayPickerText.getText().toString());
            user.setGender(binding.txtGenderProfile.getText().toString());
            user.setEmail(binding.txtEmailProfile.getText().toString());
            user.setPhonenumber(binding.txtProfilePhoneNum.getText().toString());

            databaseHelper.updateUser(user);
            setResult(RESULT_OK);
            finish();
        });

        MaterialToolbar materialToolbar = findViewById(R.id.materialToolbar);
        materialToolbar.setTitle("");
        setSupportActionBar(materialToolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE) {
                Uri selectedImageUri = data.getData();

                try {
                    InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);

                    Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    avatarBytes = outputStream.toByteArray();

                    binding.profileImg.setImageBitmap(selectedBitmap);

                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
