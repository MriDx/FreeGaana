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
import com.mridx.freegaana.activity.PlaylistUI;
import com.mridx.freegaana.dataholder.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Song> songsList = new ArrayList<>();

    public PlaylistAdapter(Context context) {
        this.context = context;
    }

    public PlaylistAdapter(Context context, ArrayList<Song> songsList) {
        this.context = context;
        this.songsList = songsList;
    }

    @NonNull
    @Override
    public PlaylistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_view_horizontal, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.MyViewHolder holder, int position) {
        Song song = songsList.get(position);
        holder.songTitleView.setText(song.getSongName());
        holder.songAlbumTitleView.setText(song.getAlbumTitle());
        Picasso.get()
                .load(song.getAlbumArtwork())
                .into(holder.songThumbView);
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public void setSongsList(ArrayList<Song> playlistSongs) {
        this.songsList = playlistSongs;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView songThumbView;
        private AppCompatTextView songTitleView, songAlbumTitleView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            songThumbView = itemView.findViewById(R.id.songThumbView);
            songTitleView = itemView.findViewById(R.id.songTitleView);
            songAlbumTitleView = itemView.findViewById(R.id.songAlbumTitleView);
            itemView.setOnClickListener(v -> handleClick(getAdapterPosition()));
        }
    }

    private void handleClick(int adapterPosition) {
        ((PlaylistUI)context).handleClick(songsList.get(adapterPosition));
    }
}
