package com.youtubeapp.doto.dotayoutubechanels.activities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youtubeapp.doto.dotayoutubechanels.R;
import com.youtubeapp.doto.dotayoutubechanels.activities.models.ChannelItem;
import com.youtubeapp.doto.dotayoutubechanels.activities.utils.CachingBitmaps;
import com.youtubeapp.doto.dotayoutubechanels.activities.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by admin on 1/4/2016.
 */
public class ChannelsListAdapter extends RecyclerView.Adapter<ChannelsListAdapter.ViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<ChannelItem> channelsList;
    private ArrayList<ChannelItem> channelBackupList = new ArrayList<ChannelItem>();

    private OnChannelInnerItemClickListener onChannelInnerItemClickListener;
    OnItemClickListener mItemClickListener;

    private ChannelItem mFilter = new ChannelItem();

    public ChannelsListAdapter(Context mContext, ArrayList<ChannelItem> channelsList) {
        this.mContext = mContext;
        this.channelsList = channelsList;
        this.channelBackupList.addAll(channelsList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_list_channels, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.bt_favor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChannelInnerItemClickListener.onAddChannelToFavorClick(v, viewHolder.getPosition());
            }
        });

        viewHolder.bt_ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChannelInnerItemClickListener.onIgnoreChannelClick(v, viewHolder.getPosition());
            }
        });

        viewHolder.bt_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChannelInnerItemClickListener.onShowContextMenuClick(v, viewHolder.getPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (channelsList.size() != 0) {
            ChannelItem item = channelsList.get(position);
            new CachingBitmaps().loadBitmap(channelsList.get(position)
                    .getThumbnails(), holder.placeChannelThumbnail, null);
            holder.placeChannelTitle.setText(item.getTitle());
            holder.placeChannelDescription.setText(StringUtils.subStringPartern(item.getDescription()));

            holder.placeChannelViewCount.setText(StringUtils.formatDecimal(item.getViewCount()));
            holder.placeChannelCommentCount.setText(StringUtils.formatDecimal(item.getCommentCount()));
            holder.placeChannelVideoCount.setText(StringUtils.formatDecimal(item.getVideoCount()));

            if (position % 2 == 0) {
                holder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.card_view_color_dark));
            } else {
                holder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.card_view_color));
            }
        }
    }

    @Override
    public int getItemCount() {
        return channelsList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // We implement here the filter logic
                if (constraint == null || constraint.length() == 0) {
                    // No filter implemented we return all the list
                    results.values = channelBackupList;
                    results.count = channelBackupList.size();
                }
                else {
                    // We perform filtering operation
                    ArrayList<ChannelItem> channelItemArrayList = new ArrayList<ChannelItem>();

                    for (ChannelItem p : channelsList) {
                        if (p.getTitle().toUpperCase().contains(constraint.toString().toUpperCase()))
                            channelItemArrayList.add(p);
                    }

                    results.values = channelItemArrayList;
                    results.count = channelItemArrayList.size();

                }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                // Now we have to inform the adapter about the new list filtered
                if (results.count == 0){
                    channelsList.clear();
                    notifyDataSetInvalidated();
                }
                else {
                    channelsList.clear();
                    channelsList.addAll((ArrayList<ChannelItem>) results.values);
                    notifyDataSetChanged();
                }
            }
        };
    }

    private void notifyDataSetInvalidated(){
        super.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout placeHolder;
        public LinearLayout cardView;

        public TextView placeChannelTitle;
        public TextView placeChannelDescription;
        public ImageView placeChannelThumbnail;
        public TextView placeChannelViewCount;
        public TextView placeChannelCommentCount;
        public TextView placeChannelVideoCount;

        public ImageButton bt_favor;
        public ImageButton bt_ignore;
        public ImageButton bt_option;

        public ViewHolder(View itemView) {
            super(itemView);
            placeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            placeHolder.setOnClickListener(this);
            placeChannelTitle = (TextView) itemView.findViewById(R.id.tv_channel_title);
            placeChannelDescription = (TextView) itemView.findViewById(R.id.tv_channel_description);
            placeChannelThumbnail = (ImageView) itemView.findViewById(R.id.img_channel_thumbnail);
            placeChannelViewCount = (TextView) itemView.findViewById(R.id.tv_channel_view_count);
            placeChannelCommentCount = (TextView) itemView.findViewById(R.id.tv_channel_comment_count);
            placeChannelVideoCount = (TextView) itemView.findViewById(R.id.tv_channel_video_count);

            cardView = (LinearLayout) itemView.findViewById(R.id.channel_card_view);

            bt_favor = (ImageButton)itemView.findViewById(R.id.bt_add_to_favorite_toggle);
            bt_ignore = (ImageButton)itemView.findViewById(R.id.bt_ignore_toggle);
            bt_option = (ImageButton)itemView.findViewById(R.id.bt_card_view_option);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }
    }

    public void setOnFeedItemClickListener(OnChannelInnerItemClickListener onChannelInnerItemClickListener) {
        this.onChannelInnerItemClickListener = onChannelInnerItemClickListener;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void restoreChannelList(){
        channelsList.clear();
        channelsList.addAll(channelBackupList);
        super.notifyDataSetChanged();
    }

    public interface OnChannelInnerItemClickListener {
        void onAddChannelToFavorClick(View v, int position);

        void onIgnoreChannelClick(View v, int position);

        void onShowContextMenuClick(View v, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
