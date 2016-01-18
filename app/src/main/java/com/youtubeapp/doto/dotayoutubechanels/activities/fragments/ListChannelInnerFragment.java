package com.youtubeapp.doto.dotayoutubechanels.activities.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.youtubeapp.doto.dotayoutubechanels.R;
import com.youtubeapp.doto.dotayoutubechanels.activities.activities.PlayListActivity;
import com.youtubeapp.doto.dotayoutubechanels.activities.adapters.ChannelsListAdapter;
import com.youtubeapp.doto.dotayoutubechanels.activities.models.ChannelItem;
import com.youtubeapp.doto.dotayoutubechanels.activities.utils.ScreenUtils;
import com.youtubeapp.doto.dotayoutubechanels.activities.widgets.FeedContextMenu;
import com.youtubeapp.doto.dotayoutubechanels.activities.widgets.FeedContextMenuManager;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by admin on 04-Dec-15.
 */
@SuppressLint("ValidFragment")
public class ListChannelInnerFragment extends Fragment
        implements ChannelsListAdapter.OnChannelInnerItemClickListener, FeedContextMenu.OnFeedContextMenuItemClickListener,
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private Context context;

    private View rootView;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private SearchView editTextSearch;
    private ImageButton btnSearch;

    private ChannelsListAdapter mChannelsListAdapter;
    private ArrayList<ChannelItem> channelArrayList;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    public ListChannelInnerFragment() {
    }

    @SuppressLint("ValidFragment")
    public ListChannelInnerFragment(Context context, ArrayList<ChannelItem> channelArrayList) {
        this.context = context;
        this.channelArrayList = channelArrayList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChannelsListAdapter = new ChannelsListAdapter(getActivity().getApplicationContext(), channelArrayList);
        mChannelsListAdapter.setOnFeedItemClickListener(this);
        mChannelsListAdapter.setOnItemClickListener(onItemClickListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_listchannels, container, false);

        initSearchView(rootView);
        initVoiceSearch(rootView);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.channels_recycler_view);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        mRecyclerView.setAdapter(mChannelsListAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                FeedContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
            }
        });


//        ChannelsListAdapter.OnItemClickListener onItemClickListener = new ChannelsListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(getActivity(), "Clicked " + position, Toast.LENGTH_SHORT).show();
//            }
//        };
//        mChannelsListAdapter.setOnItemClickListener(onItemClickListener);

        //mChangeViewSettingButton = (ImageButton) rootView.findViewById(R.id.btn_channel_list_change_show_setting);
        //mChangeViewSettingButton.setOnClickListener(this);

        return rootView;
    }

    /**************
     * Init Search module
     *****************/
    private void initSearchView(View rootView) {
        editTextSearch = (SearchView) rootView.findViewById(R.id.edt_search_channel);
        editTextSearch.setQueryHint("Search your channel");
        editTextSearch.setOnQueryTextListener(this);
        editTextSearch.setOnCloseListener(this);
        editTextSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ScreenUtils.hideSoftKeyboard(getActivity());
                }
            }
        });
    }

    private void initVoiceSearch(View rootView) {
        btnSearch = (ImageButton) rootView.findViewById(R.id.btn_voice_search_channel);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(context,
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editTextSearch.setQuery(result.get(0), false);
                }
                break;
            }

        }
    }

    /**************End init search module*****************/

    /**************
     * Set interface
     *****************/
    ChannelsListAdapter.OnItemClickListener onItemClickListener = new ChannelsListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent transitionIntent = new Intent(getActivity(), PlayListActivity.class);
            transitionIntent.putExtra(PlayListActivity.EXTRA_PARAM_CHANNEL_ID, channelArrayList.get(position).getId());
            transitionIntent.putExtra(PlayListActivity.EXTRA_PARAM_IMAGE_RESOURCE, channelArrayList.get(position).getThumbnails());
            transitionIntent.putExtra(PlayListActivity.EXTRA_PARAM_TITLE, channelArrayList.get(position).getTitle());

            ImageView placeImage = (ImageView) view.findViewById(R.id.img_channel_thumbnail);

            Pair<View, String> imagePair = Pair.create((View) placeImage, "tImage");

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), imagePair);
            ActivityCompat.startActivity(getActivity(), transitionIntent, options.toBundle());
        }
    };
    /***************
     * End set interface
     ****************/

    @Override
    public void onAddChannelToFavorClick(View v, int position) {

    }

    @Override
    public void onIgnoreChannelClick(View v, int position) {

    }

    @Override
    public void onShowContextMenuClick(View v, int position) {
        Toast.makeText(context, "show context menu", Toast.LENGTH_SHORT).show();
        FeedContextMenuManager.getInstance().toggleContextMenuFromView(v, position, this, context);
    }

    @Override
    public void onGetNotificationClick(int feedItem) {

    }

    @Override
    public void onShareClick(int feedItem) {

    }

    @Override
    public void onMoreInfoClick(int feedItem) {

    }

    @Override
    public void onCancelClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mChannelsListAdapter.getFilter().filter(query);
        ScreenUtils.hideSoftKeyboard(getActivity());
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mChannelsListAdapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public boolean onClose() {
        mChannelsListAdapter.restoreChannelList();
        return false;
    }
}
