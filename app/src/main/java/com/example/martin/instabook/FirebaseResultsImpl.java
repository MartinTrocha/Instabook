package com.example.martin.instabook;

import com.example.martin.instabook.model.PostModel;
import com.example.martin.instabook.model.UserModel;

import java.util.List;

public class FirebaseResultsImpl implements FirebaseResults {

    public List<PostModel> postModels;
    @Override
    public void onUserResult(UserModel user) {
    }

    @Override
    public void onUserResultFailed() {
    }

    @Override
    public void onPostResultById(List<PostModel> posts) {
    }

    @Override
    public void onPostResultAll(List<PostModel> posts) {
        this.postModels = posts;

    }
}
