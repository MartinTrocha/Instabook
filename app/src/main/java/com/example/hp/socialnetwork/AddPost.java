package com.example.hp.socialnetwork;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hp.socialnetwork.model.PostModel;

// Dialog Box na pridavanie prispevku ale pravdepodobne sa bude musiet menit kvoli Tomasovej implementacii
public class AddPost extends Dialog implements View.OnClickListener {

    private String username;
    private String userId;

    private final String[] types = {"Photo", "Video"};
    private Button okayBtn;
    private Button cancelBtn;
    private Button uploadBtn;
    private Spinner typeSpinner;

    public AddPost(@NonNull Context context, String username, String userId) {
        super(context);
        this.userId = userId;
        this.username = username;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_post);
        okayBtn = (Button) findViewById(R.id.btn_add_post_okay);
        cancelBtn = (Button) findViewById(R.id.btn_add_post_cancel);
        uploadBtn = (Button) findViewById(R.id.btn_add_post_file);
        typeSpinner = (Spinner) findViewById(R.id.spinner_choose_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, types);
        typeSpinner.setAdapter(adapter);
        okayBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_post_okay:
                if(typeSpinner.getSelectedItem() != null){
                    PostModel preparePost = new PostModel("video.sk",
                            "photo.sk",
                            typeSpinner.getSelectedItem().toString(),
                            userId,
                            username);
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
    }
}
