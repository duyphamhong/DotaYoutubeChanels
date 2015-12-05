package com.youtubeapp.doto.dotayoutubechanels.activities.fragments;

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
public class LiveScoreFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_livescore, container, false);
        return rootView;
    }
}
