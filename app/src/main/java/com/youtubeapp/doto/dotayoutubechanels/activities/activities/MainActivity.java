package com.youtubeapp.doto.dotayoutubechanels.activities.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.youtubeapp.doto.dotayoutubechanels.R;
import com.youtubeapp.doto.dotayoutubechanels.activities.Interface.IGettingServiceInfoCompletedListener;
import com.youtubeapp.doto.dotayoutubechanels.activities.adapters.TabPagerAdapter;
import com.youtubeapp.doto.dotayoutubechanels.activities.animation.ZoomOutPageTransformer;
import com.youtubeapp.doto.dotayoutubechanels.activities.models.ChannelItem;
import com.youtubeapp.doto.dotayoutubechanels.activities.utils.ChannelsListResponeTask;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener, IGettingServiceInfoCompletedListener<ChannelItem> {

    //----------------------- INIT VARIABLE ---------------------------
    private ViewPager viewPager;
    private TabPagerAdapter pagerAdapter;
    private ActionBar actionBar;
    private String[] tabs = {"Channel", "Your Favor", "Live"};
    private LoadToast loadToast;

    private ArrayList<ChannelItem> channelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check for any issues
        final YouTubeInitializationResult result = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
        if (result == YouTubeInitializationResult.SUCCESS) {
            actionBar = getSupportActionBar();

            loadToast = new LoadToast(this);
            loadToast.setTranslationY(500);
            loadToast.setBackgroundColor(Color.DKGRAY);

            channelList = new ArrayList<ChannelItem>();

            loadToast.setText("Loading...........").show();
            new ChannelsListResponeTask(this).execute();

        } else {
            result.getErrorDialog(this, 0).show();
        }
    }

    /*
    * Init tab contain 3 fragments
    * */
    private void initTabsHost() {
        viewPager = (ViewPager) findViewById(R.id.mainViewPager);
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), this, channelList);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        viewPager.setAdapter(pagerAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }


        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });


    }

    /*
     * adds the fragment to the FrameLayout
     */
    public void pushFragments(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.mainViewPager, fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onGetListCompleted(ArrayList<ChannelItem> list) {
        if (list != null) {
            loadToast.success();

            channelList = list;
            initTabsHost();
        } else {
            loadToast.error();
        }
    }

}
