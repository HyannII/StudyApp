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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Models.UserModel;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    DatabaseHelper databaseHelper;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storageReference;
    DocumentReference documentReference;
    String userId,imageURI;
    private final int GALLERY_REQ_CODE = 1000;
    private byte[] avatarBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = fAuth.getCurrentUser().getUid();

        StorageReference profileRef = storageReference.child("users/"+ userId +"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imageURI = uri.toString();
//                Picasso.get().load(uri).into(binding.profileImg);
            }
        });

        documentReference = fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                binding.txtUserProfile.setText(value.getString("fname"));
                binding.txtFullnameProfile.setText(value.getString("fname"));
                binding.txtEmailProfile.setText(value.getString("email"));
                binding.txtProfilePhoneNum.setText(value.getString("phone"));
                binding.txtGenderProfile.setText(value.getString("gender"));
                binding.birthdayPickerText.setText(value.getString("birthday"));
                binding.progressBar2.setVisibility(View.VISIBLE);
                if(value.getString("img")!=null){
                    Picasso.get().load(Uri.parse(value.getString("img"))).into(binding.profileImg, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            // Ảnh đã tải thành công
                            binding.progressBar2.setVisibility(View.GONE);
                            Log.d("Picasso", "Image loaded successfully");
                        }
                        @Override
                        public void onError(Exception e) {
                            // Xử lý lỗi khi tải ảnh
                            Log.e("Picasso", "Error loading image: " + e.getMessage());
                        }
                    });
                }
            }
        });

        if (avatarBytes != null) {
            InputStream inputStream = new ByteArrayInputStream(avatarBytes);
            Bitmap avatarBitmap = BitmapFactory.decodeStream(inputStream);
            binding.profileImg.setImageBitmap(avatarBitmap);
        } else {
            binding.profileImg.setImageResource(R.drawable.account_person);
            Toast.makeText(ProfileActivity.this, "No image", Toast.LENGTH_SHORT).show();
        }

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
            documentReference = fstore.collection("users").document(userId);
            Map<String,Object> user = new HashMap<>();
            user.put("fname",binding.txtFullnameProfile.getText().toString());
            user.put("birthday",binding.birthdayPickerText.getText().toString());
            user.put("gender", binding.txtGenderProfile.getText().toString());
            user.put("email",binding.txtEmailProfile.getText().toString());
            user.put("phone",binding.txtProfilePhoneNum.getText().toString());
            user.put("img",imageURI);
            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(ProfileActivity.this,"Profile saved",Toast.LENGTH_LONG).show();
                }
            });

//            databaseHelper.updateUser(user);
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
                uploadImageToFirebase(selectedImageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri selectedImageUri) {
        StorageReference fileRef = storageReference.child("users/"+ userId +"/profile.jpg");

        binding.progressBar2.setVisibility(View.VISIBLE);
        fileRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(binding.profileImg, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                binding.progressBar2.setVisibility(View.GONE);
                                Log.d("Picasso", "Image loaded successfully");
                            }
                            @Override
                            public void onError(Exception e) {
                                // Xử lý lỗi khi tải ảnh
                                Log.e("Picasso", "Error loading image: " + e.getMessage());
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this,"Image Upload Failed",Toast.LENGTH_SHORT).show();
            }
        });

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
