package com.example.myapplication.Models;

public class CourseModel {
    String Title;
    int Image;

    public CourseModel(String title, int image) {
        this.Title = title;
        this.Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}