/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.mridx.freegaana.R;
import com.mridx.freegaana.activity.SongUI;
import com.mridx.freegaana.dataholder.Song;
import com.squareup.picasso.Picasso;

public class SongHelper {

    private Context context;
    private SongUI songUI;
    private Scrapper scrapper;
    private Song song;
    private int HIGH = 0, MEDIUM = 1;
    private GaanaDownloader downloader;

    public SongHelper(Context context) {
        this.context = context;
        songUI = ((SongUI) context);
    }

    public void start() {
        populateViews();
        scrapSong(songUI.songData.getSongUrl());
    }

    private void scrapSong(String songUrl) {
        scrapper = new Scrapper(context);
        scrapper.scrapSong(songUrl, songUI.songData.getAlbumId());
        scrapper.setOnSongScrapingComplete(song -> {
            this.song = song;
            showDownload();
            showOthers(song);
        });

    }

    private void showOthers(Song song) {
        if (songUI.songData.getAlbumName().toLowerCase().equalsIgnoreCase("null") || songUI.songData.getAlbumName().length() == 0) {
            songUI.albumNameView.setText(song.getAlbumTitle());
        }
        if (songUI.songData.getArtistDetails().toLowerCase().equalsIgnoreCase("null") || songUI.songData.getArtistDetails().length() == 0) {
            songUI.artistsNameView.setText(song.getArtists());
        }
    }

    private void showDownload() {
        songUI.floatingBtnMask.setVisibility(View.GONE);
        songUI.songDownload.setVisibility(View.VISIBLE);
    }

    private void populateViews() {
        songUI.songNameView.setText(songUI.songData.getSongName());
        songUI.albumNameView.setText(songUI.songData.getAlbumName());
        songUI.artistsNameView.setText(songUI.songData.getArtistDetails());
        Picasso.get()
                .load(getHDThumbnail(songUI.songData.getThumbnail()))
                .into(songUI.thumbnailView);
        songUI.songDownload.setOnClickListener(v -> handleDownloadClick());
    }

    private void handleDownloadClick() {
        BottomSheetDialog bottomSheetDialog = createBtmSheet();
        bottomSheetDialog.show();
        bottomSheetDialog.findViewById(R.id.highDownload).setOnClickListener(v -> {
            startDownload(HIGH);
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.findViewById(R.id.mediumDownload).setOnClickListener(v -> {
            startDownload(MEDIUM);
            bottomSheetDialog.dismiss();
        });
    }

    private void startDownload(int type) {
        AlertDialog downloadAlert = createDownloadAlert();
        downloadAlert.show();
        downloadAlert.setCancelable(false);
        ContentLoadingProgressBar downloadIndicator = downloadAlert.findViewById(R.id.downloadIndicator);
        AppCompatTextView downloadOk = downloadAlert.findViewById(R.id.downloadOk);
        downloadOk.setOnClickListener(v -> downloadAlert.dismiss());

        if (type == HIGH) {
            String media = generateURL(song.getHighData());
            downloader = new GaanaDownloader();
            downloader.startDownload(context, media, songUI.songData.getSongName());
        } else {
            String media = generateURL(song.getLowData());
            downloader = new GaanaDownloader();
            downloader.startDownload(context, media, songUI.songData.getSongName());
        }

        downloader.setOnGetMaxDuration(duration -> {
            downloadIndicator.setMax(Integer.parseInt(String.valueOf(duration).split("\\.")[0]));
        });
        downloader.setOnProgress(progress -> downloadIndicator.setProgress(Integer.parseInt(String.valueOf(progress).split("\\.")[0])));

        downloader.setOnFailed(() -> {
            downloadAlert.dismiss();
            showSnackbar(0);
        });
        downloader.setOnCompleted(() -> {
            downloadOk.setVisibility(View.VISIBLE);
            ((AppCompatTextView) downloadAlert.findViewById(R.id.downloadingHeading)).setText("Download Completed !");
        });

    }

    private void showSnackbar(int i) {
        Snackbar.make(songUI.parentView, "Connection Error ! try later", Snackbar.LENGTH_SHORT).show();
    }

    private AlertDialog createDownloadAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.donwloading_view);
        return builder.create();
    }

    private String generateURL(String encrypted) {
        return new GaanaDecrypt().decrypt(encrypted);
    }

    private BottomSheetDialog createBtmSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.song_download_view);
        return bottomSheetDialog;
    }

    private String getHDThumbnail(String url) {
        String[] data = url.split("/");
        data[0] = data[0] + "/";
        String[] endPart = data[7].split("_");
        endPart[1] = "480x480";
        data[7] = TextUtils.join("_", endPart);
        //data[7] = data[7].split("_")[0] + "_480x480_" + data[7].split("_")[2];
        return TextUtils.join("/", data);
    }
}
