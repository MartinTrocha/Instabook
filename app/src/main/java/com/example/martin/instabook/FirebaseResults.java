package com.example.martin.instabook;



import com.example.martin.instabook.model.PostModel;
import com.example.martin.instabook.model.UserModel;

import java.util.List;

// Interface na volanie z akehokolvek scopu
public interface FirebaseResults {

    void onUserResult(UserModel user);

    void onUserResultFailed();

    void onPostResultById(List<PostModel> posts);

    void onPostResultAll(List<PostModel> posts);
}
