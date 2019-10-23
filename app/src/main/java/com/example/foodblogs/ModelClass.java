package com.example.foodblogs;

import android.graphics.Bitmap;
import android.net.Uri;

import java.sql.Blob;

public class ModelClass {
    private String email;
    private String title;
    private String cusine;
    private String course;
    private String image;
    private String video;
    private String description;
    private String recipie;
    private boolean isImageSaved = false;

    // Bitmap video ,
    public ModelClass(String email, String title, String cusine, String course, String image, String description, String recipie) {
        this.email = email;
        this.title = title;
        this.cusine = cusine;
        this.course = course;
        this.image = image;
        this.video = video;
        this.description = description;
        this.recipie = recipie;
    }

    public ModelClass(String email, String title, String cusine, String course, String image, String description, String recipie, boolean isImageSaved) {
        this.email = email;
        this.title = title;
        this.cusine = cusine;
        this.course = course;
        this.image = image;
        this.video = video;
        this.description = description;
        this.recipie = recipie;
        this.isImageSaved = isImageSaved;
    }

    public void setImageSaved(boolean imageSaved) {
        isImageSaved = imageSaved;
    }

    public boolean isImageSaved() {
        return isImageSaved;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
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
