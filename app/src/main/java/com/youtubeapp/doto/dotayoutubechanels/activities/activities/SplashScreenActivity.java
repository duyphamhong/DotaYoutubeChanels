package com.youtubeapp.doto.dotayoutubechanels.activities.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.youtubeapp.doto.dotayoutubechanels.R;
import com.youtubeapp.doto.dotayoutubechanels.activities.utils.ConnectionUtils;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TextView tv = (TextView)findViewById(R.id.tv_loading_screen);
        TextView tv_alert = (TextView)findViewById(R.id.tv_loading_screen_alert);
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/Yearsupplyoffairycakes.ttf");
        tv.setTypeface(customFont);
        tv_alert.setTypeface(customFont);


        if (ConnectionUtils.isNetworkConnected(getApplicationContext())) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            tv_alert.setText(getString(R.string.connection_fail_message));
        }
    }
}
