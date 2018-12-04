package com.example.hp.socialnetwork;

import com.example.hp.socialnetwork.model.PostModel;
import com.example.hp.socialnetwork.model.UserModel;

import java.util.List;

// Interface na volanie z akehokolvek scopu
public interface FirebaseResults {

    void onUserResult(UserModel user);

    void onUserResultFailed();

    void onPostResultById(List<PostModel> posts);

    void onPostResultAll(List<PostModel> posts);
}
