package com.youtubeapp.doto.dotayoutubechanels.activities.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youtubeapp.doto.dotayoutubechanels.R;


/**
 * Created by admin on 04-Dec-15.
 */
@SuppressLint("ValidFragment")
public class FavoriteFragment extends Fragment {

    private Context context;

    public FavoriteFragment(){}

    @SuppressLint("ValidFragment")
    public FavoriteFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        return rootView;
    }
}
