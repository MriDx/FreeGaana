/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;

import com.mridx.freegaana.activity.PlaylistUI;
import com.mridx.freegaana.adapter.PlaylistAdapter;
import com.mridx.freegaana.dataholder.Song;

import java.util.ArrayList;

public class PlaylistHelper {

    private Context context;
    private PlaylistUI playlistUI;
    private PlaylistAdapter playlistAdapter;
    private ArrayList<Song> songsList = new ArrayList<>(); //array list for playlist songs

    public PlaylistHelper(Context context) {
        this.context = context;
        playlistUI = ((PlaylistUI)context);
    }

    public void start() {
        startScraping();
        populateView();
    }

    private void populateView() {
        playlistAdapter = new PlaylistAdapter(context, songsList);
        playlistUI.songsHolder.setLayoutManager(new GridLayoutManager(context, 1));
        playlistUI.songsHolder.setAdapter(playlistAdapter);
    }

    private void startScraping() {
        Scrapper scrapper = new Scrapper(context);
        scrapper.scrapAlbum(playlistUI.topChart.getUrl() /*"/playlist/gaana-dj-hindi-top-50-1"*/);
        scrapper.setOnScrappingComplete((songData, topCharts, playlistSongs, dataType) -> setPlaylist(playlistSongs));
    }

    private void setPlaylist(ArrayList<Song> playlistSongs) {
        playlistAdapter.setSongsList(playlistSongs);
    }

}
