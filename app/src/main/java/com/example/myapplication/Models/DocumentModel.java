package com.example.myapplication.Models;

public class DocumentModel {
    String Title;

    String TitleContent;
    int Image;


    public DocumentModel(String title, String titleContent, int image) {
        this.Title = title;
        this.TitleContent = titleContent;
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

    public String getTitleContent() {
        return TitleContent;
    }

    public void setTitleContent(String titleContent) {
        TitleContent = titleContent;
    }
}