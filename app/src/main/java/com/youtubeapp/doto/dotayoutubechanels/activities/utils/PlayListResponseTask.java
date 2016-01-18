package com.youtubeapp.doto.dotayoutubechanels.activities.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.youtubeapp.doto.dotayoutubechanels.activities.Interface.IGettingServiceInfoCompletedListener;
import com.youtubeapp.doto.dotayoutubechanels.activities.constant.DeveloperKey;
import com.youtubeapp.doto.dotayoutubechanels.activities.models.PLayList;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by admin on 12/27/2015.
 */
public class PlayListResponseTask extends AsyncTask<Void, Void, ArrayList<Playlist>> {

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
    private static YouTube youtube;
    private IGettingServiceInfoCompletedListener gettingServiceInfoCompletedListener;
    private ArrayList<PLayList> pLayLists;
    private String channelId;

    public PlayListResponseTask(IGettingServiceInfoCompletedListener gettingServiceInfoCompletedListener, String channelId) {
        this.gettingServiceInfoCompletedListener = gettingServiceInfoCompletedListener;
        pLayLists = new ArrayList<PLayList>();
        this.channelId = channelId;
    }

    @Override
    protected ArrayList<Playlist> doInBackground(Void... params) {

        try{
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                    new HttpRequestInitializer() {
                        @Override
                        public void initialize(com.google.api.client.http.HttpRequest request) throws IOException {

                        }
                    }).setApplicationName("youtube-cmdline-search-sample")
                    .build();
            YouTube.Playlists.List playList;
            playList = youtube.playlists().list("contentDetails, snippet");

            playList.setKey(DeveloperKey.DEVELOPER_KEY);
            playList.setChannelId(channelId);
            playList.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            PlaylistListResponse response = playList.execute();
            ArrayList<Playlist> list = (ArrayList<Playlist>) response
                    .getItems();

            return list;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Playlist> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        if (result != null) {
            for (int i = 0; i < result.size(); i++) {
                PLayList playlist = new PLayList();
                playlist.setId(result.get(i).getId());
                playlist.setTitle(result.get(i).getSnippet().getTitle());
                playlist.setThumbnails(result.get(i).getSnippet()
                        .getThumbnails().getHigh().getUrl());
                playlist.setItemCount(result.get(i).getContentDetails().getItemCount());

                pLayLists.add(playlist);
            }

            gettingServiceInfoCompletedListener.onGetListCompleted(pLayLists);
        }
        else {
            Log.d("RESULT_NULL", "NULL!!");
        }
    }
}
