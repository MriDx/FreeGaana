/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
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

        findViews();

        initialQuery = getIntent().getExtras().getString("QUERY");

        helper = new SearchUIHelper(this);
        helper.start();
    }


    private void findViews() {
        songsHolder = findViewById(R.id.trendingHolder);
        topResultsHolder = findViewById(R.id.newReleaseHolder);
        albumsHolder = findViewById(R.id.topChartsHolder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search_menu, menu);
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
}
