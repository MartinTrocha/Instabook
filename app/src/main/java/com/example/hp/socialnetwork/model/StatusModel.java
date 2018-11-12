package com.example.hp.socialnetwork.model;

import java.io.Serializable;

public class StatusModel implements Serializable {

    private String text;
    private ProfileModel author;


    public StatusModel(String text) {
        this.text = text;
    }

    public StatusModel(ProfileModel author, String text) {
        this.author = author;
        this.text = text;
    }

    public String getText() {

        return text;
    }

    public ProfileModel getAuthor() {
        return author;
    }

    public void setAuthor(ProfileModel author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }
}
