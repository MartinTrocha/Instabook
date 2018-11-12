package com.example.hp.socialnetwork;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.hp.socialnetwork.model.StatusModel;

import java.util.List;

/**
 * Created by mohit on 1/1/17.
 */

public class HorizontalViewPagerAdapter extends FragmentStatePagerAdapter{

    public String parentId;
    List<StatusModel> statuses;

    public void setParentID(String parentID){
        this.parentId = parentID;
    }

    public HorizontalViewPagerAdapter(FragmentManager fm, List<StatusModel> statuses) {
        super(fm);
        this.statuses=statuses;
    }

    @Override
    public Fragment getItem(int position) {

        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle();

        contentFragment.setProfile(statuses.get(position).getAuthor());

        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Override
    public int getCount() {
        return statuses.size();
    }
}
