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

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

public class Trending extends AsyncTask<Void, Void, String> {

    private Context context;
    private ArrayList<SongData> trendingSongs = new ArrayList<>();

    public Trending(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        Connection connection = Jsoup.connect(Links.HOMEPAGE_URL);
        try {
            Document document = connection.get();
            Element trendingNode = document.select("#trendingsong").first();
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
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
        return "success";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        onComplete.setOnCompletedListener(trendingSongs);
    }
    OnComplete onComplete;
    public interface OnComplete {
        void setOnCompletedListener(ArrayList<SongData> songData);
    }
    public void setOnComplete(OnComplete onComplete) {
        this.onComplete = onComplete;
    }

    private String formatArtist(String data) {
        String[] artists = data.split(",");
        for (int i = 0; i < artists.length; i++) {
            artists[i] = artists[i].split("###")[0];
        }
        return TextUtils.join(", ", artists);
    }

}
