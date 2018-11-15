package com.example.hp.socialnetwork;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.hp.socialnetwork.model.ProfileModel;
import com.example.hp.socialnetwork.model.StatusModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String PARENT_BUNDLE="socialnetwork.parent";
    public static String CHILD_BUNDLE="socialnetwork.child";
    public static String TAG_INFO = "socialnetwork.logout";

    public HorizontalViewPagerAdapter viewPagerAdapter;
    public HorizontalViewPager viewPager;
    private FirebaseAuth auth;
    private TextView userName;
    private TextView emailName;
    private String usernameText;
    private String emailText;
    private Boolean isLoggedIn = null;
    private Boolean isMinimized = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerMainPanel = navigationView.getHeaderView(0);
        Intent intentThatStartedThisActivity = getIntent();

        userName = (TextView) headerMainPanel.findViewById(R.id.loggedInName);
        emailName = (TextView) headerMainPanel.findViewById(R.id.loggedInEmail);

        // toto treba na logout
        auth = FirebaseAuth.getInstance();
        // ak to zapinam prvy krat
        if (auth.getCurrentUser() == null) {
            isLoggedIn = false;
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            Log.w(TAG_INFO,"LoginActivity called when UserNotLoggedIn");

        } else {
            usernameText = auth.getCurrentUser().getDisplayName();
            emailText = auth.getCurrentUser().getEmail();
            userName.setText(usernameText);
            emailName.setText(emailText);
            isLoggedIn = true;
        }

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(RegisterActivity.USERNAME_KEY_R) && intentThatStartedThisActivity.hasExtra(RegisterActivity.EMAIL_KEY_R)) {
                usernameText = intentThatStartedThisActivity.getStringExtra(RegisterActivity.USERNAME_KEY_R);
                emailText = intentThatStartedThisActivity.getStringExtra(RegisterActivity.EMAIL_KEY_R);
                userName.setText(usernameText);
                emailName.setText(emailText);
            } else if (intentThatStartedThisActivity.hasExtra(LoginActivity.EMAIL_KEY)) {
                usernameText = intentThatStartedThisActivity.getStringExtra(LoginActivity.USERNAME_KEY);
                emailText = intentThatStartedThisActivity.getStringExtra(LoginActivity.EMAIL_KEY);
                //auth.getInstance().getCurrentUser().getDisplayName();
                userName.setText(usernameText);
                emailName.setText(emailText);
            }
        }
        initViewPager();
    }

    private List<StatusModel> _testData() {

        ProfileModel p1 = new ProfileModel("Tomas", "Rences", "popis o tomasovi");
        ProfileModel p2 = new ProfileModel("Martin", "Trocha", "popis o martinovy");
        ProfileModel p3 = new ProfileModel("Marek", "Ondrasik", "popis o marekovi");

        StatusModel ts1 = new StatusModel(p1, "If you're using Python 2, you should use xrange() instead of range()");
        StatusModel ts2 = new StatusModel(p1, "Or (if we're doing different representations of this particular function) you could define a lambda function via: lambda x,y: [ x[i:i+y] for i in range(0,len(x),y)] . I love this list-comprehension method!");
        StatusModel ts3 = new StatusModel(p1, "A JE TO TU: TORONTO ZAČÍNA S NYLANDEROM TESTOVAŤ PRESTUPOVÝ TRH");

        StatusModel ms1 = new StatusModel(p2, "Some soft requirements (can be broken, but the schedule is still feasible)");
        StatusModel ms2 = new StatusModel(p2, "A class can be placed only in a spare classroom.");
        StatusModel ms3 = new StatusModel(p2, "nwm uš co");

        StatusModel mms1 = new StatusModel(p3, "xDD");
        StatusModel mms2 = new StatusModel(p3, "This is where genetic algorithms");
        StatusModel mms3 = new StatusModel(p3, "How can we represent the chromosome for a class schedule? ");

        p1.getStatuses().add(ts1);
        p1.getStatuses().add(ts2);
        p1.getStatuses().add(ts3);

        p2.getStatuses().add(ms1);
        p2.getStatuses().add(ms2);
        p2.getStatuses().add(ms3);

        p3.getStatuses().add(mms1);
        p3.getStatuses().add(mms2);
        p3.getStatuses().add(mms3);

        List<StatusModel> l = new ArrayList<>();
        l.add(ts1);
        l.add(ts2);
        l.add(ts3);
        l.add(ms1);
        l.add(ms2);
        l.add(ms3);
        l.add(mms1);
        l.add(mms2);
        l.add(mms3);

        return l;
    }

    public void initViewPager(){
        viewPager = (HorizontalViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new HorizontalViewPagerAdapter(getSupportFragmentManager(), _testData());
        viewPager.setAdapter(viewPagerAdapter);

    }

    private void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_LAUNCHER);
        // startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        isMinimized = true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            minimizeApp();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {
            Log.d(TAG_INFO,"Logging out");
            if(isMinimized == null) {
                auth.signOut();
                auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user == null) {
                            // user auth state is changed - user is null
                            // launch login activity
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
                            finish();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            Log.w(TAG_INFO, "LoginActivity STARTED");
                        }
                    }
                });
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG_INFO, "DESTROYED!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG_INFO, "Minimized!!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG_INFO, "STOPPED!!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG_INFO, "Resumed");
        if (!isLoggedIn) {
            // should call authentificate
             finish();
        } else if (isMinimized != null && isMinimized) {
            isMinimized = null;
        }
    }
}
