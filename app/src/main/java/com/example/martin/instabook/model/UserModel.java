package com.example.martin.instabook.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.lang.Long;



public class UserModel implements Serializable{

    private String username;
    private Integer numberOfPosts;
    private String email;
    private Long date;
    private transient String dbKey;

    public UserModel() {
    }

    public UserModel(String username, Integer numberOfPosts, String email) {
        this.username = username;
        this.numberOfPosts = numberOfPosts;
        this.email = email;

    }

    public String getUsername() {
        return username;
    }

    public Integer getNumberOfPosts() {
        return numberOfPosts;
    }

    public String getEmail() {
        return email;
    }

    public java.util.Map<String, String> getDate(){
        return ServerValue.TIMESTAMP;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setNumberOfPosts(Integer numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String dateToStr() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(this.date);
        String formatedDate = format.format(cal.getTime());
        return formatedDate;
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
