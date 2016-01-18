package com.youtubeapp.doto.dotayoutubechanels.activities.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
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
import com.youtubeapp.doto.dotayoutubechanels.activities.models.PlayListItem;
import com.youtubeapp.doto.dotayoutubechanels.activities.utils.CachingBitmaps;

import java.util.ArrayList;

/**
 * Created by admin on 1/17/2016.
 */
public class PlayListItemAdapter extends RecyclerView.Adapter<PlayListItemAdapter.ViewHolder> implements Filterable {
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private ArrayList<PlayListItem> playListItems;
    private ArrayList<PlayListItem> backupList = new ArrayList<PlayListItem>();

    private OnPlaylistInnerItemClickListener playlistInnerItemClickListener;

    public PlayListItemAdapter(Context context, ArrayList<PlayListItem> pLayLists){
        this.mContext = context;
        this.playListItems = pLayLists;
        this.backupList.addAll(pLayLists);
    }

    @Override
    public PlayListItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_play_list_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (playListItems.size() != 0) {
            PlayListItem item = playListItems.get(position);
            new CachingBitmaps().loadBitmap(playListItems.get(position)
                    .getThumbnails(), holder.placeThumbnail, null);
            holder.placeTitle.setText(item.getTitle());

            if (position % 2 == 0) {
                holder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.transparent40));
            } else {
                holder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.transparent20));
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout placeHolder;
        public CardView cardView;

        public TextView placeTitle;
        public ImageView placeThumbnail;

        public ImageButton bt_option;

        public ViewHolder(View itemView) {
            super(itemView);
            placeHolder = (LinearLayout) itemView.findViewById(R.id.playListItemMainHolder);
            placeHolder.setOnClickListener(this);
            placeTitle = (TextView) itemView.findViewById(R.id.tv_play_list_item_title);
            placeThumbnail = (ImageView) itemView.findViewById(R.id.img_play_list_item_thumbnail);

            cardView = (CardView) itemView.findViewById(R.id.play_list_card_view_item);

            bt_option = (ImageButton)itemView.findViewById(R.id.bt_card_view_option);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }
    }

    public void restoreList(){
        playListItems.clear();
        playListItems.addAll(backupList);
        super.notifyDataSetChanged();
    }

    /****
     * Init inner Interface
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnPlaylistInnerItemClickListener {
        void onShowContextMenuClick(View v, int position);
    }
    /****
     * End init inner interface
     */

    /****
     *
     * set Interface
     */
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    public void setOnFeedItemClickListener(OnPlaylistInnerItemClickListener playlistInnerItemClickListener) {
        this.playlistInnerItemClickListener = playlistInnerItemClickListener;
    }
    /****
     *
     * End set Interface
     */

    /***
     *
     * Override field
     */
    @Override
    public int getItemCount() {
        return playListItems.size();
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
                    results.values = backupList;
                    results.count = backupList.size();
                }
                else {
                    // We perform filtering operation
                    ArrayList<PlayListItem> listItems = new ArrayList<PlayListItem>();

                    for (PlayListItem p : playListItems) {
                        if (p.getTitle().toUpperCase().contains(constraint.toString().toUpperCase()))
                            listItems.add(p);
                    }

                    results.values = listItems;
                    results.count = listItems.size();

                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                // Now we have to inform the adapter about the new list filtered
                if (results.count == 0){
                    playListItems.clear();
                    notifyDataSetChanged();
                }
                else {
                    playListItems.clear();
                    playListItems.addAll((ArrayList<PlayListItem>) results.values);
                    notifyDataSetChanged();
                }
            }
        };
    }
    /***
     * End Override field
     */
}
