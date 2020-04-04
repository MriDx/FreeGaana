/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.dataholder;

public class Song {

    private String highData, lowData, lyrics_url, artists, albumTitle;

    public Song(String highData, String lowData, String lyrics_url) {
        this.highData = highData;
        this.lowData = lowData;
        this.lyrics_url = lyrics_url;
    }

    public Song(String highData, String lowData, String lyrics_url, String artists, String albumTitle) {
        this.highData = highData;
        this.lowData = lowData;
        this.lyrics_url = lyrics_url;
        this.artists = artists;
        this.albumTitle = albumTitle;
    }

    public String getHighData() {
        return highData;
    }

    public String getLowData() {
        return lowData;
    }

    public String getLyrics_url() {
        return lyrics_url;
    }

    public String getArtists() {
        return artists;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }
}
