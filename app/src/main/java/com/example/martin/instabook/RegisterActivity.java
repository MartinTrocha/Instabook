package com.example.martin.instabook;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.martin.instabook.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "RegisterActivity";
    private final String USERS_TABLE = "users";

    private EditText inputEmail, inputPassword, inputUsername;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference mUsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /*
        * Firebase autentifikácia
        * */

        auth = FirebaseAuth.getInstance(); // Firebase instancia
        mUsersRef = FirebaseDatabase.getInstance().getReference(USERS_TABLE); // Firabase referencia na tabulku USERS_TABLE

        /*
        * Buttons
        **/
        btnSignIn = findViewById(R.id.sign_in_button);
        btnSignUp = findViewById(R.id.sign_up_button);

        /*
         * Inputs
         **/
        inputUsername = findViewById(R.id.usernameEditText);
        inputEmail = findViewById(R.id.emailEditTextR);
        inputPassword = findViewById(R.id.passwordEditTextR);
        btnResetPassword = findViewById(R.id.btn_reset_password);

        /*
         * Other elements
         **/
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        /*
        *   Reset hesla
        **/
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, ResetPasswordActivity.class));
            }
        });

        /*
         *   Registracia už bola, späť na login (Already registered. Login Me!)
         **/
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*
         *   Registracia noveho pouzivatela
         **/
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = inputUsername.getText().toString().trim();
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();

                // TODO strings texty
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

               createUser(username, email, password);
            }
        });
    }


    private void createUser(final String username, final String email, final String password) {
        progressBar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                } else {
                    if(auth.getCurrentUser() != null){
                        addUsernameToUsers(auth.getCurrentUser(), username);
                    }
                }
            }
        });
    }

    private void addUsernameToUsers(final FirebaseUser user, final String username) {

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
        user.updateProfile(profileUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                addToDB(user, username);
            }
        });
    }

    private void addToDB(final FirebaseUser user, final String username){
        UserModel userModel = new UserModel(username, 0, user.getEmail());

        mUsersRef.child(user.getUid()).setValue(userModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this,"Registration successful" ,Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();

                } else {
                    FirebaseUser newUser = auth.getCurrentUser();
                    if(newUser != null){
                        newUser.delete();
                    }

                    Toast.makeText(RegisterActivity.this,"User Created but not inserted to the Database!",Toast.LENGTH_LONG).show();
                    Log.w(TAG, databaseError.getMessage());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

}
