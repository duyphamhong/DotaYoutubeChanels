package com.youtubeapp.doto.dotayoutubechanels.activities.models;

import com.google.api.client.util.DateTime;

import java.math.BigInteger;

/**
 * Created by admin on 12/27/2015.
 */
public class ChannelItem extends BaseItem {
    private DateTime publishedAt;
    private BigInteger viewCount;
    private BigInteger commentCount;
    private BigInteger subscriberCount;
    private BigInteger videoCount;

    public DateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(DateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public BigInteger getViewCount() {
        return viewCount;
    }

    public void setViewCount(BigInteger viewCount) {
        this.viewCount = viewCount;
    }

    public BigInteger getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(BigInteger commentCount) {
        this.commentCount = commentCount;
    }

    public BigInteger getSubscriberCount() {
        return subscriberCount;
    }

    public void setSubscriberCount(BigInteger subscriberCount) {
        this.subscriberCount = subscriberCount;
    }

    public BigInteger getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(BigInteger videoCount) {
        this.videoCount = videoCount;
    }
}
