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
import com.youtubeapp.doto.dotayoutubechanels.activities.models.PLayList;
import com.youtubeapp.doto.dotayoutubechanels.activities.utils.CachingBitmaps;

import java.util.ArrayList;

/**
 * Created by admin on 1/15/2016.
 */
public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> implements Filterable {
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private ArrayList<PLayList> playLists;
    private ArrayList<PLayList> backupList = new ArrayList<PLayList>();

    private OnPlaylistInnerItemClickListener playlistInnerItemClickListener;

    public PlayListAdapter(Context context, ArrayList<PLayList> pLayLists){
        this.mContext = context;
        this.playLists = pLayLists;
        this.backupList.addAll(pLayLists);
    }

    @Override
    public PlayListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_play_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.bt_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlistInnerItemClickListener.onShowContextMenuClick(v, viewHolder.getPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (playLists.size() != 0) {
            PLayList item = playLists.get(position);
            new CachingBitmaps().loadBitmap(playLists.get(position)
                    .getThumbnails(), holder.placePlayListThumbnail, null);
            holder.placePlayListTitle.setText(item.getTitle());

            holder.placePlayListVideoCount.setText(String.valueOf(item.getItemCount()));

            if (position % 2 == 0) {
                holder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.transparent20));
            } else {
                holder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.transparent40));
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout placeHolder;
        public CardView cardView;

        public TextView placePlayListTitle;
        public ImageView placePlayListThumbnail;
        public TextView placePlayListVideoCount;

        public ImageButton bt_option;

        public ViewHolder(View itemView) {
            super(itemView);
            placeHolder = (LinearLayout) itemView.findViewById(R.id.playListMainHolder);
            placeHolder.setOnClickListener(this);
            placePlayListTitle = (TextView) itemView.findViewById(R.id.tv_play_list_title);
            placePlayListThumbnail = (ImageView) itemView.findViewById(R.id.img_play_list_thumbnail);
            placePlayListVideoCount = (TextView) itemView.findViewById(R.id.tv_play_list_video_count);

            cardView = (CardView) itemView.findViewById(R.id.card_view_play_list_item);

            bt_option = (ImageButton)itemView.findViewById(R.id.bt_show_context_menu_play_list);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }
    }

    public void restoreList(){
        playLists.clear();
        playLists.addAll(backupList);
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
        return playLists.size();
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
                    ArrayList<PLayList> playListArrayList = new ArrayList<PLayList>();

                    for (PLayList p : playLists) {
                        if (p.getTitle().toUpperCase().contains(constraint.toString().toUpperCase()))
                            playListArrayList.add(p);
                    }

                    results.values = playListArrayList;
                    results.count = playListArrayList.size();

                }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                // Now we have to inform the adapter about the new list filtered
                if (results.count == 0){
                    playLists.clear();
                    notifyDataSetChanged();
                }
                else {
                    playLists.clear();
                    playLists.addAll((ArrayList<PLayList>) results.values);
                    notifyDataSetChanged();
                }
            }
        };
    }
    /***
     * End Override field
     */

}
