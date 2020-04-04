/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.mridx.freegaana.dataholder.Links;
import com.mridx.freegaana.dataholder.Song;
import com.mridx.freegaana.dataholder.SongData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class SongScrapper extends AsyncTask<Void, Void, String> {

    private Context context;
    private String songUrl;
    private String albumId;

    private Song song;


    public SongScrapper(Context context) {
        this.context = context;
    }

    public SongScrapper(Context context, String songUrl) {
        this.context = context;
        this.songUrl = songUrl;
    }

    public SongScrapper(Context context, String songUrl, String albumId) {
        this.context = context;
        this.songUrl = songUrl;
        this.albumId = albumId;
    }
    OnSongScrapingComplete onSongScrapingComplete;
    public interface OnSongScrapingComplete {
        void setOnSongScrapingCompleteListener(Song song);
    }
    public void setOnSongScrapingComplete(OnSongScrapingComplete onSongScrapingComplete) {
        this.onSongScrapingComplete = onSongScrapingComplete;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            Document document = Jsoup.connect(Links.HOMEPAGE_URL + songUrl).get();
            Element element = document.select(".sourcelist_"+albumId).first();
            JSONObject object = new JSONObject(element.text());
            song = generateSong(object);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }

        return "success";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null) {
            onSongScrapingComplete.setOnSongScrapingCompleteListener(song);
        }
    }


    private Song generateSong(JSONObject object) throws JSONException {
        JSONObject path = object.getJSONObject("path");
        String medium = path.getJSONArray("medium").getJSONObject(0).getString("message");
        String high = path.getJSONArray("high").getJSONObject(0).getString("message");
        String lyrics = object.getString("lyrics_url");
        String artists = formatArtist(object.getString("artist"));
        String albumTitle = unescapeCharacters(object.getString("albumtitle"));
        return new Song(high, medium, lyrics, artists, albumTitle);
    }

    private String formatArtist(String data) {
        String[] artists = data.split(",");
        for (int i = 0; i < artists.length; i++) {
            artists[i] = artists[i].split("###")[0];
        }
        return TextUtils.join(", ", artists);
    }

    private String unescapeCharacters(String data) {
        return Html.fromHtml(data).toString().replaceAll("&quot;", "\"");
    }
}
