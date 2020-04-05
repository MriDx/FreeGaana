/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mridx.freegaana.R;
import com.mridx.freegaana.dataholder.SongData;
import com.mridx.freegaana.helper.SearchUIHelper;

import static com.mridx.freegaana.activity.SongUI.FROM_SEARCH;
import static com.mridx.freegaana.activity.SongUI.FROM_SONG;

public class SearchUI extends AppCompatActivity {

    public RecyclerView topResultsHolder, songsHolder, albumsHolder;
    private SearchUIHelper helper;
    public String initialQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_ui);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        findViews();

        initialQuery = getIntent().getExtras().getString("QUERY");

        helper = new SearchUIHelper(this);
        helper.start();
    }


    private void findViews() {
        songsHolder = findViewById(R.id.songsHolder);
        /*songsHolder = findViewById(R.id.trendingHolder);
        albumsHolder = findViewById(R.id.newReleaseHolder);
        topResultsHolder = findViewById(R.id.topChartsHolder);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.searchui_menu, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setQueryHint("Ex. Emiway or Jump Kar");
        searchView.setIconified(false);
        searchView.setQuery(initialQuery, false);
        //hideKeyboard(getWindow().getDecorView());
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                helper.startScraping(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    public void handleClick(SongData songData) {
        Intent intent = new Intent(this, SongUI.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("SONG", songData);
        intent.putExtras(bundle);
        intent.putExtra("LAYOUT_CODE", FROM_SEARCH);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, songsHolder, "thumbnail");
        startActivity(intent, optionsCompat.toBundle());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
