package com.youtubeapp.doto.dotayoutubechanels.activities.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.youtubeapp.doto.dotayoutubechanels.activities.fragments.FavoriteFragment;
import com.youtubeapp.doto.dotayoutubechanels.activities.fragments.ListVideoFragment;
import com.youtubeapp.doto.dotayoutubechanels.activities.fragments.LiveScoreFragment;


/**
 * Created by admin on 04-Dec-15.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    private int numberOfTabs = 3;

    /*
    *
    * Constructor of TabPagerAdapter
    *
    * */
    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // Top Rated fragment activity
                return new ListVideoFragment();
            case 1:
                // Games fragment activity
                return new FavoriteFragment();
            case 2:
                // Movies fragment activity
                return new LiveScoreFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return this.numberOfTabs;
    }
}
