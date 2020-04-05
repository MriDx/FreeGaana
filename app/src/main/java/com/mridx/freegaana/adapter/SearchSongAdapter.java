/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.mridx.freegaana.R;
import com.mridx.freegaana.activity.SearchUI;
import com.mridx.freegaana.dataholder.Song;
import com.mridx.freegaana.dataholder.SongData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchSongAdapter extends RecyclerView.Adapter<SearchSongAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<SongData> songDataList = new ArrayList<>();

    public SearchSongAdapter(Context context) {
        this.context = context;
    }

    public SearchSongAdapter(Context context, ArrayList<SongData> songDataList) {
        this.context = context;
        this.songDataList = songDataList;
    }

    @NonNull
    @Override
    public SearchSongAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_view_horizontal, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchSongAdapter.MyViewHolder holder, int position) {
        SongData songData = songDataList.get(position);
        holder.songTitleView.setText(songData.getSongName());
        holder.songAlbumTitleView.setText(songData.getAlbumName());
        Picasso.get()
                .load(songData.getThumbnail())
                .into(holder.songThumbView);
    }

    @Override
    public int getItemCount() {
        return songDataList.size();
    }

    public void setSongDataList(ArrayList<SongData> songDataList) {
        this.songDataList = songDataList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView songTitleView, songAlbumTitleView;
        private AppCompatImageView songThumbView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitleView = itemView.findViewById(R.id.songTitleView);
            songAlbumTitleView = itemView.findViewById(R.id.songAlbumTitleView);
            songThumbView = itemView.findViewById(R.id.songThumbView);
            itemView.setOnClickListener(v -> handleClick(getAdapterPosition()));
        }
    }

    private void handleClick(int adapterPosition) {
        ((SearchUI)context).handleClick(songDataList.get(adapterPosition));
    }
}
