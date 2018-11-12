package com.example.hp.socialnetwork;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.socialnetwork.model.ProfileModel;


public class ContentFragment extends Fragment {

    public VerticalViewPager viewPager;
    public ViewPagerAdapter verticalViewPagerAdapter;
    public String parentInd;
    public ProfileModel author;
    

    public ContentFragment() {
        // Required empty public constructor
    }
    
    public void setProfile(ProfileModel profile){
        this.author=profile;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);

        viewPager = (VerticalViewPager) view.findViewById(R.id.vpHorizontal);
        verticalViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), author);
        
        viewPager.setAdapter(verticalViewPagerAdapter);
        Helper.log("Card Created : " + parentInd);
        viewPager.setCurrentItem(1);
        return view;
    }

}
