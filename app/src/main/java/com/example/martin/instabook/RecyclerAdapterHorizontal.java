package com.example.martin.instabook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.martin.instabook.model.PostModel;
import com.example.martin.instabook.model.UserModel;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterHorizontal extends RecyclerView.Adapter<RecyclerViewHolderHorizontal> {
    private List<PostModel> dataset;
    private UserView nextDataset;
    private LayoutInflater inflater;
    public int current_vertical_position;


    public RecyclerAdapterHorizontal(Context context, List<PostModel> dataset) {
        this.inflater = LayoutInflater.from(context);
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public RecyclerViewHolderHorizontal onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = this.inflater.inflate(R.layout.horizontal_recycler_item, parent, false);
        return new RecyclerViewHolderHorizontal(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolderHorizontal recyclerViewHolder, int i) {

        final int index = i;
        final RecyclerViewHolderHorizontal holder = recyclerViewHolder;

        FirebaseHelpers.getCurrentUserDataFromDb(dataset.get(index).getUserId(), new FirebaseResultsImpl() {
            @Override
            public void onUserResult(UserModel user) {
                super.onUserResult(user);

                final UserView userView = new UserView(user.getDbKey());

                userView.setUserModel(user);
                nextDataset=userView;

                RecyclerView recyclerView = holder.view.findViewById(R.id.vertical_recycler);
                recyclerView.setHasFixedSize(true);

                final RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(inflater.getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(recyclerViewLayoutManager);
                recyclerViewLayoutManager.scrollToPosition(1); // nastavuje defaultne na 1 poziciu , 0 je profil

                PagerSnapHelper snapHelper = new PagerSnapHelper();
                if (recyclerView.getOnFlingListener() == null) {
                    snapHelper.attachToRecyclerView(recyclerView);
                }

                final RecyclerView.Adapter recyclerViewAdapter = new RecyclerAdapterVertical(inflater.getContext(), nextDataset);
                recyclerView.setAdapter(recyclerViewAdapter);

                FirebaseHelpers.getPostsByUserId(user.getDbKey(), new FirebaseResultsImpl(){

                    public void onPostResultById(List<PostModel> posts) {
                        super.onPostResultById(posts);

                        PostModel thisPost=dataset.get(index);
                        posts.add(0, thisPost);

                        userView.setMedia(posts);
                        recyclerViewLayoutManager.scrollToPosition(1);
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

//        FirebaseHelpers.getPostsByUserId(this.dataset.get(i).getUserId(), new FirebaseResultsImpl(){
//
//            UserView userView = new UserView(dataset.get(index).getUserId());
//
//            @Override
//            public void onPostResultById(List<PostModel> posts) {
//                super.onPostResultById(posts);
//
//                userView.setMedia(posts);
//                FirebaseHelpers.getCurrentUserDataFromDb(dataset.get(index).getUserId(), new FirebaseResultsImpl(){
//                    @Override
//                    public void onUserResult(UserModel user) {
//                        super.onUserResult(user);
//                        userView.setUserModel(user);
//                        nextDataset.add(userView);
//                    }
//                });
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return this.dataset.size();
    }

    /*public void setUserProfile(UserModel user){
        UserView userView = new UserView();
        userView.setUserModel(user);
    }*/
}
