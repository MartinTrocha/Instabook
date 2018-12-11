package com.example.martin.instabook;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import static com.example.martin.instabook.Utilities.convertStreamToString;
import static com.example.martin.instabook.Utilities.saveVideoToInternalStorage;


public class UploadActivity extends Activity {

    Button btnPickImage;
    Button btnCaptureImage;
    Button btnPickVideo;
    Button btnCaptureVideo;

    public static final int REQUEST_PICK_IMAGE = 1;
    public static final int REQUEST_CAPTURE_IMAGE = 2;
    public static final int REQUEST_PICK_VIDEO = 3;
    public static final int REQUEST_CAPTURE_VIDEO = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(android.R.style.Theme_Dialog);
        setContentView(R.layout.upload_main);

        btnPickImage = (Button) findViewById(R.id.btnPickImage);
        btnCaptureImage = (Button) findViewById(R.id.btnCaptureImage);
        btnPickVideo = (Button) findViewById(R.id.btnPickVideo);
        btnCaptureVideo = (Button) findViewById(R.id.btnCaptureVideo);

        btnPickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBtnPickImageClick(v);
            }
        });

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBtnCaptureImageClick(v);
            }
        });

        btnPickVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBtnPickVideoClick(v);
            }
        });

        btnCaptureVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBtnCaptureVideoClick(v);
            }
        });
    }

    private void requestPermisions(int request_identifier) {
        //vyziadanie prav
        if (ActivityCompat.checkSelfPermission(UploadActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(UploadActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request_identifier);

        }

        if (ActivityCompat.checkSelfPermission(UploadActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(UploadActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, request_identifier);

        }

        //ak uz su priradene prava
        if (ActivityCompat.checkSelfPermission(UploadActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UploadActivity.this,
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


    private void onBtnCaptureVideoClick(View v){
        requestPermisions(REQUEST_CAPTURE_VIDEO);
    }
    
    private void onBtnPickVideoClick(View v) {
        requestPermisions(REQUEST_PICK_VIDEO);
    }

    private void onBtnPickImageClick(View v) {
        requestPermisions(REQUEST_PICK_IMAGE);
    }

    private void onBtnCaptureImageClick(View v) {
        requestPermisions(REQUEST_CAPTURE_IMAGE);
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

                new SendHttpRequestTask().execute(picturePath);
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

                    new SendHttpRequestTask().execute(selectedVideoPath);
                }
            } catch (Exception e) {

            }
        }

        if (requestCode == REQUEST_CAPTURE_VIDEO) {

            try {

                String filepath=getVideoPath(data.getData());
                new SendHttpRequestTask().execute(filepath);

            } catch (Exception e) {

            }
        }

        if (requestCode == REQUEST_CAPTURE_IMAGE) {

            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                Uri tempUri = getImageUri(getApplicationContext(), photo);

                File finalFile = new File(getRealPathFromURI(tempUri));

                new SendHttpRequestTask().execute(finalFile.getAbsolutePath());
            } catch (Exception e) {
                Toast.makeText(UploadActivity.this,
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

                    Toast.makeText(UploadActivity.this,
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