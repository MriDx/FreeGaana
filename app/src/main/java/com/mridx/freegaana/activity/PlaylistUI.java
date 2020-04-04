/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.mridx.freegaana.R;
import com.mridx.freegaana.dataholder.Song;
import com.mridx.freegaana.dataholder.TopChart;
import com.mridx.freegaana.helper.PlaylistHelper;
import com.mridx.freegaana.helper.Utility;
import com.squareup.picasso.Picasso;

import static com.mridx.freegaana.activity.SongUI.FROM_PLAYLIST;

public class PlaylistUI extends AppCompatActivity {

    private PlaylistHelper helper;
    public AppCompatImageView albumImageview;
    public RecyclerView songsHolder;
    public TopChart topChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_ui);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));

        topChart = (TopChart) getIntent().getSerializableExtra("PLAYLIST");

        findViews();

        helper = new PlaylistHelper(this);
        helper.start();

    }

    private void findViews() {
        albumImageview = findViewById(R.id.albumImageView);
        songsHolder = findViewById(R.id.songsHolder);
        Picasso.get()
                .load(new Utility().getHDThumbnail(topChart.getThumbnail()))
                .into(albumImageview);
        getSupportActionBar().setTitle(topChart.getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    public void handleClick(Song song) {
        Intent intent = new Intent(this, SongUI.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("SONG", song);
        intent.putExtras(bundle);
        intent.putExtra("LAYOUT_CODE", FROM_PLAYLIST);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                songsHolder,
                "playlistAlbum"
        );
        startActivity(intent, optionsCompat.toBundle());
    }
}
