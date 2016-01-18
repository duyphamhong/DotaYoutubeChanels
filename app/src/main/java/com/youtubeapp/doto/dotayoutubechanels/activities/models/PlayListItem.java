package com.youtubeapp.doto.dotayoutubechanels.activities.models;

import com.google.api.client.util.DateTime;

/**
 * Created by admin on 1/12/2016.
 */
public class PlayListItem extends BaseItem {
    private DateTime publishDate;

    public DateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(DateTime publishDate) {
        this.publishDate = publishDate;
    }
}
