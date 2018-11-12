package com.example.hp.socialnetwork;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hp.socialnetwork.model.ProfileModel;
import com.example.hp.socialnetwork.model.StatusModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String PARENT_BUNDLE="socialnetwork.parent";
    public static String CHILD_BUNDLE="socialnetwork.child";

    public HorizontalViewPagerAdapter viewPagerAdapter;
    public HorizontalViewPager viewPager;

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
