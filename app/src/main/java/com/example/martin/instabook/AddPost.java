package com.example.martin.instabook;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.martin.instabook.model.PostModel;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import static com.example.martin.instabook.Utilities.saveVideoToInternalStorage;

// Dialog Box na pridavanie prispevku ale pravdepodobne sa bude musiet menit kvoli Tomasovej implementacii
public class AddPost extends Dialog {

    private String username;
    private String userId;

    private MainActivity owner;

    private final String[] types = {"Photo", "Video"};
    Button btnPickImage;
    Button btnCaptureImage;
    Button btnPickVideo;
    Button btnCaptureVideo;


    public AddPost(@NonNull Context context, String username, String userId, MainActivity owner) {
        super(context);
        this.userId = userId;
        this.username = username;
        this.owner=owner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_post);

        btnPickImage = (Button) findViewById(R.id.btnPickImage);
        btnCaptureImage = (Button) findViewById(R.id.btnCaptureImage);
        btnPickVideo = (Button) findViewById(R.id.btnPickVideo);
        btnCaptureVideo = (Button) findViewById(R.id.btnCaptureVideo);
        //edtResp = (EditText) findViewById(R.id.edtResp);

        btnPickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                owner.onBtnPickImageClick(v);
                dismiss();
            }
        });

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                owner.onBtnCaptureImageClick(v);
                dismiss();
            }
        });

        btnPickVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                owner.onBtnPickVideoClick(v);
                dismiss();
            }
        });

        btnCaptureVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                owner.onBtnCaptureVideoClick(v);
                dismiss();
            }
        });

    }

    //TODO spravit ukladanie do db

    /*@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_post_okay:
                if(typeSpinner.getSelectedItem() != null){
                    PostModel preparePost = new PostModel("video.sk", "photo.sk", typeSpinner.getSelectedItem().toString(), userId, username);
                    FirebaseHelpers.addPostToDb(preparePost);
                    Toast.makeText(getContext(), "Successfuly created post", Toast.LENGTH_SHORT);
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "SELECT TYPE AND UPLOAD FILE TO THE SERVER!", Toast.LENGTH_LONG);
                }
                break;
            case R.id.btn_add_post_cancel:
                dismiss();
                break;
            case R.id.btn_add_post_file:
                break;
        }
    }*/
}
