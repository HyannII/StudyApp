package com.example.myapplication.Models;

import com.google.firebase.Timestamp;

import java.util.Comparator;

public class ResultModel {
    private String userId;
    private String userName;
    private int correct;
    private int wrong;
    private Timestamp startTime;
    private String timeLeft;

    public ResultModel() {
        // Constructor mặc định để sử dụng với Firebase Firestore
    }

    public ResultModel(String userId, String userName, int correct, int wrong, Timestamp startTime, String timeLeft) {
        this.userId = userId;
        this.userName = userName;
        this.correct = correct;
        this.wrong = wrong;
        this.startTime = startTime;
        this.timeLeft = timeLeft;
    }
    public static Comparator<ResultModel> SortByWrong = new Comparator<ResultModel>() {
        @Override
        public int compare(ResultModel r1, ResultModel r2) {
            return r1.getCorrect() - r2.getCorrect();
        }
    };
    public static Comparator<ResultModel> SortByCorrect  = new Comparator<ResultModel>() {
        @Override
        public int compare(ResultModel r1, ResultModel r2) {
            return r1.getWrong() - r2.getWrong();
        }
    };
    public static Comparator<ResultModel> SortByOldest = new Comparator<ResultModel>() {
        @Override
        public int compare(ResultModel r1, ResultModel r2) {
            return r1.getStartTime().compareTo(r2.getStartTime());
        }
    };
    public static Comparator<ResultModel> SortByNewest  = new Comparator<ResultModel>() {
        @Override
        public int compare(ResultModel r1, ResultModel r2) {
            return r2.getStartTime().compareTo(r1.getStartTime());
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }

}
