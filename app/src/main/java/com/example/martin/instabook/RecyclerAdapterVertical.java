package com.example.martin.instabook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Random;

public class RecyclerAdapterVertical extends RecyclerView.Adapter<RecyclerViewHolderVertical> {
    private UserView dataset;
    private LayoutInflater inflater;
    //private Context context;


    public RecyclerAdapterVertical(Context context, UserView dataset) {

        this.inflater = LayoutInflater.from(context);
        this.dataset = dataset; // horizontal hodnoty
        //this.context=context;
    }


    @NonNull
    @Override
    public RecyclerViewHolderVertical onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = this.inflater.inflate(R.layout.vertical_recycler_item, parent, false);

        return new RecyclerViewHolderVertical(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolderVertical recyclerViewHolder, int i) {

        // TODO : obcas bugne profil a zobrazi obrazok

        SimpleDraweeView imgView = recyclerViewHolder.view.findViewById(R.id.imgView);

        TextView txtAuthor = recyclerViewHolder.view.findViewById(R.id.txtAuthor);
        TextView txtDate = recyclerViewHolder.view.findViewById(R.id.txtDate);

        if (i == 0) {
            imgView.setEnabled(false);
            try {
                txtAuthor.setText(this.dataset.getUserModel().getUsername());
                txtDate.setText("profil");
            }catch (Exception e){
                txtAuthor.setText("Loading...");
                txtDate.setText("profil");
            }
        } else {

            imgView.setEnabled(true);
            try {

                txtAuthor.setText(this.dataset.getUserModel().getUsername());
                txtDate.setText(this.dataset.getMedia().get(i-1).dateToStr());

                if (this.dataset.getMedia().get(i-1).getType().equals(MainActivity.PHOTO_TYPE)) {
                    imgView.setImageURI(MainActivity.DOWNLOAD_URL + this.dataset.getMedia().get(i-1).getImageUrl());
                }

            } catch (Exception e) {


                txtAuthor.setText("Loading...");
                txtDate.setText("Loading...");
            }
        }

        /*synchronized (this){
            this.notifyAll();
        }*/
    }

    @Override
    public int getItemCount() {
        try {
            return this.dataset.getMedia().size() + 1;
        } catch (Exception e) {
            return 2;
        }

    }
}
