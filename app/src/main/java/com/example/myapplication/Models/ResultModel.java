package com.example.myapplication.Models;

public class ResultModel {
    String examinee, startTime, timeLeft;
    int correct, wrong,id;
    public ResultModel(int id,String examinee, String startTime, int correct, int wrong, String timeLeft) {
        this.id = id;
        this.examinee = examinee;
        this.startTime = startTime;
        this.timeLeft = timeLeft;
        this.correct = correct;
        this.wrong = wrong;
    }

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
