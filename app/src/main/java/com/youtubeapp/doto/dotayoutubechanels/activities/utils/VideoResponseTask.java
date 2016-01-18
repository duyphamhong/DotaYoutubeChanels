package com.youtubeapp.doto.dotayoutubechanels.activities.utils;

import android.os.AsyncTask;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.youtubeapp.doto.dotayoutubechanels.activities.constant.DeveloperKey;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by admin on 1/12/2016.
 */
public class VideoResponseTask extends AsyncTask<Void, Void, ArrayList<Video>> {

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
    private static YouTube youtube;
    private String videoId;

    private ArrayList<Video> listVideo;

    public VideoResponseTask(String videoId) {
        // TODO Auto-generated constructor stub
        this.videoId = videoId;

        listVideo = new ArrayList<Video>();
    }

    @Override
    protected ArrayList<Video> doInBackground(Void... params) {
        // TODO Auto-generated method stub

        try {
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                    new HttpRequestInitializer() {
                        public void initialize(HttpRequest request)
                                throws IOException {
                        }
                    }).setApplicationName("youtube-cmdline-search-sample")
                    .build();

            YouTube.Videos.List video;
            video = youtube.videos().list("snippet, statistics");

            video.setKey(DeveloperKey.DEVELOPER_KEY);
            video.setId(videoId);

            video.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            VideoListResponse reponse = video.execute();
            ArrayList<Video> list = (ArrayList<Video>) reponse
                    .getItems();

            return list;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Video> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getSnippet().getThumbnails() == null)
                continue;
            com.youtubeapp.doto.dotayoutubechanels.activities.models.Video video = new com.youtubeapp.doto.dotayoutubechanels.activities.models.Video();
            video.setId(videoId);
            video.setTitle(result.get(i).getSnippet().getTitle());
            video.setDescription(result.get(i).getSnippet().getDescription());
            video.setThumbnails(result.get(i).getSnippet().getThumbnails().getDefault().getUrl());
            video.setPublishedAt(result.get(i).getSnippet().getPublishedAt());
            video.setViewCount(result.get(i).getStatistics().getViewCount());
            video.setLikeCount(result.get(i).getStatistics().getLikeCount());
            video.setDisLikeCount(result.get(i).getStatistics().getDislikeCount());
            video.setFavoriteCount(result.get(i).getStatistics().getFavoriteCount());
            video.setCommentCount(result.get(i).getStatistics().getCommentCount());
        }
    }
}
