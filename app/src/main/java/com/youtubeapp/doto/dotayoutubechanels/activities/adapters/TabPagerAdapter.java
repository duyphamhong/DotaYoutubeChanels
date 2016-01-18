package com.youtubeapp.doto.dotayoutubechanels.activities.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.youtubeapp.doto.dotayoutubechanels.activities.fragments.FavoriteFragment;
import com.youtubeapp.doto.dotayoutubechanels.activities.fragments.ListChannelInnerFragment;
import com.youtubeapp.doto.dotayoutubechanels.activities.fragments.LiveScoreFragment;
import com.youtubeapp.doto.dotayoutubechanels.activities.models.ChannelItem;

import java.util.ArrayList;


/**
 * Created by admin on 04-Dec-15.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    private int numberOfTabs = 3;
    private Context context;

    private ListChannelInnerFragment listChannelFragment;
    private FavoriteFragment favoriteFragment;
    private LiveScoreFragment liveScoreFragment;

    /*
    *
    * Constructor of TabPagerAdapter
    *
    * */
    public TabPagerAdapter(FragmentManager fm, Context context,
                           ArrayList<ChannelItem> channelArrayList) {
        super(fm);
        this.context = context;
        listChannelFragment = new ListChannelInnerFragment(context, channelArrayList);
        favoriteFragment = new FavoriteFragment(context);
        liveScoreFragment = new LiveScoreFragment(context);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return listChannelFragment;
            case 1:
                return favoriteFragment;
            case 2:
                return liveScoreFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.numberOfTabs;
    }
}
