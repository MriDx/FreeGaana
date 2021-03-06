/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.mridx.freegaana.dataholder.Links;
import com.mridx.freegaana.dataholder.SongData;
import com.mridx.freegaana.dataholder.TopChart;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ScrapHomePage extends AsyncTask<Void, Void, String> {

    private Context context;
    private ArrayList<SongData> trendingSongs = new ArrayList<>();
    private ArrayList<SongData> newReleaseSongs = new ArrayList<>();
    private ArrayList<TopChart> topChartsSongs = new ArrayList<>();

    public ScrapHomePage(Context context) {
        this.context = context;
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

    OnTopChartCompleteListener onTopChartCompleteListener;
    public interface OnTopChartCompleteListener {
        void setOnTopChartCompleteListener(ArrayList<TopChart> topChartSongs);
    }
    public void setOnTopChartCompleteListener(OnTopChartCompleteListener onTopChartCompleteListener) {
        this.onTopChartCompleteListener = onTopChartCompleteListener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        Connection connection = Jsoup.connect(Links.HOMEPAGE_URL);
        try {
            Document document = connection.get();
            Element trendingNode = document.select("#trendingsong").first();
            Element newReleaseNode = document.select("#newrelease").first();
            Element topChartsNode = document.select("#topchartsCarousel").first();

            //for trending songs
            Elements songsArray = trendingNode.select("#new-release-album").select(".sourcelist_trending");
            for (int i = 0; i < songsArray.size(); i++) {
                Log.d("kaku", "doInBackground: " + songsArray.get(i).text());
                JSONObject object = new JSONObject(songsArray.get(i).text());
                SongData songData = new SongData(object.getString("title"),
                        formatArtist(object.getString("artist")),
                        object.getString("albumartwork"),
                        object.getString("share_url"),
                        object.getString("albumtitle"),
                        object.getString("album_id"));
                trendingSongs.add(songData);
            }
            //onTrendingCompleteListener.setOnTrendingCompleteListener(trendingSongs);

            //for new releases
            Elements newReleaseArray = newReleaseNode.select("#new-release-album").select(".parentnode");
            for (int i = 0; i < newReleaseArray.size(); i++) {
                JSONObject object = new JSONObject(newReleaseArray.get(i).text());
                SongData songData = new SongData(
                        object.getString("title"),
                        formatArtist(object.getString("artist")),
                        object.getString("albumartwork"),
                        object.getString("share_url"),
                        object.getString("albumtitle"),
                        object.getString("id") );

                newReleaseSongs.add(songData);
            }
            //onNewReleaseCompleteListener.setOnNewReleaseCompleteListener(newReleaseSongs);

            //for top charts
            Elements topChartsArray = topChartsNode.select("span");
            for (int i = 0; i < topChartsArray.size(); i++) {
                if (i % 2 == 0) {
                    JSONObject object = new JSONObject(topChartsArray.get(i).text());
                    TopChart topChart = new TopChart(
                            object.getString("id"),
                            object.getString("title"),
                            object.getString("share_url"),
                            object.getString("albumartwork")
                    );
                    topChartsSongs.add(topChart);
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.d("kaku", "doInBackground: " + e);
            return null;
        }
        return "success";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        onTrendingCompleteListener.setOnTrendingCompleteListener(trendingSongs);
        onNewReleaseCompleteListener.setOnNewReleaseCompleteListener(newReleaseSongs);
        onTopChartCompleteListener.setOnTopChartCompleteListener(topChartsSongs);
    }

    private String formatArtist(String data) {
        String[] artists = data.split(",");
        for (int i = 0; i < artists.length; i++) {
            artists[i] = artists[i].split("###")[0];
        }
        return TextUtils.join(", ", artists);
    }
}
