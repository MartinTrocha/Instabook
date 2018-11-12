package com.example.hp.socialnetwork;

import android.os.Bundle;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.hp.socialnetwork.model.ProfileModel;
import com.example.hp.socialnetwork.model.StatusModel;

import java.util.List;

/**
 * Created by mohit on 28/12/16.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    ProfileModel profile;


    public ViewPagerAdapter(FragmentManager fm, ProfileModel profile) {
        super(fm);
        this.profile=profile;
    }

    @Override
    public int getCount() {
        return profile.getStatuses().size()+1;
    }

    @Override
    public Fragment getItem(int position) {
        ChildFragment childFragment = new ChildFragment();
        Bundle bundle = new Bundle();

        if (position==0){
            bundle.putString(MainActivity.PARENT_BUNDLE, "Profil: " + profile.getName() + " " + profile.getSurname());
            bundle.putString(MainActivity.CHILD_BUNDLE,  "Info: " + profile.getInfo());
            childFragment.setArguments(bundle);
        }
        else {
            bundle.putString(MainActivity.PARENT_BUNDLE, "");
            bundle.putString(MainActivity.CHILD_BUNDLE, profile.getName() + " " + profile.getSurname()+": "+
                    profile.getStatuses().get(position-1).getText());
            childFragment.setArguments(bundle);
        }

        return childFragment;
    }
}
