package com.example.foodblogs;

import android.graphics.Bitmap;

import java.sql.Blob;

public class ModelClass {
    private String email;
    private String title;
    private String cusine;
    private String course;
    private Bitmap image;
    private Bitmap video;
    private String description;
    private String recipie;



    // Bitmap video ,
    public ModelClass(String email, String title, String cusine, String course,Bitmap image, String description, String recipie) {
        this.email = email;
        this.title = title;
        this.cusine = cusine;
        this.course = course;
        this.image = image;
        this.video = video;
        this.description = description;
        this.recipie = recipie;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCusine() {
        return cusine;
    }

    public void setCusine(String cusine) {
        this.cusine = cusine;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getVideo() {
        return video;
    }

    public void setVideo(Bitmap video) {
        this.video = video;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecipie() {
        return recipie;
    }

    public void setRecipie(String recipie) {
        this.recipie = recipie;
    }
}
