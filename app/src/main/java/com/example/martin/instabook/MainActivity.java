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
    public static String PHOTO_TYPE = "Photo";

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


        FloatingActionButton fab = findViewById(R.id.btnAddPost);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), UploadActivity.class);

                startActivity(myIntent);
                //TODO alebo:
//                postDialogBox = new AddPost(MainActivity.this,usernameText,userIdtext, MainActivity.this);
//                postDialogBox.show();
            }
        });

        this._init();
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

                getSupportFragmentManager().beginTransaction().add(R.id.container, fragmentPlaceholderHorizontal).commit();
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

        /*FloatingActionButton fab = findViewById(R.id.fab_add_post);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDialogBox = new AddPost(MainActivity.this,usernameText,userIdtext);
                postDialogBox.show();
            }
        });*/
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
        //Log.d(TAG_INFO, "DESTROYED!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.d(TAG_INFO, "PAUSED!!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Log.d(TAG_INFO, "STOPPED!!");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }





    // TODO zmazat ak sa rozhodneme pre activito-dialog

    private void requestPermisions(int request_identifier) {
        //vyziadanie prav
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request_identifier);

        }

        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, request_identifier);

        }

        //ak uz su priradene prava
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            switch (request_identifier) {
                case REQUEST_CAPTURE_IMAGE: {
                    runCaptureImage();
                    break;
                }
                case REQUEST_PICK_IMAGE: {
                    runPickImage();
                    break;
                }
                case REQUEST_PICK_VIDEO: {
                    runPickVideo();
                    break;
                }
                case REQUEST_CAPTURE_VIDEO: {
                    runCaptureVideo();
                    break;
                }
            }
        }
    }

    //netody na pick/capture obrazkov/videi
    private void runCaptureImage() {

        Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(m_intent, REQUEST_CAPTURE_IMAGE);
    }

    private void runCaptureVideo() {

        Intent captureVideoIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(captureVideoIntent, REQUEST_CAPTURE_VIDEO);

    }

    private void runPickVideo() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, REQUEST_PICK_VIDEO);

    }

    private void runPickImage() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, REQUEST_PICK_IMAGE);
    }

    public void onBtnCaptureVideoClick(View v) {
        requestPermisions(REQUEST_CAPTURE_VIDEO);
    }

    public void onBtnPickVideoClick(View v) {
        requestPermisions(REQUEST_PICK_VIDEO);
    }

    public void onBtnPickImageClick(View v) {
        requestPermisions(REQUEST_PICK_IMAGE);
    }

    public void onBtnCaptureImageClick(View v) {
        requestPermisions(REQUEST_CAPTURE_IMAGE);
    }


    //reakcia na usera ked potvrdi/zamietne prava na citanie/zapis
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        //podla requestCode-u vykona funkciu
        switch (requestCode) {
            case REQUEST_CAPTURE_IMAGE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    runCaptureImage();
                }
                break;
            }
            case REQUEST_PICK_IMAGE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    runPickImage();
                }
                break;
            }
            case REQUEST_PICK_VIDEO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    runPickVideo();
                }
                break;
            }

            case REQUEST_CAPTURE_VIDEO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    runCaptureVideo();
                }
                break;
            }
        }
    }

    //vysledok aktivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //podla requestCode-u vykona pozadovanu funkciu
        if (requestCode == REQUEST_PICK_IMAGE) {

            try {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                new MainActivity.SendHttpRequestTask().execute(picturePath);
            } catch (NullPointerException e) {
            }
        }

        if (requestCode == REQUEST_PICK_VIDEO) {

            try {

                Uri contentURI = data.getData();

                String selectedVideoPath = getVideoPath(contentURI);
                Log.d("path", selectedVideoPath);
                saveVideoToInternalStorage(selectedVideoPath);
                if (selectedVideoPath != null) {

                    new MainActivity.SendHttpRequestTask().execute(selectedVideoPath);
                }
            } catch (Exception e) {

            }
        }

        if (requestCode == REQUEST_CAPTURE_VIDEO) {

            try {

                String filepath=getVideoPath(data.getData());
                new MainActivity.SendHttpRequestTask().execute(filepath);

            } catch (Exception e) {

            }
        }

        if (requestCode == REQUEST_CAPTURE_IMAGE) {

            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                Uri tempUri = getImageUri(this, photo);

                File finalFile = new File(getRealPathFromURI(tempUri));

                new MainActivity.SendHttpRequestTask().execute(finalFile.getAbsolutePath());
            } catch (Exception e) {
                Toast.makeText(MainActivity.this,
                        e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    //metoda na upload
    private String uploadMedia(String filename) {

        String jsonReply="";

        try {

            String charset = "UTF-8";
            File uploadFile1 = new File(filename);
            String requestURL = MainActivity.UPLOAD_URL;

            MultipartUtility multipart = new MultipartUtility(requestURL, charset);


            multipart.addFilePart("upfile", uploadFile1);

            List<String> response = multipart.finish();

            Log.v("rht", "SERVER REPLIED:");

            for (String line : response) {
                jsonReply+=line;
            }

            runOnUiThread(new Runnable() {
                public void run() {

                    Toast.makeText(MainActivity.this,
                            "File Upload Complete.", Toast.LENGTH_SHORT)
                            .show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonReply;
    }

    //pomocne metody na ziskavanie ciest k suborom
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = null;
        try {
            path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);

        } catch (Exception e) {

            System.out.println();
        }
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public String getVideoPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    //async task pre odoslanie a ziskanie responusu zo servera
    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String name = params[0];

            String res = uploadMedia(name);

            return res;
        }

        //response callback
        @Override
        protected void onPostExecute(String result) {
//            edtResp.setText(result);

            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(result);
                System.out.println(jsonObj.get("message"));

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }

    }
}
