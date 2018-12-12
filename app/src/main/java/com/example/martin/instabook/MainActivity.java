package com.example.martin.instabook;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.martin.instabook.model.PostModel;
import com.example.martin.instabook.model.UserModel;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.martin.instabook.Utilities.saveVideoToInternalStorage;


public class MainActivity extends AppCompatActivity  {

    private DrawerLayout mDrawerLayout;

    private TextView userName;
    private TextView emailName;
    private TextView dateOfRegistration;
    private TextView numberOfPosts;

    private FirebaseDatabase mDatabaseRef;
    private FirebaseAuth auth;
    private UserModel userLoggedIn;

    private AddPost postDialogBox;
    private String usernameText;
    private String userIdtext;
    private Boolean isLoggedIn = null;
    private Boolean isMinimized = null;

    public static String UPLOAD_URL = "http://mobv.mcomputing.eu/upload/index.php";
    public static String DOWNLOAD_URL = "http://mobv.mcomputing.eu/upload/v/";
    public static String PHOTO_TYPE = "photo";
    public static String VIDEO_TYPE = "video";

    public static final String USER_ID_UPLOADER_KEY = "com.example.martin.instabook.USERID_UPLOAD_KEY";
    public static final String USERNAME_UPLOADER_KEY = "com.example.martin.instabook.USERNAME_UPLOAD_KEY";

    private final String TAG = "MainActivityLOG";

    public static final int REQUEST_PICK_IMAGE = 1;
    public static final int REQUEST_CAPTURE_IMAGE = 2;
    public static final int REQUEST_PICK_VIDEO = 3;
    public static final int REQUEST_CAPTURE_VIDEO = 4;



    private List<UserModel> userModels;
    private List<PostModel> content_dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Handle navigation view item clicks here.
                int id = menuItem.getItemId();

                if (id == R.id.nav_camera) {
                    // Handle the camera action
                } else if (id == R.id.nav_gallery) {
                    startActivity(new Intent(MainActivity.this, UploadActivity.class));
                } else if (id == R.id.nav_slideshow) {

                } else if (id == R.id.nav_manage) {

                } else if (id == R.id.nav_logout) {

                    auth.signOut();

                    if(isMinimized == null) {
                        auth.signOut();
                        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                            @Override
                            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user == null) {
                                    finish();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                }
                            }
                        });
                    }
                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        View headerHeader = navigationView.getHeaderView(0);
        userName = headerHeader.findViewById(R.id.nickname_header);
        emailName = headerHeader.findViewById(R.id.email_header);
        dateOfRegistration = headerHeader.findViewById(R.id.registration_header);
        numberOfPosts = headerHeader.findViewById(R.id.num_posts_header);




        this._init();

        FloatingActionButton fab = findViewById(R.id.btnAddPost);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), UploadActivity.class);
                // TODO: Mozno to este zmenim ze nech to taha z DB a nie z AUTH
                myIntent.putExtra(USER_ID_UPLOADER_KEY, userIdtext);
                myIntent.putExtra(USERNAME_UPLOADER_KEY, usernameText);
                startActivity(myIntent);
            }
        });
    }

    private void _init(){

        auth = FirebaseAuth.getInstance(); // firebase referencia
        mDatabaseRef = FirebaseDatabase.getInstance(); // firebase databaza referencia

        FirebaseHelpers.getAllPostsFromDbOrderedByRecency(new FirebaseResultsImpl(){
            @Override
            public void onPostResultAll(List<PostModel> posts) {
                super.onPostResultAll(posts);
                FragmentPlaceholder fragmentPlaceholderHorizontal = new FragmentPlaceholder();
                fragmentPlaceholderHorizontal.setTest(posts);

                // Trebalo dat sem Replace lebo potom sa tam davali viacere fragmenty furt ked sa vratilo
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentPlaceholderHorizontal).commit();
                // getSupportFragmentManager().beginTransaction().add(R.id.container, fragmentPlaceholderHorizontal).commitAllowingStateLoss();
                navigationDrawerInit();
            }
        });
    }

    private void navigationDrawerInit(){

        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser == null){
            return;
        }

        FirebaseHelpers.getCurrentUserDataFromDb(currentUser.getUid(), new FirebaseResultsImpl() {
            @Override
            public void onUserResult(UserModel user) {
                userName.setText(user.getUsername());
                emailName.setText(user.getEmail());
                numberOfPosts.setText("Number of Posts posted: " + user.getNumberOfPosts().toString());
                dateOfRegistration.setText("Date of registration: " + user.dateToStr());
                usernameText = user.getUsername();
                userIdtext = user.getDbKey();
                isLoggedIn = true;
            }

            @Override
            public void onUserResultFailed() {
                Toast.makeText(MainActivity.this,"Get user data from DB failed", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            // minimizeApp();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DESTROYED MAIN ACTIVITY!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "PAUSED MAIN ACTIVITY!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "STOPPED MAIN ACTIVITY");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"RESUMED MAIN ACTIVITY");
    }
}
