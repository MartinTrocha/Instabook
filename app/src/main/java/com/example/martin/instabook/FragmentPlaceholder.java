package com.example.martin.instabook;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.PagerSnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.martin.instabook.model.PostModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentPlaceholder extends Fragment {
    private List<PostModel> postModels;

    public FragmentPlaceholder() {
        this.postModels = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_horizontal, parent, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.horizontal_recycler);
        recyclerView.setHasFixedSize(true);
        //recyclerView.invalidate();

        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        final RecyclerView.Adapter recyclerViewAdapter = new RecyclerAdapterHorizontal(this.getContext(), postModels);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(((RecyclerAdapterHorizontal) recyclerViewAdapter).current_vertical_position != 1) {
                    recyclerView.stopScroll();
                }
            }
        });

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        return rootView;
    }

    public void setTest(List<PostModel> list){
        this.postModels = list;
    }

}