package com.example.myapplication.Models;

import java.util.Comparator;

public class ResultModel {
    private String examinee, startTime, timeLeft;
    private int correct, wrong,id;
    public ResultModel(int id,String examinee, String startTime, int correct, int wrong, String timeLeft) {
        this.id = id;
        this.examinee = examinee;
        this.startTime = startTime;
        this.timeLeft = timeLeft;
        this.correct = correct;
        this.wrong = wrong;
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
            return r1.getId() - r2.getId();
        }
    };
    public static Comparator<ResultModel> SortByNewest  = new Comparator<ResultModel>() {
        @Override
        public int compare(ResultModel r1, ResultModel r2) {
            return r2.getId() - r1.getId();
        }
    };

    public String getExaminee() {
        return examinee;
    }

    public void setExaminee(String examinee) {
        this.examinee = examinee;
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

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
