package com.example.myapplication.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Models.QuestionModel;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityQuizBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    ArrayList<QuestionModel> listQuestion = new ArrayList<>();
    private int count = 0;
    private int position = 0;
    private int score = 0;
    private int minutes,seconds;
    private String fname;
    private String timeLeftFormatted;
    CountDownTimer timer;
    ActivityQuizBinding binding;
    DatabaseHelper databaseHelper;
    String savedUsername;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        databaseHelper = new DatabaseHelper(this);
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        savedUsername = prefs.getString("username", null);

        binding.btnNext.setEnabled(false);
        enableOption(true);
        resetTimer();

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("questions");
        ArrayList<QuestionModel> tempList = new ArrayList<>(); // Danh sách tạm thời
        int listSize = 5;

        ProgressDialog progressDialog = new ProgressDialog(QuizActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Dữ liệu đã được trả về từ Firebase
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Lấy dữ liệu từ mỗi snapshot và tạo thành đối tượng QuestionModel
                    String question = snapshot.getKey();
                    String answerA = snapshot.child("answerA").getValue(String.class);
                    String answerB = snapshot.child("answerB").getValue(String.class);
                    String answerC = snapshot.child("answerC").getValue(String.class);
                    String answerD = snapshot.child("answerD").getValue(String.class);
                    String correctAnswer = snapshot.child("correctAnswer").getValue(String.class);

                    tempList.add(new QuestionModel(question, answerA, answerB, answerC, answerD, correctAnswer));
                }

                // Xáo trộn thứ tự các đối tượng trong danh sách tạm thời
                Collections.shuffle(tempList);

                // Thêm các đối tượng đã được xáo trộn vào listQuestion
                listQuestion.addAll(tempList.subList(0, Math.min(listSize, tempList.size())));
                progressDialog.dismiss();
                timer.start();
                for (int i=0;i<4;i++){
                    binding.optionContainer.getChildAt(i).setOnClickListener(view -> {
                        checkAnswer((Button) view);
                        disableButton();
                    });
                }

                playAnimation(binding.question, 0, listQuestion.get(position).getQuestion());
                binding.btnNext.setOnClickListener(view -> {

                    binding.btnNext.setEnabled(false);
                    binding.btnNext.setAlpha((float) 0.3);
                    enableOption(true);
                    position++;

                    if(position == listQuestion.size()){
                        Date now = new Date();

                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy", Locale.getDefault());
                        String formattedTime = sdf.format(now);
                        databaseHelper.addResult(savedUsername,formattedTime,score,listQuestion.size()-score,timeLeftFormatted);

                        String userId = fAuth.getCurrentUser().getUid();
                        CollectionReference usersRef = fStore.collection("users");
                        DocumentReference userDocRef = usersRef.document(userId);
                        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot userDoc = task.getResult();
                                    if (userDoc.exists()) {
                                        // Lấy tên người dùng từ document "Users"
                                        String userName = userDoc.getString("fname");

                                        // Tạo một ID mới cho kết quả bài làm
                                        String resultId = fStore.collection("Results").document().getId();

                                        // Tạo dữ liệu mới để thêm vào Firestore
                                        Map<String, Object> resultData = new HashMap<>();
                                        resultData.put("user_id", userId);
                                        resultData.put("user_name", userName);
                                        resultData.put("correct", score);
                                        resultData.put("wrong", listQuestion.size()-score);
                                        resultData.put("timeLeft", timeLeftFormatted);
                                        resultData.put("start_time", Timestamp.now()); // Thời gian hiện tại

                                        // Thêm dữ liệu mới vào collection "Results"
                                        fStore.collection("Results").document(resultId).set(resultData)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG, "Document added successfully!");
                                                        } else {
                                                            Log.w(TAG, "Error adding document", task.getException());
                                                        }
                                                    }
                                                });
                                    } else {
                                        Log.d(TAG, "User document does not exist!");
                                    }
                                } else {
                                    Log.d(TAG, "Error getting user document: ", task.getException());
                                }
                            }
                        });
                        Intent intent = new Intent(QuizActivity.this,ScoreActivity.class);
                        intent.putExtra("score",score);
                        intent.putExtra("total",listQuestion.size());
                        intent.putExtra("time",timeLeftFormatted);
                        startActivity(intent);
                        finish();
                        return;
                    }

                    count = 0;

                    playAnimation(binding.question,0,listQuestion.get(position).getQuestion());
                });
                // Bây giờ listQuestion chứa các đối tượng QuestionModel với thứ tự ngẫu nhiên
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });


        setContentView(binding.getRoot());

        MaterialToolbar materialToolbar = findViewById(R.id.materialToolbar);
        materialToolbar.setTitle("");
        setSupportActionBar(materialToolbar);
    }

    private void disableButton() {
        binding.option1.setEnabled(false);
        binding.option2.setEnabled(false);
        binding.option3.setEnabled(false);
        binding.option4.setEnabled(false);
    }

    private void resetTimer() {
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                minutes = (int) (millisUntilFinished / 1000) / 60;
                seconds = (int) (millisUntilFinished / 1000) % 60;

                timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
                binding.timer.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                binding.timer.setText("00:00");
                Date now = new Date();

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy", Locale.getDefault());
                String formattedTime = sdf.format(now);
                databaseHelper.addResult(savedUsername,formattedTime,score,listQuestion.size()-score,timeLeftFormatted);

                Intent intent = new Intent(QuizActivity.this,ScoreActivity.class);
                intent.putExtra("score",score);
                intent.putExtra("total",listQuestion.size());
                intent.putExtra("time",timeLeftFormatted);
                startActivity(intent);
                finish();
            }
        };
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    private void playAnimation(View view, int value, String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(400).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animation) {
                        if(value == 0 && count<4){
                            String option = "";
                            if(count == 0){
                                option = listQuestion.get(position).getOption1();
                            }else if(count == 1){
                                option = listQuestion.get(position).getOption2();
                            }else if(count == 2){
                                option = listQuestion.get(position).getOption3();
                            }else if(count == 3){
                                option = listQuestion.get(position).getOption4();
                            }

                            playAnimation(binding.optionContainer.getChildAt(count),0,option);
                            count++;
                        }
                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animation) {
                        if(value == 0){
                            try{
                                ((TextView)view).setText(data);
                                binding.questionCount.setText(position+1 +"/"+listQuestion.size());
                            }catch (Exception e){
                                ((Button)view).setText(data);
                            }
                            view.setTag(data);
                            playAnimation(view,1,data);

                        }
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animation) {

                    }
                });
    }

    private void enableOption(boolean enable) {
        for(int i=0;i<4;i++){
            binding.optionContainer.getChildAt(i).setEnabled(enable);

            if(enable){
                binding.optionContainer.getChildAt(i).setBackgroundColor(0xFF6A73C3);
            }
        }

    }

    private void checkAnswer(Button selectedOption) {
        binding.btnNext.setEnabled(true);
        binding.btnNext.setAlpha(1);

        if(selectedOption.getText().toString().equals(listQuestion.get(position).getCorrectAnswer())){
            score++;
            selectedOption.setBackgroundColor(0xFF00FF00);
        }else{
            selectedOption.setBackgroundColor(0xFFFF0000);
            Button correctOption = (Button) binding.optionContainer.findViewWithTag(listQuestion.get(position).getCorrectAnswer());
            correctOption.setBackgroundColor(0xFF00FF00);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            showExitConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showExitConfirmationDialog();
    }
    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to quit?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Call super onBackPressed if the user confirms
            QuizActivity.super.onBackPressed();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Dismiss the dialog
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}