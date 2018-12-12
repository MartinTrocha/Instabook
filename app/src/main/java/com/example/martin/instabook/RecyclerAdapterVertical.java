package com.example.martin.instabook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martin.instabook.model.PostModel;
import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.Random;

public class RecyclerAdapterVertical extends RecyclerView.Adapter<RecyclerViewHolderVertical> {
    private UserView dataset;
    private LayoutInflater inflater;
    private Context context;

    public static final String URL_KEY = "com.example.martin.instabook.URLKEY";
    private final String userPlaceholderImageUrl = "http://oi63.tinypic.com/5w7k7.jpg";


    public RecyclerAdapterVertical(Context context, UserView dataset) {

        this.inflater = LayoutInflater.from(context);
        this.dataset = dataset; // horizontal hodnoty
        this.context=context;
    }

    public void onImageClick(PostModel post){

        // spusti aktivitu na prehranie videa
        if(post.getType().toLowerCase().equals("video")){
            Toast.makeText(this.context, String.format("play video: %s%s", MainActivity.DOWNLOAD_URL, post.getImageUrl()),Toast.LENGTH_SHORT).show();
            Intent startVideoIntent = new Intent(context.getApplicationContext(), VideoActivity.class);
            if (post.getVideoUrl() != null) {
                startVideoIntent.putExtra(URL_KEY, MainActivity.DOWNLOAD_URL + post.getVideoUrl());
                context.startActivity(startVideoIntent);
            }

        }
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


        final int index=i;

        SimpleDraweeView imgView = recyclerViewHolder.view.findViewById(R.id.imgView);



        TextView txtAuthor = recyclerViewHolder.view.findViewById(R.id.txtAuthor);
        TextView postedOn = recyclerViewHolder.view.findViewById(R.id.postedOnText);
        TextView txtDate = recyclerViewHolder.view.findViewById(R.id.txtDate);
        TextView txtNumPostsUser = recyclerViewHolder.view.findViewById(R.id.numberOfPostsUserPostedProfileId);
        TextView txtEmailContactHint = recyclerViewHolder.view.findViewById(R.id.emailTextContactHintId);
        TextView txtEmailContact = recyclerViewHolder.view.findViewById(R.id.emailTextContactId);

        if (i == 0) {
            // imgView.setVisibility(View.INVISIBLE);
            imgView.setImageURI(userPlaceholderImageUrl);
            txtEmailContactHint.setVisibility(View.VISIBLE);
            txtEmailContact.setVisibility(View.VISIBLE);
            try {
                txtAuthor.setText(this.dataset.getUserModel().getUsername());
                postedOn.setVisibility(View.INVISIBLE);
                txtDate.setText("This User registered on : " + this.dataset.getUserModel().dateToStr());
                txtNumPostsUser.setText("Number of Posts posted : " + this.dataset.getUserModel().getNumberOfPosts());
                txtEmailContactHint.setText("You can contact user directly via registed email");
                txtEmailContact.setText(this.dataset.getUserModel().getEmail());
                txtEmailContact.setPaintFlags(txtEmailContact.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                txtEmailContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {dataset.getUserModel().getEmail()});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Message from Instabook by User : " + FirebaseHelpers.getUser().getDisplayName());
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, try Instabook ;)");

                        context.startActivity(Intent.createChooser(emailIntent,"Send email..."));
                    }
                });
            }catch (Exception e){
                txtAuthor.setText("Loading...");
                txtNumPostsUser.setText("Loading...");
                txtDate.setText("profil");
            }
        } else {
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onImageClick(dataset.getMedia().get(index-1));
                }
            });
            imgView.setVisibility(View.VISIBLE);
            postedOn.setVisibility(View.VISIBLE);
            txtEmailContact.setVisibility(View.INVISIBLE);
            txtEmailContactHint.setVisibility(View.INVISIBLE);
            try {

                txtAuthor.setText(this.dataset.getUserModel().getUsername());
                txtDate.setText(this.dataset.getMedia().get(i-1).dateToStr());
                txtNumPostsUser.setText("");
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
