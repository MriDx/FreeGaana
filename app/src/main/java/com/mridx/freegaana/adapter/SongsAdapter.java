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
import com.mridx.freegaana.activity.MainUI;
import com.mridx.freegaana.dataholder.SongData;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<SongData> songDataList = new ArrayList<>();
    private int layoutType = -1;

    public SongsAdapter(Context context, ArrayList<SongData> songDataList) {
        this.context = context;
        this.songDataList = songDataList;
    }

    public SongsAdapter(Context context, ArrayList<SongData> songDataList, int layoutType) {
        this.context = context;
        this.songDataList = songDataList;
        this.layoutType = layoutType;
    }

    public void setSongDataList(ArrayList<SongData> songDataList) {
        this.songDataList = songDataList;
        //notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SongsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.song_view, null);
            return new MyViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.song_view_large, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsAdapter.MyViewHolder holder, int position) {
        SongData songData = songDataList.get(position);
        holder.songNameView.setText(songData.getSongName());
        holder.songArtist.setText(songData.getArtistDetails());
        Picasso.get()
                .load(songData.getThumbnail())
                .into(holder.thumbView);
    }

    @Override
    public int getItemCount() {
        return songDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView thumbView;
        private AppCompatTextView songNameView, songArtist;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbView = itemView.findViewById(R.id.songThumbView);
            songNameView = itemView.findViewById(R.id.songNameView);
            songArtist = itemView.findViewById(R.id.songInfoView);
            //songArtist.setVisibility(View.GONE);
            itemView.setOnClickListener(v -> handleSongClick(getAdapterPosition()));
        }
    }
    private void handleSongClick(int adapterPosition) {
        ((MainUI)context).handleClick(songDataList.get(adapterPosition));
    }
}
