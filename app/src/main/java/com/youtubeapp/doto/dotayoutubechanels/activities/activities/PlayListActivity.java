package com.youtubeapp.doto.dotayoutubechanels.activities.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.youtubeapp.doto.dotayoutubechanels.R;
import com.youtubeapp.doto.dotayoutubechanels.activities.Interface.IGettingServiceInfoCompletedListener;
import com.youtubeapp.doto.dotayoutubechanels.activities.adapters.PlayListAdapter;
import com.youtubeapp.doto.dotayoutubechanels.activities.models.PLayList;
import com.youtubeapp.doto.dotayoutubechanels.activities.utils.CachingBitmaps;
import com.youtubeapp.doto.dotayoutubechanels.activities.utils.DownloadImageTask;
import com.youtubeapp.doto.dotayoutubechanels.activities.utils.PlayListResponseTask;
import com.youtubeapp.doto.dotayoutubechanels.activities.widgets.PlayListContextMenu;
import com.youtubeapp.doto.dotayoutubechanels.activities.widgets.PlayListContextMenuManager;

import java.util.ArrayList;

/**
 * Created by admin on 1/15/2016.
 */
public class PlayListActivity extends ActionBarActivity implements IGettingServiceInfoCompletedListener<PLayList>,
        PlayListAdapter.OnPlaylistInnerItemClickListener, PlayListContextMenu.OnFeedContextMenuItemClickListener, DownloadImageTask.IDownLoadImageCompletedListener {
    public static final String EXTRA_PARAM_CHANNEL_ID = "channel_id";
    public static final String EXTRA_PARAM_IMAGE_RESOURCE = "image_resource";
    public static final String EXTRA_PARAM_TITLE = "title";

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private PlayListAdapter mPlayListAdapter;
    private ImageView imgChannelThumbnail;
    private FrameLayout mainLayout;

    private String thumbnailResource;
    private String channelId;
    private String channelTitle;
    private ArrayList<PLayList> pLayLists = new ArrayList<PLayList>();
    private Palette mPalette;
    private int defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        mainLayout = (FrameLayout) findViewById(R.id.layout_play_list);

        //load Channel icon thumbnail
        channelTitle = getIntent().getExtras().getString(EXTRA_PARAM_TITLE);
        getSupportActionBar().setTitle(channelTitle);
        thumbnailResource = getIntent().getExtras().getString(EXTRA_PARAM_IMAGE_RESOURCE);
        imgChannelThumbnail = (ImageView) findViewById(R.id.img_channel_thumbnail);
        defaultColor = getResources().getColor(R.color.colorPrimaryDark);
        loadThumbnail();

        //init RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.play_list_recycler_view);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                PlayListContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
            }
        });

        //init adapter
        mPlayListAdapter = new PlayListAdapter(this, pLayLists);
        mPlayListAdapter.setOnFeedItemClickListener(this);
        mPlayListAdapter.setOnItemClickListener(onItemClickListener);

        //set adapter
        mRecyclerView.setAdapter(mPlayListAdapter);

        channelId = getIntent().getExtras().getString(EXTRA_PARAM_CHANNEL_ID);
        new PlayListResponseTask(this, channelId).execute();
    }

    private void loadThumbnail() {
        new CachingBitmaps().loadBitmap(thumbnailResource, imgChannelThumbnail, this);
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

    PlayListAdapter.OnItemClickListener onItemClickListener = new PlayListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent transitionIntent = new Intent(PlayListActivity.this, PlayListItemActivity.class);
            transitionIntent.putExtra(PlayListItemActivity.EXTRA_PARAM_ID, pLayLists.get(position).getId());
            transitionIntent.putExtra(PlayListItemActivity.EXTRA_PARAM_IMAGE_RESOURCE, pLayLists.get(position).getThumbnails());

            ImageView placeImage = (ImageView) view.findViewById(R.id.img_play_list_thumbnail);

            Pair<View, String> imagePair = Pair.create((View) placeImage, "tPlayListImage");

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(PlayListActivity.this, imagePair);
            ActivityCompat.startActivity(PlayListActivity.this, transitionIntent, options.toBundle());
        }
    };

    @Override
    public void onGetListCompleted(ArrayList<PLayList> list) {
        pLayLists.clear();
        pLayLists.addAll(list);
        mPlayListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onShowContextMenuClick(View v, int position) {
        Toast.makeText(this, "show context menu", Toast.LENGTH_SHORT).show();
        PlayListContextMenuManager.getInstance().toggleContextMenuFromView(v, position, this, this);
    }

    @Override
    public void onGetNotificationClick(int feedItem) {

    }

    @Override
    public void onShareClick(int feedItem) {

    }

    @Override
    public void onMakeFavorClick(int feedItem) {

    }

    @Override
    public void onCancelClick(int feedItem) {
        PlayListContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onDownloadCompleted(Bitmap bitmap) {
        colorize(bitmap);
    }
}
