/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.content.Context;
import android.os.AsyncTask;

import com.mridx.freegaana.dataholder.Song;
import com.mridx.freegaana.dataholder.SongData;
import com.mridx.freegaana.dataholder.TopChart;

import org.jsoup.Jsoup;

import java.util.ArrayList;

public class Scrapper {

    private Context context;
    public static int TRENDING = 0, NEW_RELEASE = 1, TOP_CHARTS = 2, PLAYLIST = 3;

    public Scrapper(Context context) {
        this.context = context;
    }

    OnScrappingComplete onScrappingComplete;



    public interface OnScrappingComplete {
        void setOnScrappingComplete(ArrayList<SongData> songData, ArrayList<TopChart> topCharts, ArrayList<Song> playlistSongs, int dataType);
    }
    public void setOnScrappingComplete(OnScrappingComplete onScrappingComplete) {
        this.onScrappingComplete = onScrappingComplete;
    }

    OnSongScrapingComplete onSongScrapingComplete;
    public interface OnSongScrapingComplete {
        void setOnSongScrapingComplete(Song song);
    }
    public void setOnSongScrapingComplete(OnSongScrapingComplete onSongScrapingComplete) {
        this.onSongScrapingComplete = onSongScrapingComplete;
    }



    public void scrapSong(String songUrl, String albumId) {
        SongScrapper songScrapper = new SongScrapper(context, songUrl, albumId);
        songScrapper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        songScrapper.setOnSongScrapingComplete(song -> onSongScrapingComplete.setOnSongScrapingComplete(song));
    }

    public void scrapHomepage() {
        ScrapHomePage scrapHomePage = new ScrapHomePage(context);
        scrapHomePage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        scrapHomePage.setOnTrendingCompleteListener(trendingSongs -> onScrappingComplete.setOnScrappingComplete(trendingSongs, null, null,  TRENDING) );
        scrapHomePage.setOnNewReleaseCompleteListener(newReleaseSongs -> onScrappingComplete.setOnScrappingComplete(newReleaseSongs, null, null, NEW_RELEASE));
        scrapHomePage.setOnTopChartCompleteListener(topChartSongs -> onScrappingComplete.setOnScrappingComplete(null, topChartSongs, null,  TOP_CHARTS));
    }

    public void scrapAlbum(String url) {
        ScrapPlaylist scrapPlaylist = new ScrapPlaylist(context, url);
        scrapPlaylist.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        scrapPlaylist.setOnPlaylistScrapingComplete(songsList -> onScrappingComplete.setOnScrappingComplete(null, null, songsList,  PLAYLIST));
    }



}
