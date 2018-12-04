package com.example.hp.socialnetwork.model;


import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import static java.text.DateFormat.getDateInstance;

public class UserModel implements Serializable{

    private String username;
    private Integer numberOfPosts;
    private String email;
    private HashMap<String, Object> date;
    private transient String dbKey;

    public UserModel(String username, Integer numberOfPosts, String email) {
        this.username = username;
        this.numberOfPosts = numberOfPosts;
        this.email = email;
        HashMap<String, Object> newTimestamp = new HashMap<>();
        newTimestamp.put("date", ServerValue.TIMESTAMP);
        this.date = newTimestamp;

    }

    public UserModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getNumberOfPosts() {
        return numberOfPosts;
    }

    public void setNumberOfPosts(Integer numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getDate() {
        long test_timestamp = getTimeStamp();
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(test_timestamp);
        return (cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-"
                + cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":"
                + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
    }

    public void setDate(HashMap<String, Object> date) {
        this.date = date;
    }

    @Exclude
    private long getTimeStamp() {
        return (long)date.get("date");
    }

    @Exclude
    public String getDbKey() {
        return this.dbKey;
    }

    @Exclude
    public void setDbKey(String key) {
        this.dbKey = key;
    }


}
