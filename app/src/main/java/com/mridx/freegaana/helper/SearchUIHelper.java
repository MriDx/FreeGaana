/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mridx.freegaana.activity.SearchUI;
import com.mridx.freegaana.adapter.SearchSongAdapter;
import com.mridx.freegaana.adapter.SongsAdapter;
import com.mridx.freegaana.dataholder.Song;
import com.mridx.freegaana.dataholder.SongData;
import com.mridx.freegaana.dataholder.TopChart;

import java.util.ArrayList;

import static com.mridx.freegaana.helper.Scrapper.SEARCH_SONGS;

public class SearchUIHelper {

    private Context context;
    private SearchUI searchUI;
    private SongsAdapter songsAdapter, albumAdapter;
    private SearchSongAdapter searchSongAdapter;
    private ArrayList<SongData> songDataList = new ArrayList<>();
    private ArrayList<SongData> albumDataList = new ArrayList<>();
    private Scrapper scrapper;

    public SearchUIHelper(Context context) {
        this.context = context;
        searchUI = ((SearchUI)context);
    }

    public void start() {
        scrapper = new Scrapper(context);
        startScraping(searchUI.initialQuery);
        populateResults();
    }

    private void populateResults() {
        searchSongAdapter = new SearchSongAdapter(context, songDataList);
        searchUI.songsHolder.setLayoutManager(new GridLayoutManager(context, 1));
        searchUI.songsHolder.setAdapter(searchSongAdapter);
    }

    /*private void populateResults() {
        *//*songsAdapter = new SongsAdapter(context, songDataList, 0);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 1);
        //layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        searchUI.songsHolder.setLayoutManager(layoutManager);
        searchUI.songsHolder.setAdapter(songsAdapter);*//*

        *//*albumAdapter = new SongsAdapter(context, albumDataList, 0);
        GridLayoutManager layoutManager1 = new GridLayoutManager(context, 1);
        layoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        searchUI.albumsHolder.setLayoutManager(layoutManager1);
        searchUI.albumsHolder.setAdapter(albumAdapter);*//*
    }*/

    public void startScraping(String query) {
        songDataList.clear();
        //albumDataList.clear();
        scrapper.scrapSearch(query);
        /*scrapper.setOnScrappingComplete((songData, albumData, dataType) -> {
            if (dataType == SEARCH_SONGS) {
                setSearchSongs(songData);
            }
        });*/
        scrapper.setOnSearchComplete((songData, albumData, dataType) -> {
            setSearchSongs(songData);
            //setSearchAlbums(albumData);
        });

    }

    private void setSearchAlbums(ArrayList<SongData> albumData) {
        albumAdapter.setSongDataList(albumData);
    }

    private void setSearchSongs(ArrayList<SongData> songData) {
        //songsAdapter.setSongDataList(songData);
        searchSongAdapter.setSongDataList(songData);
    }
}
