package com.example.hp.socialnetwork.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class PostModel implements Serializable {

    private HashMap<String, Object> date;
    private String imageUrl;
    private String videoUrl;
    private String type;
    private String userId;
    private String username;

    public PostModel() {

    }

    public PostModel(String imageUrl, String videoUrl, String type, String userId, String username) {
        this.username = username;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.type = type;
        HashMap<String, Object> newTimestamp = new HashMap<>();
        newTimestamp.put("date", ServerValue.TIMESTAMP);
        this.date = newTimestamp;
    }

    public HashMap<String, Object> getDate() {
        return date;
    }

//    public long getDate() {
//        return (long)date.get("date");
//    }

    @Exclude
    public String getRealDate() {
        long test_timestamp = getTimeStamp();
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(test_timestamp);
        return (cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-"
                + cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":"
                + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
    }

    @Exclude
    private long getTimeStamp() {
        return (long)date.get("date");
    }

    public void setDate(HashMap<String, Object> date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
