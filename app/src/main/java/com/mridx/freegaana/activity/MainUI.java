/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.mridx.freegaana.R;
import com.mridx.freegaana.dataholder.SongData;
import com.mridx.freegaana.dataholder.TopChart;
import com.mridx.freegaana.helper.MainUIHelper;

import org.jsoup.Jsoup;

import java.io.UnsupportedEncodingException;

import static com.mridx.freegaana.activity.SongUI.FROM_PLAYLIST;
import static com.mridx.freegaana.activity.SongUI.FROM_SONG;

public class MainUI extends AppCompatActivity {

    public RecyclerView trendingHolder, newReleaseHolder, topChartsHolder;
    private MainUIHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ui);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(getString(R.string.app_name));

        findViews();
        helper = new MainUIHelper(this);
        helper.start();
    }

    private void findViews() {
        trendingHolder = findViewById(R.id.trendingHolder);
        newReleaseHolder = findViewById(R.id.newReleaseHolder);
        topChartsHolder = findViewById(R.id.topChartsHolder);
    }

    public void handleClick(SongData songData) {
        Intent intent = new Intent(this, SongUI.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("SONG", songData);
        intent.putExtras(bundle);
        intent.putExtra("LAYOUT_CODE", FROM_SONG);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, trendingHolder, "thumbnail");
        startActivity(intent, optionsCompat.toBundle());
    }

    public void handleTopChartsClick(TopChart topChart) {
        Intent intent = new Intent(this, PlaylistUI.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("PLAYLIST", topChart);
        intent.putExtras(bundle);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, topChartsHolder, "playlistAlbum"
        );
        startActivity(intent, optionsCompat.toBundle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Ex. Emiway or Jump Kar");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.setQuery("", false);
                Intent i = new Intent(getApplicationContext(), SearchUI.class);
                i.putExtra("QUERY", query);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }

}
