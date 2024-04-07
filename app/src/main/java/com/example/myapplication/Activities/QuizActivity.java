package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Models.QuestionModel;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityQuizBinding;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    ArrayList<QuestionModel> listQuestion = new ArrayList<>();
    private int count = 0;
    private int position = 0;
    private int score = 0;
    CountDownTimer timer;
    ActivityQuizBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        getSupportActionBar().hide();

        String [] questionAndAnswers = getResources().getStringArray(R.array.question);

        for (int i = 0;i<questionAndAnswers.length;i+=6) {
            listQuestion.add(new QuestionModel(questionAndAnswers[i], questionAndAnswers[i+1],
                    questionAndAnswers[i+2],questionAndAnswers[i+3],questionAndAnswers[i+4],questionAndAnswers[i+5]));
        }

        for (int i=0;i<4;i++){
            binding.optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkAnswer((Button) view);
                }
            });
        }

        playAnimation(binding.question, 0, listQuestion.get(position).getQuestion());
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.btnNext.setEnabled(false);
                binding.btnNext.setAlpha((float) 0.3);
                enableOption(true);
                position++;

                if(position == listQuestion.size()){
                    Intent intent = new Intent(QuizActivity.this,ScoreActivity.class);
                    intent.putExtra("score",score);
                    intent.putExtra("total",listQuestion.size());
                    startActivity(intent);
                    finish();
                    return;
                }

                count = 0;

                playAnimation(binding.question,0,listQuestion.get(position).getQuestion());
            }
        });
    }

    private void playAnimation(View view, int value, String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
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
                                binding.questionCount.setText(position+1+"/"+listQuestion.size());
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
                binding.optionContainer.getChildAt(i).setBackgroundColor(0xFF0000FF);
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
}