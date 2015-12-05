package com.youtubeapp.doto.dotayoutubechanels.activities.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.youtubeapp.doto.dotayoutubechanels.R;
import com.youtubeapp.doto.dotayoutubechanels.activities.utils.ConnectionUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        if (ConnectionUtils.isNetworkConnected(getApplicationContext())) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(getString(R.string.connection_status_message))
                    .setContentText(getString(R.string.connection_fail_message))
                    .setConfirmText(getString(R.string.action_ok))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .show();
        }
    }
}
