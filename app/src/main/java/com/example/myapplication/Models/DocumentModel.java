package com.example.myapplication.Models;

public class DocumentModel {
    private String Title;

    private String TitleContent;
    private String DocumentUri;
    int Image;

    public DocumentModel(String title, String titleContent,String documentUri, int image) {
        this.Title = title;
        this.TitleContent = titleContent;
        this.DocumentUri = documentUri;
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

    public String getDocumentUri() {
        return DocumentUri;
    }

    public void setDocumentUri(String documentUri) {
        DocumentUri = documentUri;
    }
    public void setTitleContent(String titleContent) {
        TitleContent = titleContent;
    }
}