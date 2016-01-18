package com.youtubeapp.doto.dotayoutubechanels.activities.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.youtubeapp.doto.dotayoutubechanels.R;
import com.youtubeapp.doto.dotayoutubechanels.activities.constant.DeveloperKey;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by DuyPH on 12/17/2015.
 */

/**
 * This Activity shows how the YouTubePlayerView can be used to create a "Lightbox" similar to that of the
 * StandaloneYouTubePlayer. Using this method, we can improve upon it by performing transitions and allowing for
 * custom behaviour, such as closing when the user clicks anywhere outside the player
 * We manage to avoid rebuffering the video by setting some configchange flags on this activities declaration in the manifest.
 */

public class VideoPlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    //Keys
    public static final String KEY_VIDEO_ID = "KEY_VIDEO_ID";
    private static final String KEY_VIDEO_TIME = "KEY_VIDEO_TIME";

    private YouTubePlayer mPlayer;
    private boolean isFullscreen;

    private int millis;
    private String mVideoId;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_youtube_lightbox);

        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout_youtube_activity);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.youTubePlayerView);
        playerView.initialize(DeveloperKey.DEVELOPER_KEY, this);

        if (bundle != null) {
            millis = bundle.getInt(KEY_VIDEO_TIME);
        }

        final Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(KEY_VIDEO_ID)) {
            mVideoId = extras.getString(KEY_VIDEO_ID);
        } else {
            finish();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        mPlayer = youTubePlayer;
        youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
        youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {
                isFullscreen = b;
            }
        });

        if (mVideoId != null && !wasRestored) {
            youTubePlayer.loadVideo(mVideoId);
        }

        if (wasRestored) {
            //youTubePlayer.seekToMillis(millis);
            youTubePlayer.loadVideo(mVideoId, millis);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mPlayer != null) {
            outState.putInt(KEY_VIDEO_TIME, mPlayer.getCurrentTimeMillis());
        }
    }

    @Override
    public void onBackPressed() {
        //If the Player is fullscreen then the transition crashes on L when navigating back to the MainActivity
        boolean finish = true;
        try {
            if (mPlayer != null) {
                if (isFullscreen) {
                    finish = false;
                    mPlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                        @Override
                        public void onFullscreen(boolean b) {
                            //Wait until we are out of fullscreen before finishing this activity
                            if (!b) {
                                finish();
                            }
                        }
                    });
                    mPlayer.setFullscreen(false);
                }
                mPlayer.pause();
            }
        } catch (final IllegalStateException e) {
            //Crashlytics.logException(e);
        }

        if (finish) {
            super.onBackPressed();
        }
    }
}
