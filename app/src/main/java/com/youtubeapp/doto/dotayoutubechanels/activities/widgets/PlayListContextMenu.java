package com.youtubeapp.doto.dotayoutubechanels.activities.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.youtubeapp.doto.dotayoutubechanels.R;
import com.youtubeapp.doto.dotayoutubechanels.activities.utils.ScreenUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 1/17/2016.
 */
public class PlayListContextMenu extends LinearLayout {
    private static final int CONTEXT_MENU_WIDTH = ScreenUtils.dpToPx(200);

    private int feedItem = -1;

    private OnFeedContextMenuItemClickListener onItemClickListener;

    public PlayListContextMenu(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_play_list_context_menu, this, true);
        setBackgroundResource(R.drawable.bg_container_shadow);
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(CONTEXT_MENU_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void bindToItem(int feedItem) {
        this.feedItem = feedItem;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.bind(this);
    }

    public void dismiss() {
        ((ViewGroup) getParent()).removeView(PlayListContextMenu.this);
    }

    @OnClick(R.id.btn_get_notification)
    public void onGetNotification() {
        if (onItemClickListener != null) {
            onItemClickListener.onGetNotificationClick(feedItem);
        }
    }

    @OnClick(R.id.btn_share)
    public void onShare() {
        if (onItemClickListener != null) {
            onItemClickListener.onShareClick(feedItem);
        }
    }

    @OnClick(R.id.btn_more_info)
    public void onMakeFavorClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onMakeFavorClick(feedItem);
        }
    }

    @OnClick(R.id.btnCancel)
    public void onCancelClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onCancelClick(feedItem);
        }
    }

    public void setOnFeedMenuItemClickListener(OnFeedContextMenuItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnFeedContextMenuItemClickListener {
        public void onGetNotificationClick(int feedItem);

        public void onShareClick(int feedItem);

        public void onMakeFavorClick(int feedItem);

        public void onCancelClick(int feedItem);
    }
}
