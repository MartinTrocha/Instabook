package com.example.martin.instabook;

import com.example.martin.instabook.model.PostModel;
import com.example.martin.instabook.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserView {

    private UserModel userModel;
    private List<PostModel> media;
    private String uID;

    public UserView() {
        this.userModel = new UserModel();
        this.media = new ArrayList<>();
    }

    public UserView(String uID){
        this.uID = uID;
        this.userModel = new UserModel();
        this.media = new ArrayList<>();
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public List<PostModel> getMedia() {
        return media;
    }

    public void setMedia(List<PostModel> media) {
        this.media = media;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }
}
