package com.example.myapplication.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Models.UserModel;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_PROFILE_ACTIVITY = 1;
    private DrawerLayout drawerLayout;
    private ImageButton drawerToggle;
    private CardView introduction, readDocument,exercise, test, videoPlayer, profile;
    private TextView userName, userNameNav, emailNav;
    private ImageView avatar, avatarNav;
    private NavigationView navigationView;
    DatabaseHelper databaseHelper;
    UserModel user;
    String savedUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = findViewById(R.id.drawer_toggle);

        introduction = findViewById(R.id.introductionBtn);
        readDocument = findViewById(R.id.readDocumentBtn);
        exercise = findViewById(R.id.exerciseBtn);
        test = findViewById(R.id.testBtn);
        videoPlayer = findViewById(R.id.videoBtn);
        profile = findViewById(R.id.btnProfile);

        navigationView = drawerLayout.findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        Menu menu = navigationView.getMenu();
        MenuItem navLogoutItem = menu.findItem(R.id.nav_logout);
        MenuItem navProfileItem = menu.findItem(R.id.nav_profile);
        MenuItem navTestResultsItem = menu.findItem(R.id.nav_test_results);
        MenuItem navHome = menu.findItem(R.id.nav_home);

        userName = findViewById(R.id.txtUser);
        userNameNav = headerView.findViewById(R.id.txtUsername);
        emailNav = headerView.findViewById(R.id.txtEmail);
        avatar = findViewById(R.id.userImg);
        avatarNav = headerView.findViewById(R.id.userImg);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        savedUsername = prefs.getString("username", null);

        user = databaseHelper.getUser(savedUsername);
        userName.setText(savedUsername);
        userNameNav.setText(savedUsername);
        emailNav.setText(user.getEmail());

        byte[] avatarBytes = user.getAvatar();
        if (avatarBytes != null) {
            InputStream inputStream = new ByteArrayInputStream(avatarBytes);
            Bitmap avatarBitmap = BitmapFactory.decodeStream(inputStream);
            avatar.setImageBitmap(avatarBitmap);
            avatarNav.setImageBitmap(avatarBitmap);
        }else{
            avatar.setImageResource(R.drawable.account_person);
            avatarNav.setImageResource(R.drawable.account_person);
        }

//        if(checkPermission() == false){
//            requestPermission();
//            return;
//        }
        // Set click listener for drawer toggle button
        drawerToggle.setOnClickListener(v -> drawerLayout.open());
        if (navLogoutItem != null) {
            navLogoutItem.setOnMenuItemClickListener(item -> {
                showLogoutConfirmationDialog();
                return false;
            });
        }
        if (navProfileItem != null) {
            navProfileItem.setOnMenuItemClickListener(item -> {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                drawerLayout.close();
                return false;
            });
        }
        if (navTestResultsItem != null) {
            navTestResultsItem.setOnMenuItemClickListener(item -> {
                Intent intent = new Intent(MainActivity.this, ResultListActivity.class);
                startActivity(intent);
                drawerLayout.close();
                return false;
            });
        }
        if (navHome != null) {
            navHome.setOnMenuItemClickListener(item ->{
                drawerLayout.close();
                return false;
            });
        }

        introduction.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            intent.putExtra("action", "introduction");
            startActivity(intent);
        });
        readDocument.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            intent.putExtra("action", "read");
            startActivity(intent);
        });
        exercise.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            intent.putExtra("action", "exercise");
            startActivity(intent);
        });
        test.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            startActivity(intent);
        });
        videoPlayer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            intent.putExtra("action", "video");
            startActivity(intent);
        });
        profile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivityForResult(intent, REQUEST_PROFILE_ACTIVITY);
        });

    }
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Dismiss the dialog
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_PROFILE_ACTIVITY){
            user = databaseHelper.getUser(savedUsername);
            userName.setText(savedUsername);
            userNameNav.setText(savedUsername);
            emailNav.setText(user.getEmail());
            byte[] avatarBytes = user.getAvatar();
            if (avatarBytes != null) {
                InputStream inputStream = new ByteArrayInputStream(avatarBytes);
                Bitmap avatarBitmap = BitmapFactory.decodeStream(inputStream);
                avatar.setImageBitmap(avatarBitmap);
                avatarNav.setImageBitmap(avatarBitmap);
            }else{
                avatar.setImageResource(R.drawable.account_person);
                avatarNav.setImageResource(R.drawable.account_person);
            }
        }
    }

    //    boolean checkPermission(){
//        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
//            int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_MEDIA_AUDIO);
//            if(result == PackageManager.PERMISSION_GRANTED){
//                return true;
//            }else{
//                return false;
//            }
//        }else{
//            int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
//            if(result == PackageManager.PERMISSION_GRANTED){
//                return true;
//            }else{
//                return false;
//            }
//        }
//    }
//
//    void requestPermission(){
//        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
//            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_MEDIA_AUDIO)){
//                Toast.makeText(MainActivity.this,"READ PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTTINGS",Toast.LENGTH_SHORT).show();
//            }else
//                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_MEDIA_AUDIO},123);
//        }else{
//            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
//                Toast.makeText(MainActivity.this,"READ PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTTINGS",Toast.LENGTH_SHORT).show();
//            }else
//                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
//        }
//    }
}