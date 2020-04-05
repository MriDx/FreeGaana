/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mridx.freegaana.activity.SearchUI;
import com.mridx.freegaana.adapter.SongsAdapter;
import com.mridx.freegaana.dataholder.SongData;

import java.util.ArrayList;

import static com.mridx.freegaana.helper.Scrapper.SEARCH_SONGS;

public class SearchUIHelper {

    private Context context;
    private SearchUI searchUI;
    private SongsAdapter songsAdapter;
    private ArrayList<SongData> songDataList = new ArrayList<>();

    public SearchUIHelper(Context context) {
        this.context = context;
        searchUI = ((SearchUI)context);
    }

    public void start() {
        startScraping(searchUI.initialQuery);
        populateResults();
    }

    private void populateResults() {
        songsAdapter = new SongsAdapter(context, songDataList, 0);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 1);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        searchUI.songsHolder.setLayoutManager(layoutManager);
        searchUI.songsHolder.setAdapter(songsAdapter);
    }

    private void startScraping(String query) {
        Scrapper scrapper = new Scrapper(context);
        scrapper.scrapSearch(query);
        scrapper.setOnScrappingComplete((songData, topCharts, playlistSongs, dataType) -> {
            if (dataType == SEARCH_SONGS) {
                setSearchSongs(songData);
            }
        });
    }

    private void setSearchSongs(ArrayList<SongData> songData) {
        songsAdapter.setSongDataList(songData);
    }
}
