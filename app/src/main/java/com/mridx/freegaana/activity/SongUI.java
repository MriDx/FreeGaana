/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mridx.freegaana.R;
import com.mridx.freegaana.dataholder.SongData;
import com.mridx.freegaana.helper.SongHelper;

public class SongUI extends AppCompatActivity {

    public SongData songData;
    public AppCompatImageView thumbnailView;
    public AppCompatTextView songNameView, albumNameView, artistsNameView;
    private SongHelper helper;
    public FloatingActionButton songDownload;
    public ConstraintLayout floatingBtnMask, parentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_ui);

        songData = (SongData) getIntent().getSerializableExtra("SONG");

        findView();
        helper = new SongHelper(this);
        helper.start();

    }

    private void findView() {
        thumbnailView = findViewById(R.id.thumbnailView);
        songNameView = findViewById(R.id.songNameView);
        albumNameView = findViewById(R.id.albumNameView);
        artistsNameView = findViewById(R.id.artistsNameView);
        songDownload = findViewById(R.id.songDownload);
        floatingBtnMask = findViewById(R.id.floatingBtnMask);
        parentView = findViewById(R.id.parentView);
    }
}
