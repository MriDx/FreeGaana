/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.content.Context;
import android.os.AsyncTask;

import com.mridx.freegaana.dataholder.Links;
import com.mridx.freegaana.dataholder.SongData;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ScrapSearch extends AsyncTask<Void, Void, String> {

    private Context context;
    private String query;
    private ArrayList<SongData> songDataList = new ArrayList<>();

    public ScrapSearch(Context context) {
        this.context = context;
    }

    public ScrapSearch(Context context, String query) {
        this.context = context;
        this.query = query;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            Document document = Jsoup.connect(Links.SEARCH_URL + query).get();
            Element songListNode = document.select(".songlist-type2").first();
            Elements songs = songListNode.select("span");
            for (int i = 0; i < songs.size(); i++) {
                if (i % 2 == 0) {
                    JSONObject object = new JSONObject(songs.get(i).text());
                    SongData songData = new SongData(
                            object.getString("title"),
                            object.getString("artist"),
                            object.getString("albumartwork"),
                            object.getString("share_url"),
                            object.getString("albumtitle"),
                            object.getString("id")
                    );
                    songDataList.add(songData);
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
        onSearchScrapingComplete.setOnSearchScrapingComplete(songDataList);
    }


    OnSearchScrapingComplete onSearchScrapingComplete;
    public interface OnSearchScrapingComplete {
        void setOnSearchScrapingComplete(ArrayList<SongData> songData);
    }
    public void setOnSearchScrapingComplete(OnSearchScrapingComplete onSearchScrapingComplete) {
        this.onSearchScrapingComplete = onSearchScrapingComplete;
    }

}
