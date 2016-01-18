package com.youtubeapp.doto.dotayoutubechanels.activities.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.youtubeapp.doto.dotayoutubechanels.activities.Interface.IGettingServiceInfoCompletedListener;
import com.youtubeapp.doto.dotayoutubechanels.activities.constant.DeveloperKey;
import com.youtubeapp.doto.dotayoutubechanels.activities.constant.VideoChannels;
import com.youtubeapp.doto.dotayoutubechanels.activities.models.ChannelItem;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by admin on 1/7/2016.
 */
public class ChannelsListResponeTask extends AsyncTask<Void, Void, ArrayList<Channel>> {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
    private static YouTube youtube;
    private IGettingServiceInfoCompletedListener gettingChannelsListListener;
    private ArrayList<ChannelItem> channelArrayList;

    public ChannelsListResponeTask(IGettingServiceInfoCompletedListener gettingChannelsListListener) {
        this.gettingChannelsListListener = gettingChannelsListListener;
        channelArrayList = new ArrayList<ChannelItem>();
    }

    @Override
    protected ArrayList<Channel> doInBackground(Void... params) {

        try {
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                    new HttpRequestInitializer() {
                        @Override
                        public void initialize(com.google.api.client.http.HttpRequest request) throws IOException {

                        }
                    }).setApplicationName("youtube-cmdline-search-sample")
                    .build();
            YouTube.Channels.List channelList;
            channelList = youtube.channels().list("snippet, statistics");

            channelList.setKey(DeveloperKey.DEVELOPER_KEY);
            channelList.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            ArrayList<Channel> finalList = new ArrayList<Channel>();

            for (String channelUser : VideoChannels.DOTA_CHANNEL_USER) {
                channelList.setId(channelUser);
                ChannelListResponse response = channelList.execute();
                ArrayList<Channel> list = (ArrayList<Channel>) response
                        .getItems();
                finalList = mergeList(finalList, list);
            }

            return finalList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<Channel> mergeList(ArrayList<Channel> listA, ArrayList<Channel> listB) {
        ArrayList<Channel> mergeList = new ArrayList<Channel>();
        mergeList.addAll(listA);
        mergeList.addAll(listB);
        return mergeList;
    }

    @Override
    protected void onPostExecute(ArrayList<Channel> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        if (result != null) {
            for (int i = 0; i < result.size(); i++) {
                ChannelItem channelItem = new ChannelItem();
                channelItem.setId(result.get(i).getId());
                channelItem.setTitle(result.get(i).getSnippet().getTitle());
                channelItem.setDescription(result.get(i).getSnippet().getDescription());
                channelItem.setThumbnails(result.get(i).getSnippet()
                        .getThumbnails().getDefault().getUrl());
                channelItem.setViewCount(result.get(i).getStatistics().getViewCount());
                channelItem.setCommentCount(result.get(i).getStatistics().getCommentCount());
                channelItem.setSubscriberCount(result.get(i).getStatistics().getSubscriberCount());
                channelItem.setVideoCount(result.get(i).getStatistics().getVideoCount());

                channelArrayList.add(channelItem);
            }

            gettingChannelsListListener.onGetListCompleted(channelArrayList);
        } else {
            Log.d("RESULT_NULL", "NULL!!");
        }
    }
}
