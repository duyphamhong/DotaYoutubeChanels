package com.youtubeapp.doto.dotayoutubechanels.activities.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.youtubeapp.doto.dotayoutubechanels.R;
import com.youtubeapp.doto.dotayoutubechanels.activities.adapters.TabPagerAdapter;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

    //----------------------- INIT VARIABLE ---------------------------
    private ViewPager viewPager;
    private TabPagerAdapter pagerAdapter;
    private ActionBar actionBar;
    private String[] tabs = {"Tab1", "Tab2", "Tab3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();

        // Init tabs host
        initTabsHost();

    }

    /*
    * Init tab contain 3 fragments
    * */
    private void initTabsHost() {
        viewPager = (ViewPager) findViewById(R.id.mainViewPager);
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager());

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
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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
}
