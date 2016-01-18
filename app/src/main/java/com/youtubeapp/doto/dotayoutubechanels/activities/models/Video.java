package com.youtubeapp.doto.dotayoutubechanels.activities.models;

import com.google.api.client.util.DateTime;

import java.math.BigInteger;

/**
 * Created by admin on 12/28/2015.
 */
public class Video extends BaseItem{
    private DateTime publishedAt;
    private BigInteger viewCount;
    private BigInteger commentCount;
    private BigInteger likeCount;
    private BigInteger disLikeCount;
    private BigInteger favoriteCount;

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

    public BigInteger getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(BigInteger likeCount) {
        this.likeCount = likeCount;
    }

    public BigInteger getDisLikeCount() {
        return disLikeCount;
    }

    public void setDisLikeCount(BigInteger disLikeCount) {
        this.disLikeCount = disLikeCount;
    }

    public BigInteger getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(BigInteger favoriteCount) {
        this.favoriteCount = favoriteCount;
    }
}
