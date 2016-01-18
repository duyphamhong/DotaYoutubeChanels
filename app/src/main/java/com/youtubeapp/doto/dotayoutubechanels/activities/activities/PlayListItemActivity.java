package com.youtubeapp.doto.dotayoutubechanels.activities.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.youtubeapp.doto.dotayoutubechanels.R;
import com.youtubeapp.doto.dotayoutubechanels.activities.Interface.IGettingServiceInfoCompletedListener;
import com.youtubeapp.doto.dotayoutubechanels.activities.adapters.PlayListItemAdapter;
import com.youtubeapp.doto.dotayoutubechanels.activities.models.PlayListItem;
import com.youtubeapp.doto.dotayoutubechanels.activities.utils.CachingBitmaps;
import com.youtubeapp.doto.dotayoutubechanels.activities.utils.DownloadImageTask;
import com.youtubeapp.doto.dotayoutubechanels.activities.utils.PlayListItemResponseTask;
import com.youtubeapp.doto.dotayoutubechanels.activities.widgets.PlayListContextMenuManager;

import java.util.ArrayList;

/**
 * Created by admin on 1/17/2016.
 */
public class PlayListItemActivity extends ActionBarActivity implements DownloadImageTask.IDownLoadImageCompletedListener, IGettingServiceInfoCompletedListener {
    public static final String EXTRA_PARAM_ID = "channel_id";
    public static final String EXTRA_PARAM_IMAGE_RESOURCE = "image_resource";

    private FrameLayout mainLayout;
    private ImageView imgPlayListItemThumbnail;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private PlayListItemAdapter playListItemAdapter;

    private String thumbnailResource;
    private int defaultColor;
    private Palette mPalette;
    private ArrayList<PlayListItem> listItems = new ArrayList<PlayListItem>();
    private String playListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_item);
        mainLayout = (FrameLayout) findViewById(R.id.layout_play_list_item);

        //load Channel icon thumbnail
        thumbnailResource = getIntent().getExtras().getString(EXTRA_PARAM_IMAGE_RESOURCE);
        imgPlayListItemThumbnail = (ImageView) findViewById(R.id.img_play_list_thumbnail);
        defaultColor = getResources().getColor(R.color.colorPrimaryDark);
        loadThumbnail();

        //init RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.play_list_item_recycler_view);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                PlayListContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
            }
        });

        //init adapter
        playListItemAdapter = new PlayListItemAdapter(this, listItems);
        playListItemAdapter.setOnItemClickListener(onItemClickListener);

        //set adapter
        mRecyclerView.setAdapter(playListItemAdapter);

        playListId = getIntent().getExtras().getString(EXTRA_PARAM_ID);
        new PlayListItemResponseTask(playListId, this).execute();
    }

    private void loadThumbnail() {
        new CachingBitmaps().loadBitmap(thumbnailResource, imgPlayListItemThumbnail, this);
    }

    private void colorize(Bitmap photo) {
        mPalette = Palette.generate(photo);
        applyPalette();
    }

    private void applyPalette() {
        getWindow().setBackgroundDrawable(new ColorDrawable(mPalette.getDarkMutedColor(defaultColor)));
        mainLayout.setBackgroundColor(mPalette.getVibrantColor(defaultColor));
        mRecyclerView.setBackgroundColor(mPalette.getVibrantColor(defaultColor));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(mPalette.getDarkVibrantColor(defaultColor)));

        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(mPalette.getDarkVibrantColor(defaultColor));
    }

    PlayListItemAdapter.OnItemClickListener onItemClickListener = new PlayListItemAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent transitionIntent = new Intent(PlayListItemActivity.this, VideoPlayerActivity.class);
            transitionIntent.putExtra(VideoPlayerActivity.KEY_VIDEO_ID, listItems.get(position).getId());
            startActivity(transitionIntent);
        }
    };

    @Override
    public void onDownloadCompleted(Bitmap bitmap) {
        colorize(bitmap);
    }

    @Override
    public void onGetListCompleted(ArrayList list) {
        listItems.clear();
        listItems.addAll(list);
        playListItemAdapter.notifyDataSetChanged();
    }
}
