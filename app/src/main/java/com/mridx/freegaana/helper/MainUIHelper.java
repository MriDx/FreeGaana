/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mridx.freegaana.activity.MainUI;
import com.mridx.freegaana.adapter.SongsAdapter;
import com.mridx.freegaana.adapter.TopChartsAdapter;
import com.mridx.freegaana.dataholder.SongData;
import com.mridx.freegaana.dataholder.TopChart;

import java.util.ArrayList;

public class MainUIHelper {

    private Context context;
    private MainUI mainUI;
    private SongsAdapter trendingAdapter, newReleaseAdapter;
    private TopChartsAdapter topChartsAdapter;
    private ArrayList<SongData> trendingSongs = new ArrayList<>();
    private ArrayList<SongData> newReleaseSongs = new ArrayList<>();
    private ArrayList<TopChart> topChartsSongs = new ArrayList<>();
    private GridLayoutManager trendingLayoutManager, newReleaseLayoutManager, topChartsLayoutManager;
    private static int TRENDING = 0, NEW_RELEASE = 1, TOP_CHARTS = 2;

    public MainUIHelper(Context context) {
        this.context = context;
        mainUI = ((MainUI)context);
    }

    public void start() {

        startScrapping();

        trendingAdapter = new SongsAdapter(context, trendingSongs, 0);
        trendingLayoutManager = new GridLayoutManager(context, 1);
        trendingLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mainUI.trendingHolder.setLayoutManager(trendingLayoutManager);
        mainUI.trendingHolder.setAdapter(trendingAdapter);
        //trendingAdapter.notifyDataSetChanged();

        newReleaseAdapter = new SongsAdapter(context, newReleaseSongs, 0);
        newReleaseLayoutManager = new GridLayoutManager(context, 1);
        newReleaseLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mainUI.newReleaseHolder.setLayoutManager(newReleaseLayoutManager);
        mainUI.newReleaseHolder.setAdapter(newReleaseAdapter);
        //newReleaseAdapter.notifyDataSetChanged();

        topChartsAdapter = new TopChartsAdapter(context, topChartsSongs);
        topChartsLayoutManager = new GridLayoutManager(context, 1);
        topChartsLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mainUI.topChartsHolder.setLayoutManager(topChartsLayoutManager);
        mainUI.topChartsHolder.setAdapter(topChartsAdapter);
        //topChartsAdapter.notifyDataSetChanged();

    }

    private void startScrapping() {
        Scrapper scrapper = new Scrapper(context);
        scrapper.scrapHomepage();
        scrapper.setOnScrappingComplete((songData, topCharts, playlistSongs,  dataType) -> {
            if (dataType == TRENDING) {
                setTrendingSongs(songData);
            } else if (dataType == NEW_RELEASE) {
                setNewReleaseSongs(songData);
            } else if (dataType == TOP_CHARTS) {
                setTopChartsSongs(topCharts);
            }
        });
    }

    /*private void startScrapping() {
        Scrapper scrapper = new Scrapper(context);
        scrapper.scrapTrending();
        scrapper.setOnScrappingComplete(new Scrapper.OnScrappingComplete() {
            @Override
            public void setOnScrappingComplete(ArrayList<SongData> songData, int dataType) {
                if (dataType == TRENDING) {
                    setTrendingSongs(songData);
                } else if (dataType == NEW_RELEASE) {
                    setNewReleaseSongs(songData);
                } else {

                }
            }
        });
    }*/



    public void setTrendingSongs(ArrayList<SongData> trendingSongs) {
        //this.trendingSongs = trendingSongs;
        trendingAdapter.setSongDataList(trendingSongs);
        //trendingAdapter.notifyDataSetChanged();
    }
    public void setNewReleaseSongs(ArrayList<SongData> newReleaseSongs) {
        //this.newReleaseSongs = newReleaseSongs;
        //newReleaseAdapter.notifyDataSetChanged();
        newReleaseAdapter.setSongDataList(newReleaseSongs);
        //newReleaseAdapter.notifyDataSetChanged();
    }
    public void setTopChartsSongs(ArrayList<TopChart> topChartsSongs) {
        //this.topChartsSongs = topChartsSongs;
        topChartsAdapter.setSongDataList(topChartsSongs);
        //topChartsAdapter.notifyDataSetChanged();
    }


}
