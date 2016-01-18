package com.youtubeapp.doto.dotayoutubechanels.activities.utils;

import android.os.AsyncTask;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.youtubeapp.doto.dotayoutubechanels.activities.Interface.IGettingServiceInfoCompletedListener;
import com.youtubeapp.doto.dotayoutubechanels.activities.constant.DeveloperKey;
import com.youtubeapp.doto.dotayoutubechanels.activities.models.PlayListItem;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by admin on 1/12/2016.
 */
public class PlayListItemResponseTask extends
        AsyncTask<Void, Void, ArrayList<PlaylistItem>> {

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
    private static YouTube youtube;
    private String playListId;

    private ArrayList<PlayListItem> listVideo;
    private IGettingServiceInfoCompletedListener gettingServiceInfoCompletedListener;

    public PlayListItemResponseTask(String playListId, IGettingServiceInfoCompletedListener gettingServiceInfoCompletedListener) {
        // TODO Auto-generated constructor stub
        this.playListId = playListId;
        this.gettingServiceInfoCompletedListener = gettingServiceInfoCompletedListener;

        listVideo = new ArrayList<PlayListItem>();
    }

    @Override
    protected ArrayList<PlaylistItem> doInBackground(Void... params) {
        // TODO Auto-generated method stub

        try {
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                    new HttpRequestInitializer() {
                        public void initialize(HttpRequest request)
                                throws IOException {
                        }
                    }).setApplicationName("youtube-cmdline-search-sample")
                    .build();

            YouTube.PlaylistItems.List playListItem;
            playListItem = youtube.playlistItems().list("snippet");

            playListItem.setKey(DeveloperKey.DEVELOPER_KEY);
            playListItem.setPlaylistId(playListId);
            playListItem.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            PlaylistItemListResponse reponse = playListItem.execute();
            ArrayList<PlaylistItem> list = (ArrayList<PlaylistItem>) reponse
                    .getItems();

            return list;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<PlaylistItem> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getSnippet().getThumbnails() == null)
                continue;
            PlayListItem pLayListItem = new PlayListItem();
            pLayListItem.setId(result.get(i).getSnippet().getResourceId().getVideoId());
            pLayListItem.setTitle(result.get(i).getSnippet().getTitle());
            pLayListItem.setDescription(result.get(i).getSnippet().getDescription());
            pLayListItem.setThumbnails(result.get(i).getSnippet().getThumbnails().getHigh().getUrl());
            pLayListItem.setPublishDate(result.get(i).getSnippet().getPublishedAt());

            this.listVideo.add(pLayListItem);
        }
        this.gettingServiceInfoCompletedListener.onGetListCompleted(listVideo);
    }
}
