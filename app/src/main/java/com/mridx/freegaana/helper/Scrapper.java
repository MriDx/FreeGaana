/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.content.Context;
import android.os.AsyncTask;

import com.mridx.freegaana.dataholder.Song;
import com.mridx.freegaana.dataholder.SongData;

import org.jsoup.Jsoup;

import java.util.ArrayList;

public class Scrapper {

    private Context context;

    public Scrapper(Context context) {
        this.context = context;
    }

    OnScrappingComplete onScrappingComplete;
    public interface OnScrappingComplete {
        void setOnScrappingComplete(ArrayList<SongData> songData, int dataType);
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



    OnTrendingCompleteListener onTrendingCompleteListener;
    public interface OnTrendingCompleteListener {
        void setOnTrendingCompleteListener(ArrayList<SongData> trendingSongs);
    }
    public void setOnTrendingCompleteListener(OnTrendingCompleteListener onTrendingCompleteListener) {
        this.onTrendingCompleteListener = onTrendingCompleteListener;
    }

    OnNewReleaseCompleteListener onNewReleaseCompleteListener;
    public interface OnNewReleaseCompleteListener {
        void setOnNewReleaseCompleteListener(ArrayList<SongData> newReleaseSongs);
    }
    public void setOnNewReleaseCompleteListener(OnNewReleaseCompleteListener onNewReleaseCompleteListener) {
        this.onNewReleaseCompleteListener = onNewReleaseCompleteListener;
    }

    public void scrapTrending() {
        Trending trending = new Trending(context);
        trending.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        trending.setOnComplete(new Trending.OnComplete() {
            @Override
            public void setOnCompletedListener(ArrayList<SongData> songData) {
                onScrappingComplete.setOnScrappingComplete(songData, 0);
            }
        });
    }

    public void scrapSong(String songUrl) {

    }

    public void scrapSong(String songUrl, String albumId) {
        SongScrapper songScrapper = new SongScrapper(context, songUrl, albumId);
        songScrapper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        songScrapper.setOnSongScrapingComplete(song -> onSongScrapingComplete.setOnSongScrapingComplete(song));
    }

    public void scrapHomepage() {
        ScrapHomePage scrapHomePage = new ScrapHomePage(context);
        scrapHomePage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        scrapHomePage.setOnTrendingCompleteListener(trendingSongs -> onScrappingComplete.setOnScrappingComplete(trendingSongs, 0) );
        scrapHomePage.setOnNewReleaseCompleteListener(newReleaseSongs -> onScrappingComplete.setOnScrappingComplete(newReleaseSongs, 1));
    }



}
