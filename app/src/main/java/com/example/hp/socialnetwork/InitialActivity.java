package com.example.hp.socialnetwork;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

// Tato activita len rozhodne ci sa treba prihlasit/registrovat alebo vojst rovno do appky
public class InitialActivity extends AppCompatActivity {

    private final String TAG = "Initial Activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseHelpers.getUserId() == null) {
            startActivity(new Intent(this,LoginActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "INITIAL ACTIVITY DESTROYED");
    }
}
