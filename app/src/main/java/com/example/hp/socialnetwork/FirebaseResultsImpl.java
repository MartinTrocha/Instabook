package com.example.hp.socialnetwork;

import com.example.hp.socialnetwork.model.PostModel;
import com.example.hp.socialnetwork.model.UserModel;

import java.util.List;

// Implementacia interfacu na to aby ked zavolame metody interfacu tak sme ich mohli pretazit
public class FirebaseResultsImpl implements FirebaseResults {
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

    }


}
