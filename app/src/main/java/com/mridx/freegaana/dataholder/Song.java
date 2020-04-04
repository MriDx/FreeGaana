/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.dataholder;

import java.io.Serializable;

public class Song implements Serializable {

    private String songName, songUrl, albumId, albumArtwork, release_date, artwork;
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

    public Song(String songName,
                String artists,
                String albumTitle,
                String artwork,
                String albumArtwork,
                String release_date,
                String highData,
                String lowData,
                String lyrics_url,
                String songUrl,
                String albumId
                ) {
        this.songName = songName;
        this.albumArtwork = albumArtwork;
        this.release_date = release_date;
        this.artwork = artwork;
        this.highData = highData;
        this.lowData = lowData;
        this.lyrics_url = lyrics_url;
        this.artists = artists;
        this.albumTitle = albumTitle;
        this.songUrl = songUrl;
        this.albumId = albumId;
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

    public String getSongName() {
        return songName;
    }

    public String getAlbumArtwork() {
        return albumArtwork;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getArtwork() {
        return artwork;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public String getAlbumId() {
        return albumId;
    }
}
