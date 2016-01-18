package com.youtubeapp.doto.dotayoutubechanels.activities.models;

/**
 * Created by admin on 1/12/2016.
 */
public class BaseItem {
    private String id;
    private String title;
    private String thumbnails;
    private String description;

    public BaseItem(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
