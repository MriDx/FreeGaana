/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mridx.freegaana.dataholder.Links;
import com.mridx.freegaana.dataholder.Song;
import com.mridx.freegaana.dataholder.SongData;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ScrapPlaylist extends AsyncTask<Void, Void, String> {

    private Context context;
    private ArrayList<Song> songsList = new ArrayList<>();
    private String url;

    public ScrapPlaylist(Context context) {
        this.context = context;
    }

    public ScrapPlaylist(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    OnPlaylistScrapingComplete onPlaylistScrapingComplete;
    public interface OnPlaylistScrapingComplete {
        void setOnPlaylistScrapingCompleteListener(ArrayList<Song> songsList);
    }
    public void setOnPlaylistScrapingComplete(OnPlaylistScrapingComplete onPlaylistScrapingComplete) {
        this.onPlaylistScrapingComplete = onPlaylistScrapingComplete;
    }


    @Override
    protected String doInBackground(Void... voids) {

        try {
            Document document = Jsoup.connect(Links.HOMEPAGE_URL + url).get();
            Elements songs = document.select(".content-container").select("span");
            for (int i = 0; i < songs.size(); i++) {
                if (i % 2 == 0) {
                    Log.d("kaku", "doInBackground: " + songs.get(i).text());
                    JSONObject object = new JSONObject(songs.get(i).text());
                    JSONObject path = object.getJSONObject("path");
                    Song song = new Song(
                            object.getString("title"),
                            new Utility().formatArtist(object.getString("artist")),
                            new Utility().unescapeString(object.getString("albumtitle")),
                            object.getString("artwork"),
                            object.getString("albumartwork"),
                            object.getString("release_date"),
                            path.getJSONArray("high").getJSONObject(0).getString("message"),
                            path.getJSONArray("medium").getJSONObject(0).getString("message"),
                            object.getString("lyrics_url"),
                            object.getString("share_url"),
                            object.getString("album_id")
                    );
                    songsList.add(song);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        onPlaylistScrapingComplete.setOnPlaylistScrapingCompleteListener(songsList);
    }
}
