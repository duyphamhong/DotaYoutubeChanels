package com.youtubeapp.doto.dotayoutubechanels.activities.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.youtubeapp.doto.dotayoutubechanels.R;
import com.youtubeapp.doto.dotayoutubechanels.activities.utils.ScreenUtils;

/**
 * Created by admin on 1/17/2016.
 */
public class PlayListContextMenuManager extends RecyclerView.OnScrollListener implements View.OnAttachStateChangeListener  {

    private static PlayListContextMenuManager instance;

    private PlayListContextMenu contextMenuView;

    private boolean isContextMenuDismissing;
    private boolean isContextMenuShowing;

    public static PlayListContextMenuManager getInstance() {
        if (instance == null) {
            instance = new PlayListContextMenuManager();
        }
        return instance;
    }

    private PlayListContextMenuManager() {

    }

    public void toggleContextMenuFromView(View openingView, int feedItem, PlayListContextMenu.OnFeedContextMenuItemClickListener listener, Context context) {
        if (contextMenuView == null) {
            showContextMenuFromView(openingView, feedItem, listener, context);
        } else {
            hideContextMenu();
        }
    }

    private void showContextMenuFromView(final View openingView, int feedItem, PlayListContextMenu.OnFeedContextMenuItemClickListener listener, final Context context) {
        if (!isContextMenuShowing) {
            isContextMenuShowing = true;
            contextMenuView = new PlayListContextMenu(openingView.getContext());
            contextMenuView.bindToItem(feedItem);
            contextMenuView.addOnAttachStateChangeListener(this);
            contextMenuView.setOnFeedMenuItemClickListener(listener);

            ((ViewGroup) openingView.getRootView().findViewById(R.id.fragment_list_channels)).addView(contextMenuView);

            contextMenuView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contextMenuView.getViewTreeObserver().removeOnPreDrawListener(this);
                    setupContextMenuInitialPosition(openingView, context);
                    performShowAnimation();
                    return false;
                }
            });
        }
    }

    private void setupContextMenuInitialPosition(View openingView, Context context) {
        final int[] openingViewLocation = new int[2];
        openingView.getLocationOnScreen(openingViewLocation);
        int additionalBottomMargin = ScreenUtils.dpToPx(100);

        if (openingViewLocation[1] < ScreenUtils.getScreenHeight(context) / 2) {
            contextMenuView.setTranslationY(openingViewLocation[1] - contextMenuView.getHeight() / 4 - additionalBottomMargin);
        } else {
            contextMenuView.setTranslationY(openingViewLocation[1] - contextMenuView.getHeight() - additionalBottomMargin);
        }
        if(openingViewLocation[0] < ScreenUtils.getScreenWidth(context) / 2){
            contextMenuView.setTranslationX(openingViewLocation[0]);
        } else {
            contextMenuView.setTranslationX(openingViewLocation[0] - contextMenuView.getWidth());
        }
    }

    private void performShowAnimation() {
        contextMenuView.setPivotX(contextMenuView.getWidth() / 2);
        contextMenuView.setPivotY(contextMenuView.getHeight());
        contextMenuView.setScaleX(0.1f);
        contextMenuView.setScaleY(0.1f);
        contextMenuView.animate()
                .scaleX(1f).scaleY(1f)
                .setDuration(150)
                .setInterpolator(new OvershootInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isContextMenuShowing = false;
                    }
                });
    }

    public void hideContextMenu() {
        if (!isContextMenuDismissing) {
            isContextMenuDismissing = true;
            performDismissAnimation();
        }
    }

    private void performDismissAnimation() {
        contextMenuView.setPivotX(contextMenuView.getWidth() / 2);
        contextMenuView.setPivotY(contextMenuView.getHeight());
        contextMenuView.animate()
                .scaleX(0.1f).scaleY(0.1f)
                .setDuration(150)
                .setInterpolator(new AccelerateInterpolator())
                .setStartDelay(100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (contextMenuView != null) {
                            contextMenuView.dismiss();
                        }
                        isContextMenuDismissing = false;
                    }
                });
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (contextMenuView != null) {
            hideContextMenu();
            contextMenuView.setTranslationY(contextMenuView.getTranslationY() - dy);
        }
    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        contextMenuView = null;
    }
}
