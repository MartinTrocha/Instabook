package com.example.martin.instabook.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

public class PostModel implements Serializable {


    private String imageUrl;
    private String videoUrl;
    private String type;
    private String userId;
    private String username;
    private Long date;

    public PostModel() {

    }

    public PostModel(String imageUrl, String videoUrl, String type, String userId, String username) {
        this.username = username;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getType() {
        return type;
    }

    public java.util.Map<String, String> getDate(){
        return ServerValue.TIMESTAMP;
    }

    public long getDateAsLong(){
        return this.date;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String dateToStr() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(this.date);
        return (cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-"
                + cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":"
                + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
    }
}
